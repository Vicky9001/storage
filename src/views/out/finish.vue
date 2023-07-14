<template>
  <div class="app-container">
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
      <el-table-column label="出库单描述" width="250" align="center">
        <template slot-scope="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预出库数" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="进度" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.state | stateFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button icon="el-icon-search" size="mini" @click="scanDesc(row)">
            详情
          </el-button>
          <el-button icon="el-icon-check" type="primary" size="mini" @click="handleApprove(row)">
            出库完成
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
        <el-form-item label="审批意见">
          <el-input v-model="form.remark" style="width:350px;" type="textarea" :rows="3" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleFinish">
          确认
        </el-button>
        <el-button type="danger" @click="handleReject">
          取消出库
        </el-button>
        <el-button type="info" @click="approveFormVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="textMap['desc']" :visible.sync="DescVisible" width="900px">
      <el-form ref="dataForm" :model="form" label-position="left" style="margin-left:70px;">
        <el-row>
          <el-col :span="10">
            <el-form-item label="创建时间 ">
              <el-input v-model="form.time" readonly style="margin-right:20px; width: 180px" />
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
import { outOrder, changeState } from '@/api/outStore'
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
        logisticsType: '',
        logisticsPrice: '',
        totalPrice: '',
        task: [],
        goods: []
      },
      opOptions,
      options: [],
      approveFormVisible: false,
      DescVisible: false,
      textMap: {
        desc: '出库单详情页',
        approve: '审批出库单'
      },
      listQueryReturn: [],
      tableData: [],
      tableData1: [],
      multiSelection: [],
      nowDate: null,
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
      this.listLoading = true
      outOrder({ 'type': 4 }).then(response => {
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
        creatorName: '',
        time: '',
        state: '',
        goodsPrice: '',
        supplierId: '',
        desc: '',
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
    handleApprove(row) {
      this.form = Object.assign({}, row)
      this.approveFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleFinish() {
      const finish = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          finish.state = 19
          finish.id = parseInt(this.form.id)
          finish.remark = this.form.remark
          finish.operator = parseInt(this.$store.getters.userId)
          changeState(finish).then(() => {
            this.approveFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Finish Successfully',
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
    handleReject() {
      const reject = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          reject.state = 20
          reject.id = parseInt(this.form.id)
          reject.remark = this.form.remark
          reject.operator = parseInt(this.$store.getters.userId)
          changeState(reject).then(() => {
            this.approveFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Reject Successfully',
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
