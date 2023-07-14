<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="state" placeholder="请选择出库单状态" style="margin-right:20px; width: 200px">
        <el-option v-for="item in stateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" style="margin-right:20px;width: 100px;" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 95%;margin-top:20px;"
    >
      <el-table-column label="出库单号" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="80" align="center">
        <template slot-scope="{row}">
          <span>{{ row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出库单描述" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预出库数" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="进度" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.state | stateFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button icon="el-icon-search" type="primary" size="mini" @click="scanDesc(row)">
            详情
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
    <el-dialog :title="textMap['desc']" :visible.sync="DescVisible" width="900px">
      <el-form ref="dataForm" :model="form" label-position="left" style="margin-left:70px;">
        <el-row>
          <el-col :span="10">
            <el-form-item label="创建时间 ">
              <el-input v-model="form.time" readonly style="margin-right:20px; width: 180px" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="备注">
              <el-input v-model="form.remark" readonly style="margin-right:20px; width: 180px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="出库单描述">
          <el-input v-model="form.desc" readonly style="margin-right:20px; width: 500px" />
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="出库单状态">
              <el-select v-model="form.state" :disabled="true" style="margin-right:20px; width: 180px">
                <el-option v-for="item in stateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="物流类型 ">
              <el-select v-model="form.logisticsType" :disabled="true" style="margin-right:20px; width: 180px">
                <el-option v-for="item in logisticsTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="商品价格 ">
              <el-input v-model="form.goodsPrice" readonly style="margin-left:14px;margin-right:10px; width: 180px" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="物流价格 ">
              <el-input v-model="form.logisticsPrice" readonly style="margin-right:20px; width: 180px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="总价  ">
              <el-input v-model="form.totalPrice" readonly style="margin-left:42px;width: 180px" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="出库总数 ">
              <el-input v-model="form.num" readonly style="margin-right:20px;width: 180px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-table
          :data="form.goods"
          border
          fit
          highlight-current-row
          style="width: 90% ;margin-top:20px;"
        >
          <el-table-column label="货物名称" width="120" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.goodsName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物信息" min-width="180" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.goodsInfo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="单位" width="50" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.unit }}</span>
            </template>
          </el-table-column>
          <el-table-column label="仓库库存" width="80" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.groundingNum }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物总重" width="90" align="center">
            <template slot-scope="{row}">
              <span>{{ row.totalWeight }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物总价" width="90" align="center">
            <template slot-scope="{row}">
              <span>{{ row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="出库数目" width="90" align="center">
            <template slot-scope="{row}">
              <span>{{ row.num }}</span>
            </template>
          </el-table-column>
        </el-table>

        <el-table
          :data="form.task"
          border
          fit
          highlight-current-row
          style="width: 90% ;margin-top:20px;"
        >
          <el-table-column prop="remark" label="操作描述" width="240">
            <template slot-scope="{row}">
              {{ row.taskType | opFilter }}
            </template>
          </el-table-column>
          <el-table-column prop="operateTime" label="操作日期" width="240">
            <template slot-scope="{row}">
              {{ row.operateTime }}
            </template>
          </el-table-column>
          <el-table-column prop="user" label="操作人">
            <template slot-scope="{row}">
              {{ row.user.realName }}
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import waves from '@/directive/waves' // waves directive
import { outOrder } from '@/api/outStore'
const stateTypeOptions = [
  { key: 0, display_name: ' ' },
  { key: 1, display_name: '待处理' },
  { key: 2, display_name: '拣货确认完成' },
  { key: 3, display_name: '复核完成' },
  { key: 4, display_name: '打包确认完成' },
  { key: 5, display_name: '出库完成' },
  { key: 6, display_name: '不予出库' }
]
const logisticsTypeOptions = [
  { key: 1, display_name: '海运' },
  { key: 2, display_name: '陆运' },
  { key: 3, display_name: '空运' }
]

const opOptions = [
  { key: 15, display_name: '创建出库单' },
  { key: 16, display_name: '拣货确认' },
  { key: 17, display_name: '复核确认' },
  { key: 18, display_name: '打包确认' },
  { key: 19, display_name: '出货确认' },
  { key: 20, display_name: '不予出库' }
]
const stateTypeKeyValue = stateTypeOptions.reduce((states, cur) => {
  states[cur.key] = cur.display_name
  return states
}, {})

const logisticsTypeKeyValue = logisticsTypeOptions.reduce((logistics, cur) => {
  logistics[cur.key] = cur.display_name
  return logistics
}, {})

const opKeyValue = opOptions.reduce((ops, cur) => {
  ops[cur.key] = cur.display_name
  return ops
}, {})

export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    stateFilter(type) {
      return stateTypeKeyValue[type]
    },
    logisticsFilter(type) {
      return logisticsTypeKeyValue[type]
    },
    opFilter(type) {
      return opKeyValue[type]
    }
  },
  data() {
    return {
      stateTypeOptions,
      logisticsTypeOptions,
      form: {
        id: '',
        creatorName: '',
        time: '',
        state: '',
        goodsPrice: '',
        supplierId: '',
        desc: '',
        remark: '',
        logisticsType: '',
        logisticsPrice: '',
        totalPrice: '',
        task: [],
        goods: []
      },
      state: null,
      opOptions,
      options: [],
      updateFormVisible: false,
      DescVisible: false,
      textMap: {
        update: '更新出库单',
        desc: '详情页面'
      },
      listQueryReturn: [],
      tableData: [],
      tableData1: [],
      multiSelection: [],
      nowDate: null,
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10, // 每页的数据条数
      tabClickIndex: null,
      tabClickLabel: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // tabClick row 当前行 column 当前列
    getList() {
      this.listLoading = true
      let type
      const role = this.$store.getters.roleNames
      const params = {}
      if (role !== ('超级管理员' | '库存管理员')) {
        params.id = this.$store.getters.userId
      }
      if (!(this.state == null | this.state === 0)) {
        params.type = this.state
      } else {
        params.type = '1,2,3,4,5,6'
        console.log(type)
      }
      outOrder(params).then(response => {
        this.tableData = response.data.outOrder
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.form = {
        id: '',
        time: '',
        state: '',
        goodsPrice: '',
        desc: '',
        remark: '',
        logisticsType: '',
        logisticsPrice: '',
        totalPrice: '',
        task: [],
        goods: []
      }
    },
    scanDesc(row) {
      this.form = Object.assign({}, row) // copy obj
      this.DescVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
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
