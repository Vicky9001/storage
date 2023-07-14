<template>
  <div class="app-container">
    <div>
      <el-select v-model="listQuery.id" placeholder="请选择货区" style="margin-right:20px; width: 200px">
        <el-option v-for="item in areas" :key="item.id" :label="item.area" :value="item.id" />
      </el-select>
      <el-select v-model="listQuery.state" placeholder="请选择状态" style="margin-right:20px; width: 200px">
        <el-option v-for="item in stateOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
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
      <el-table-column type="selection" align="center" width="45" />
      <el-table-column label="货区名" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.area.houseName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货区类型" width="130" align="center">
        <template slot-scope="{row}">
          <el-tag>{{ row.area.areaType | typeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="货架ID" width="90" align="center">
        <template slot-scope="{row}">
          <span>{{ row.shelveId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物类型" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goods | goodsFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货架状态" width="115" align="center">
        <template slot-scope="{row}">
          <el-tag>{{ row.state | stateFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="货物数量" width="110" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleTransfer(row)">
            内配
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
    <el-dialog :title="textMap['create']" :visible.sync="addFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="货区">
          <el-select v-model="form.areaId" placeholder="请选择货区" style="margin-right:20px; width: 250px">
            <el-option v-for="item in areas" :key="item.id" :label="item.area" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createData()">
          确定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="textMap['update']" :visible.sync="updateFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="货区名">
          <el-input v-model="form.houseName" readonly />
        </el-form-item>
        <el-form-item label="货区类型">
          <el-input readonly :value="form.areaType | typeFilter" />
        </el-form-item>
        <el-form-item label="货架ID">
          <el-input v-model="form.shelveId" readonly :value="parseInt(form.shelveId)" placeholder="请输入货物类型" />
        </el-form-item>
        <el-form-item label="货物类型">
          <el-select v-model="form.goodsType" placeholder="请选择货物类型" style="width: 300px">
            <el-option v-for="item in options" :key="item.id" :label="item.goodsName" :value="item.goodsId" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-input readonly :value="form.state | stateFilter" />
        </el-form-item>
        <el-form-item label="货物数量">
          <el-input v-model="form.goodsNum" readonly :value="parseInt(form.goodsNum)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { addPosition, delPosition, getArea, getPosition } from '@/api/shelve'
import { getGoods } from '@/api/goods' // secondary package based on el-pagination

const areaTypeOptions = [
  { key: 0, display_name: '暂存区' },
  { key: 1, display_name: '普通存储区' },
  { key: 2, display_name: '处理品区' }
]

const stateOptions = [
  { key: 0, display_name: '空闲' },
  { key: 1, display_name: '已占用' },
  { key: 2, display_name: '待入库' }
]

const areaTypeKeyValue = areaTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

const stateKeyValue = stateOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    typeFilter(type) {
      return areaTypeKeyValue[type]
    },
    goodsFilter(goods) {
      return goods == null ? goods : goods.goodsName
    },
    stateFilter(type) {
      return stateKeyValue[type]
    }
  },
  data() {
    return {
      listQuery: {
        id: '',
        state: ''
      },
      form: {
        positionId: '',
        areaId: '',
        houseName: '',
        areaType: '',
        shelveId: '',
        goodsType: '',
        goodsName: '',
        state: '',
        goodsNum: ''
      },
      options: [],
      areas: [],
      areaTypeOptions,
      stateOptions,
      addFormVisible: false,
      updateFormVisible: false,
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
    this.getAreas()
  },
  methods: {
    getOptions() {
      this.options = [{ goodsId: null, goodsName: ' ' }]
      getGoods().then(res => {
        res.data.goodsList.forEach(item => {
          const data = {}
          data.goodsId = item.goodsId
          data.goodsName = item.goodsName
          this.options.push(data)
        })
      })
      console.log(this.options)
    },
    getAreas() {
      this.areas = [{ id: null, area: ' ' }]
      getArea().then(res => {
        res.data.areaList.forEach(item => {
          const data = {}
          data.id = item.id
          data.area = item.houseName + '-' + areaTypeKeyValue[item.areaType]
          this.areas.push(data)
        })
      })
      console.log(this.areas)
    },
    getList() {
      this.listLoading = true
      getPosition(this.listQuery).then(response => {
        console.log(this.listQuery)
        this.tableData = response.data.positionList
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.getList()
      this.getAreas()
    },
    handleTransfer(row) {
      this.$router.push({
        name: 'createTransfer',
        query: {
          row: row
        }}
      )
    },
    resetTemp() {
      this.form = {
        positionId: '',
        areaId: '',
        shelveId: '',
        goodsType: '',
        state: '',
        goodsNum: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.addFormVisible = true
      this.getAreas()
      this.getOptions()
      this.areas.shift()
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      const add = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          add.areaId = parseInt(this.form.areaId)
          add.goodsType = parseInt(this.form.goodsType)
          addPosition(add).then(() => {
            this.addFormVisible = false
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
        const del = {}
        del.areaId = select.areaId
        del.shelveId = select.shelveId
        delData.push(del)
      })
      console.log(delData)
      delPosition(delData).then(response => {
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
