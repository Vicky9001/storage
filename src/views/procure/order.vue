<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="supplierId" placeholder="请选择供货商" style="margin-right:20px; width: 200px">
        <el-option v-for="item in suppls" :key="item.supplierId" :label="item.supplName" :value="item.supplierId" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" style="margin-right:20px;width: 100px;" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
      <el-select ref="mySelect" v-model="logisticsType" placeholder="请选择物流类型" style="margin-right:20px; width: 200px">
        <el-option v-for="item in logisticsTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-button @click="handleApprove()">提交采购单</el-button>
      <el-button @click="toggleSelection()">取消选择</el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 95%;margin-top:20px;"
      :row-key="getRowKeys"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" :reserve-selection="true" width="55" />
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
      <el-table-column label="单重" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unitWeight }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单价" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unitPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预购数目" width="200" align="center">
        <template scope="scope">
          <el-input v-model="scope.row.num" size="small" placeholder="请输入数目" />
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
        <el-form-item label="采购描述">
          <el-input v-model="form.desc" style="width:350px;" type="textarea" :rows="3" placeholder="请输入采购单描述" />
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
import { getGoods, createOrder } from '@/api/purchase'
import { getSupplier } from '@/api/related'

const logisticsTypeOptions = [
  { key: 1, display_name: '海运' },
  { key: 2, display_name: '陆运' },
  { key: 3, display_name: '空运' }
]

const logisticsTypeKeyValue = logisticsTypeOptions.reduce((logistics, cur) => {
  logistics[cur.key] = cur.display_name
  return logistics
}, {})

export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    typeFilter(type) {
      return logisticsTypeKeyValue[type]
    }
  },
  data() {
    return {
      logisticsTypeOptions,
      creatorId: '',
      supplierId: '',
      desc: 'test',
      logisticsType: '',
      goodsId: '',
      num: '',
      form: {
        desc: ''
      },
      options: [],
      suppls: [],
      approveFormVisible: false,
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
    this.getSuppliers()
    this.nowtimer = setInterval(this.gettime, 1000)
  },
  methods: {
    getRowKeys(row) {
      return row.goodsId
    },
    getSuppliers() {
      console.log(this.$store.getters.userId)
      this.suppls = []
      getSupplier({ 'supplierId': this.supplierId }).then(res => {
        res.data.supplierList.forEach(item => {
          const data = {}
          data.supplierId = item.supplierId
          data.supplName = item.supplierName
          this.suppls.push(data)
        })
      })
      console.log(this.suppls)
    },
    getList() {
      this.listLoading = true
      getGoods({ 'supplierId': this.supplierId }).then(response => {
        this.tableData = response.data.supplierList
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.getSuppliers()
      this.getList()
    },
    gettime() {
      this.nowDate = new Date().toLocaleString()
    },
    handleApprove() {
      if (this.$refs.mySelect && this.$refs.mySelect.value) {
        this.approveFormVisible = true
      } else {
        // 无选中内容，不允许提交
        this.$message.error('请选择物流类型')
      }
    },
    createData() {
      const create = {}
      const goods = []
      this.multipleSelection.forEach(select => {
        console.log('sss')
        console.log(select.goodsId)
        const g = {}
        g.goodsId = select.goodsId
        g.num = parseInt(select.num)
        g.manufactureDate = this.manufactureDate
        g.expirationDate = this.expirationDate
        goods.push(g)
      })
      create.creatorId = this.$store.getters.userId
      create.supplierId = this.supplierId
      create.desc = this.form.desc
      create.logisticsType = this.logisticsType
      create.goods = goods
      console.log(create)
      /*      const add = {}
      add.creatorId = this.$store.getters.userId*/
      createOrder(create).then(() => {
        this.approveFormVisible = false
        this.$notify({
          title: 'Success',
          message: 'Created Successfully',
          type: 'success',
          duration: 2000
        })
        this.$router.push({
          name: 'scanPurchaseOrder'
        })
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
