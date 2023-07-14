<template>
  <div class="app-container">
    <div>
      <el-input v-model="listQuery.name" placeholder="仓库名" style="width: 200px; margin-right: 20px" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>
      <el-button @click="handleDelete">批量删除</el-button>
      <el-button @click="toggleSelection()">取消选择</el-button>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 87% ;margin-top:20px;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column label="仓库名" width="200" align="center">
        <template slot-scope="{row}">
          <span>{{ row.houseName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库存储物资类型" min-width="200" align="center">
        <template slot-scope="{row}">
          <span>{{ row.houseType | housetypeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货区类型" width="200" align="center">
        <template slot-scope="{row}">
          <el-tag>{{ row.areaType | typeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="货架数目" width="200" align="center">
        <template slot-scope="{row}">
          <span>{{ row.shelveNum }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      align="center"
      :current-page="currentPage"
      :page-sizes="[1,5,10,20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.length"
      style="width: 87% ;margin-top:20px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="仓库名">
          <el-input v-model="form.houseName" placeholder="请输入仓库名" style="width: 311px; " />
        </el-form-item>
        <el-form-item label="存储物资类型">
          <el-select v-model="form.houseType" class="filter-item" placeholder="请选择该仓库存储物资类型" style="width: 311px; ">
            <el-option v-for="item in houseTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="货区类型">
          <el-select v-model="form.areaType" class="filter-item" multiple placeholder="请选择货区类型" style="width: 311px; ">
            <el-option v-for="item in areaTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createData()">
          确定
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive

import { getArea, addArea, deleteArea } from '@/api/shelve' // secondary package based on el-pagination

const areaTypeOptions = [
  { key: 0, display_name: '暂存区' },
  { key: 1, display_name: '普通存储区' },
  { key: 2, display_name: '处理品区' }
]

const houseTypeOptions = [
  { key: 1, display_name: '药品' },
  { key: 2, display_name: '医疗器械' },
  { key: 3, display_name: '医疗辅助物资' },
  { key: 4, display_name: '放射物资及危险品' }
]

const houseTypeKeyValue = houseTypeOptions.reduce((h, cur) => {
  h[cur.key] = cur.display_name
  return h
}, {})

const areaTypeKeyValue = areaTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  filters: {
    housetypeFilter(type) {
      return houseTypeKeyValue[type]
    },
    typeFilter(type) {
      return areaTypeKeyValue[type]
    }
  },
  data() {
    return {
      listQuery: {
        name: ''
      },
      form: {
        houseName: '',
        houseType: '',
        areaType: '',
        shelveNum: ''
      },
      houseTypeOptions,
      areaTypeOptions,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        create: '新增'
      },
      listQueryReturn: [],
      tableData: [],
      multiSelection: [],
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      getArea(this.listQuery).then(response => {
        console.log(this.listQuery)
        this.tableData = response.data.areaList
      }).catch(error => {
        console.log(error)
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.form = {
        id: '',
        houseName: '',
        houseType: '',
        areaType: '',
        shelveNum: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addArea(this.form).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDelete() {
      const delData = []
      this.multipleSelection.forEach(select => {
        console.log(select)
        delData.push(select)
      })
      console.log(delData)
      deleteArea(delData).then(response => {
        this.$notify({
          title: 'Success',
          message: 'Delete Successfully',
          type: 'success',
          duration: 2000
        })
        this.getList()
      }).catch(error => {
        console.error(error)
      })
    },
    // 每页条数改变时触发 选择一页显示多少行
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.currentPage = 1
      this.pageSize = val
    },
    // 当前页改变时触发 跳转其他页
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.currentPage = val
    }
  }
}
</script>
