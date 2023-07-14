<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-input v-model="listQuery.name" placeholder="货品名称" style="margin-right:20px; width: 180px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
      <el-select v-model="logisticsType" placeholder="请选择物流类型" style="margin-left:20px; width: 200px">
        <el-option v-for="item in logisticsTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="departmentId" placeholder="请选择科室" style="margin-left:20px; width: 200px">
        <el-option v-for="item in departmentOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 95%;margin-top:20px;"
    >
      <el-table-column label="货物名称" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物信息" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unit }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库库存" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.groundingNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物类型" width="130" align="center">
        <template slot-scope="{row}">
          <el-tag>{{ row.goodsType | goodstypeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预出库数目" width="120" align="center">
        <template scope="scope">
          <el-input v-model="scope.row.num" size="small" placeholder="请输入数目" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button icon="el-icon-check" type="primary" size="mini" @click="handleApprove(row)">
            提交
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
      style="width: 95% ;margin-top:20px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <el-dialog :title="textMap['approve']" :visible.sync="approveFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="margin-left:50px;">
        <el-form-item label="出库单描述">
          <el-input v-model="form.desc" style="width:350px;" type="textarea" :rows="3" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData()">
          确认
        </el-button>
        <el-button type="info" @click="approveFormVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import waves from '@/directive/waves' // waves directive
import { createOrder } from '@/api/outStore'
import { getDepartment } from '@/api/related'
import { getGoods } from '@/api/goods'

const logisticsTypeOptions = [
  { key: 1, display_name: '海运' },
  { key: 2, display_name: '陆运' },
  { key: 3, display_name: '空运' }
]
const goodsTypeOptions = [
  { key: 1, display_name: '药品' },
  { key: 2, display_name: '医疗器械' },
  { key: 3, display_name: '医疗辅助物资' },
  { key: 4, display_name: '放射物资及危险品' }
]
const logisticsTypeKeyValue = logisticsTypeOptions.reduce((logistics, cur) => {
  logistics[cur.key] = cur.display_name
  return logistics
}, {})
const goodsTypeKeyValue = goodsTypeOptions.reduce((goods, cur) => {
  goods[cur.key] = cur.display_name
  return goods
}, {})
/* const departmentKeyValue = departmentOptions.reduce((department, cur) => {
  department[cur.key] = cur.display_name
  return department
}, {})*/

export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    logisticstypeFilter(type) {
      return logisticsTypeKeyValue[type]
    },
    goodstypeFilter(type) {
      return goodsTypeKeyValue[type]
    }
    /* departmentFilter(type) {
      return departmentKeyValue[type]
    }*/
  },
  data() {
    return {
      listQuery: {
        name: ''
      },
      departments: [],
      departmentOptions: [],
      department1: {
        key: null,
        display_name: ''
      },
      form: {
        desc: '',
        creatorId: '',
        goodsType: '',
        goodsId: '',
        num: ''
      },
      logisticsTypeOptions,
      goodsTypeOptions,
      departmentId: '',
      creatorId: '',
      goodsType: '',
      logisticsType: '',
      approveFormVisible: false,
      goodsId: '',
      num: '',
      options: [],
      textMap: {
        update: '编辑',
        create: '新增'
      },
      listQueryReturn: [],
      tableData: [],
      multiSelection: [],
      nowDate: null,
      nowtimer: '',
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.getList()
    this.getDepartmentList()
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
    getDepartmentList() {
      getDepartment().then(response => {
        this.departments = response.data.departmentList
        for (let i = 0; i < this.departments.length; i++) {
          const dept = this.departments[i]
          this.departmentOptions.push({
            key: dept.departmentId,
            display_name: dept.departmentName
          })
        }
        console.log(this.departmentOptions)
      }).catch(error => {
        console.log(error)
      })
    },
    handleFilter() {
      this.getList()
      this.getDepartmentList()
    },
    handleApprove(row) {
      this.form = Object.assign({}, row)
      this.approveFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      const create = {}
      create.creatorId = this.$store.getters.userId
      create.departmentId = this.departmentId
      create.desc = this.form.desc
      create.goodsId = this.form.goodsId
      create.logisticsType = this.logisticsType
      create.num = parseInt(this.form.num)
      console.log(create)
      createOrder(create).then(() => {
        this.approveFormVisible = false
        this.getList()
        this.$notify({
          title: 'Success',
          message: 'Created Successfully',
          type: 'success',
          duration: 2000
        })
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
