<template>
  <div class="app-container">
    <div>
      <el-input v-model="listQuery.name" placeholder="货品名称" style="margin-right:20px; width: 180px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      style="width: 90% ;margin-top:20px;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column label="货物类型" width="220" align="center">
        <template slot-scope="{row}">
          <el-tag>{{ row.goodsType | typeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="货物名" min-width="220" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物信息" width="220" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库库存" width="160" align="center">
        <template slot-scope="{row}">
          <span>{{ row.groundingNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位" width="160" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unit }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
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
      style="width: 90% ;margin-top:20px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="货物类型" prop="goodsType">
          <el-select v-model="form.goodsType" class="filter-item" placeholder="请选择货物类型" style="width: 300px; ">
            <el-option v-for="item in goodsTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="货物名" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="请输入货物名" />
        </el-form-item>
        <el-form-item label="货物信息" prop="goodsInfo">
          <el-input v-model="form.goodsInfo" placeholder="请输入货物信息" />
        </el-form-item>
        <el-form-item label="仓库库存" prop="groundingNum">
          <el-input v-model="form.groundingNum" placeholder="0" readonly="readonly" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入货物单位" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='新增'?createData():updateData()">
          确定
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive

import { getGoods, addGoods, modGoods, delGoods } from '@/api/goods'

const goodsTypeOptions = [
  { key: 1, display_name: '药品' },
  { key: 2, display_name: '医疗器械' },
  { key: 3, display_name: '医疗辅助物资' },
  { key: 4, display_name: '放射物资及危险品' }
]
const goodsTypeKeyValue = goodsTypeOptions.reduce((goods, cur) => {
  goods[cur.key] = cur.display_name
  return goods
}, {})
export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  filters: {
    typeFilter(type) {
      return goodsTypeKeyValue[type]
    }
  },
  data() {
    return {

      listQuery: {
        name: ''
      },
      goodsTypeOptions,
      form: {
        goodsType: '',
        goodsName: '',
        goodsInfo: '',
        unit: '',
        groundingNum: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
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
  selectPlatform() {
    this.$forceUpdate()
  },
  methods: {

    getList() {
      getGoods(this.listQuery).then(response => {
        console.log(this.listQuery)
        this.tableData = response.data.goodsList
      }).catch(error => {
        console.log(error)
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.form = {
        goodsType: '',
        goodsName: '',
        goodsInfo: '',
        unit: '',
        groundingNum: ''
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
      const add = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          add.goodsType = parseInt(this.form.goodsType)
          add.goodsName = this.form.goodsName
          add.goodsInfo = this.form.goodsInfo
          add.unit = this.form.unit
          add.groundingNum = parseInt(this.form.groundingNum)
          addGoods(add).then(() => {
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
    handleUpdate(row) {
      this.form = Object.assign({}, row) // copy obj
      this.form.goodsType = parseInt(this.form.goodsType)
      this.form.groundingNum = parseInt(this.form.groundingNum)
      this.dialogStatus = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.form)
          modGoods(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
          setTimeout(() => {
            this.getList()
          }, 500)
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
      delGoods(delData).then(response => {
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
