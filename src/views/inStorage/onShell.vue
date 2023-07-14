<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.goodsId" placeholder="物资名称" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in goodsOptions" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
      </el-select>
      <el-select v-model="listQuery.supplier" placeholder="供应商" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in supplierOptions" :key="item.supplierId" :label="item.supplierName" :value="item.supplierId" />
      </el-select>
      <el-button v-waves class="filter-item" style="margin-left: 10px" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      :span-method="objectSpanMethod"
      border
      fit
      highlight-current-row
      style="width: 90% ;margin-top:20px;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="" type="index" prop="index" sortable="custom" align="center" width="50" />
      <el-table-column label="入库单ID" prop="orderId" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="批次" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.batchNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物资名称" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.supplier }}</span>
        </template>
      </el-table-column>
      <el-table-column label=" " prop="detail" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="text" @click="checkDetail(row)">
            查看详情
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" prop="operation" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="danger" size="small" @click="handleAbnormal(row)">
            驳回
          </el-button>
          <el-button type="success" size="small" @click="handelNormal(row)">
            验收合格
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
    <!--    详情弹窗-->
    <el-dialog :visible.sync="dialogVisible" width="70%">
      <el-descriptions title="详情" direction="vertical" :column="4" border style="width: 90% ">
        <el-descriptions-item label="入库单ID">
          {{ this.detail.id }}
        </el-descriptions-item>
        <el-descriptions-item label="发起人">
          {{ this.detail.creatorName }}
        </el-descriptions-item>
        <el-descriptions-item label="入库单描述">
          {{ this.detail.desc }}
        </el-descriptions-item>
        <el-descriptions-item label="金额 / 元">
          {{ this.detail.price }}
        </el-descriptions-item>
      </el-descriptions>

      <el-table
        ref="multipleTable"
        :data="detailGoods.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        border
        fit
        highlight-current-row
        style="width: 90% ;margin-top:20px;"
      >
        <el-table-column prop="batchNum" label="批次" width="180">
          <template slot-scope="{row}">
            {{ row.batchNum }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="物资名称" width="180">
          <template slot-scope="{row}">
            {{ row.goodsName }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsInfo" label="物资信息" width="180">
          <template slot-scope="{row}">
            {{ row.goodsInfo }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="物资名称" width="180">
          <template slot-scope="{row}">
            {{ row.goodsName }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="物资数量" width="180">
          <template slot-scope="{row}">
            {{ row.num }}
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="180">
          <template slot-scope="{row}">
            {{ row.unit }}
          </template>
        </el-table-column>
        <el-table-column prop="unitPrice" label="单价（元）" width="180">
          <template slot-scope="{row}">
            {{ row.unitPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总价（元）" width="180">
          <template slot-scope="{row}">
            {{ row.totalPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="unitWeight" label="单重（kg）" width="180">
          <template slot-scope="{row}">
            {{ row.unitWeight }}
          </template>
        </el-table-column>
        <el-table-column prop="totalWeight" label="总重（kg）" width="180">
          <template slot-scope="{row}">
            {{ row.totalWeight }}
          </template>
        </el-table-column>
        <el-table-column prop="manufactureDate" label="生产日期" width="180">
          <template slot-scope="{row}">
            {{ row.manufactureDate }}
          </template>
        </el-table-column>
        <el-table-column prop="expirationDate" label="过期日期" width="180">
          <template slot-scope="{row}">
            {{ row.expirationDate }}
          </template>
        </el-table-column>
        <el-table-column prop="supplier" label="供应商" width="180">
          <template slot-scope="{row}">
            {{ row.supplier }}
          </template>
        </el-table-column>
        <el-table-column prop="houseName" label="仓库" width="180">
          <template slot-scope="{row}">
            {{ row.houseName }}
          </template>
        </el-table-column>
      </el-table>
      <div class="table-title" style="margin-top: 25px;font-weight: bold">入库方案</div>
      <el-table
        ref="multipleTable"
        :data="solve.slice((currentPage-1)*pageSize,currentPage*pageSize)"
        border
        fit
        highlight-current-row
        style="width: 90% ;margin-top:10px;"
      >
        <el-table-column prop="operateTime" label="库位" width="240">
          <template slot-scope="{row}">
            {{ row.positionId }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="仓库名" width="240">
          <template slot-scope="{row}">
            {{ row.houseName }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="库位状态" width="240">
          <template slot-scope="{row}">
            {{ row.state | psFilter }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="存储货物" width="240">
          <template slot-scope="{row}">
            {{ row.goodsName }}
          </template>
        </el-table-column>
        <el-table-column prop="operateTime" label="存储数量" width="240">
          <template slot-scope="{row}">
            {{ row.goodsNum }}
          </template>
        </el-table-column>
      </el-table>
      <el-table
        ref="multipleTable"
        :data="detailRecords.slice((currentPage-1)*pageSize,currentPage*pageSize)"
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!--    操作按钮弹窗-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="操作" prop="operation">
          {{ this.dialogStatus }}
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="update()">
          确定
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { getWarehousingOrder, changeState } from '@/api/inStore'
// import { looseEqual } from 'element-ui'

const opOptions = [
  { key: 5, display_name: '创建入库单' },
  { key: 6, display_name: '删除入库单' },
  { key: 7, display_name: '验收确认' },
  { key: 8, display_name: '确认收货' },
  { key: 9, display_name: '拒绝入库审核' },
  { key: 10, display_name: '上架完成' }
]
const psOptions = [
  { key: 0, display_name: '未入库' },
  { key: 1, display_name: '预约入库' },
  { key: 2, display_name: '已入库' }
]
const opKeyValue = opOptions.reduce((ops, cur) => {
  ops[cur.key] = cur.display_name
  return ops
}, {})
const psKeyValue = psOptions.reduce((ops, cur) => {
  ops[cur.key] = cur.display_name
  return ops
}, {})
export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  filters: {
    opFilter(type) {
      return opKeyValue[type]
    },
    psFilter(type) {
      return psKeyValue[type]
    }
  },
  data() {
    return {
      listQuery: {
        state: 5,
        goodsId: '',
        supplier: ''
      },
      detail: {
        id: '',
        creatorName: '',
        price: '',
        desc: ''
      },
      form: {
        operation: '',
        remark: ''
      },
      opOptions,
      detailRecords: [],
      detailGoods: [],
      dialogVisible: false,
      dialogFormVisible: false,
      tableData: [],
      solve: [],
      goodsOptions: [],
      supplierOptions: [],
      dialogStatus: '',
      textMap: {
        no: '驳回',
        ok: '确认上架'
      },
      mergeObj: {},
      mergeArr: ['index', 'orderId', 'detail', 'operation'],
      changeList: {},
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.getList()
    this.getOpt()
  },
  methods: {
    getList: function() {
      this.listLoading = true
      this.tableData = []
      getWarehousingOrder(this.listQuery).then(response => {
        response.data.WarehousingOrder.forEach(item => {
          const orderId = item.id
          item.goodsBatches.forEach(item => {
            const data = {}
            data.batchNum = item.id
            data.supplier = item.supplier.supplierName
            data.goodsName = item.goods.goodsName
            data.goodsType = item.goods.goodsType
            data.orderId = orderId
            this.tableData.push(data)
          })
          this.getSpanArr()
        })
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    getOpt() {
      this.goodsOptions = []
      this.supplierOptions = []
      const searchList = { state: 5 }
      getWarehousingOrder(searchList).then(response => {
        response.data.WarehousingOrder.forEach(item => {
          item.goodsBatches.forEach(item => {
            const goodsOpt = {}
            const supplierOpt = {}
            let flag = true
            this.goodsOptions.forEach(i => {
              if (i.goodsId === item.goods.goodsId) {
                flag = false
              }
            })
            if (flag) {
              goodsOpt.goodsId = item.goods.goodsId
              goodsOpt.goodsName = item.goods.goodsName
              this.goodsOptions.push(goodsOpt)
            }
            flag = true
            this.supplierOptions.forEach(i => {
              if (i.supplierId === item.supplier.supplierId) {
                flag = false
              }
            })
            if (flag) {
              supplierOpt.supplierId = item.supplier.supplierId
              supplierOpt.supplierName = item.supplier.supplierName
              this.supplierOptions.push(supplierOpt)
            }
          })
        })
      })
    },
    handleFilter() {
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
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
    },
    checkDetail(row) {
      const searchList = { id: row.orderId }
      getWarehousingOrder(searchList).then(response => {
        this.solve = []
        response.data.WarehousingOrder.forEach(item => {
          this.detail.id = row.orderId
          this.detail.creatorName = item.creatorName
          this.detail.price = item.goodsPrice
          this.detail.desc = item.desc
          this.detailGoods = []
          item.goodsBatches.forEach(item => {
            const good = {}
            good.batchNum = item.id
            good.manufactureDate = item.manufactureDate
            good.expirationDate = item.expirationDate
            good.unitPrice = item.unitPrice
            good.num = item.num
            good.totalPrice = item.totalPrice
            good.supplier = item.supplier.supplierName
            good.unitWeight = item.unitWeight
            good.totalWeight = item.totalWeight
            good.goodsId = item.goods.goodsId
            good.goodsName = item.goods.goodsName
            good.goodsInfo = item.goods.goodsInfo
            // good.unit = '单位 / ' + item.goods.unit
            good.unit = item.goods.unit
            good.houseName = item.houseName
            this.detailGoods.push(good)
            item.positionList.forEach(u => {
              u.goodsName = item.goods.goodsName
              u.houseName = item.houseName
              this.solve.push(u)
            })
          })
          this.detailRecords = item.taskRecords
        })
      })
      this.dialogVisible = true
    },
    handleAbnormal(row) {
      this.dialogStatus = '驳回'
      this.dialogFormVisible = true
      this.changeList = {}
      this.changeList.orderId = row.orderId
      this.changeList.state = 4
      this.changeList.userId = this.$store.getters.userId
      this.changeList.remark = '取消上架'
    },
    update() {
      if (this.form.remark !== '') {
        this.changeList.remark = this.changeList.remark + '，' + this.form.remark
      }
      changeState(this.changeList).then(() => {
        this.getList()
        this.getOpt()
        this.dialogFormVisible = false
        this.$notify({
          title: 'Success',
          message: 'onShell Successfully',
          type: 'success',
          duration: 2000
        })
      })
    },
    handelNormal(row) {
      this.dialogStatus = '确认上架'
      this.dialogFormVisible = true
      this.changeList = {}
      this.changeList.orderId = row.orderId
      this.changeList.state = 6
      this.changeList.userId = this.$store.getters.userId
      this.changeList.remark = '确认上架'
    },
    getSpanArr() {
      let count = 0 // 用来记录需要合并行的起始位置
      this.mergeObj['index'] = []
      this.mergeObj['orderId'] = []
      this.mergeObj['detail'] = []
      this.mergeObj['operation'] = [] // 记录每一列的合并信息
      this.tableData.forEach((item, index) => {
        // index == 0表示数据为第一行，直接 push 一个 1
        if (index % this.pageSize === 0) {
          this.mergeObj['index'].push(1)
          this.mergeObj['orderId'].push(1)
          this.mergeObj['detail'].push(1)
          this.mergeObj['operation'].push(1)
          count = index
        } else {
          // 判断当前行是否与上一行其值相等 如果相等 在 count 记录的位置其值 +1 表示当前行需要合并 并push 一个 0 作为占位
          if (item['orderId'] === this.tableData[index - 1]['orderId']) {
            this.mergeObj['index'][count] += 1
            this.mergeObj['index'].push(0)
            this.mergeObj['orderId'][count] += 1
            this.mergeObj['orderId'].push(0)
            this.mergeObj['detail'][count] += 1
            this.mergeObj['detail'].push(0)
            this.mergeObj['operation'][count] += 1
            this.mergeObj['operation'].push(0)
          } else {
            // 如果当前行和上一行其值不相等
            count = index // 记录当前位置
            this.mergeObj['index'].push(1)
            this.mergeObj['orderId'].push(1)
            this.mergeObj['detail'].push(1)
            this.mergeObj['operation'].push(1) // 重新push 一个 1
          }
        }
      })
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      const index = (this.currentPage - 1) * this.pageSize + rowIndex
      // 判断列的属性
      if (this.mergeArr.indexOf(column.property) !== -1) {
        // 判断其值是不是为0
        if (this.mergeObj[column.property][index]) {
          return [this.mergeObj[column.property][index], 1]
        } else {
          // 如果为0则为需要合并的行
          return [0, 0]
        }
      }
    }
  }
}
</script>

