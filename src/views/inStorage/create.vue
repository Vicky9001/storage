<template>
  <!--产品搜索-->
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.supplierId" placeholder="请选择供货商" style="margin-right:20px; width: 200px">
        <el-option v-for="item in suppliers" :key="item.supplierId" :label="item.supplierName" :value="item.supplierId" />
      </el-select>
      <el-select v-model="listQuery.goodsId" placeholder="请选择货物类型" style="margin-right:20px; width: 200px">
        <el-option v-for="item in goods" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button @click="handleCreate">创建入库单</el-button>
      <el-button @click="toggleSelection()">取消选择</el-button>
    </div>
    <!--产品列表-->
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
      <el-table-column label="货物类型" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goods.goodsType | goodsTypeFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物名" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goods.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供应商" min-width="120" align="center">
        <template slot-scope="{row}">
          <span>{{ row.supplier.supplierName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生产日期" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.manufactureDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效期" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expirationDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标仓库" width="200" align="center">
        <template scope="{row}">
          <el-select v-model="row.areaId" placeholder="请选择仓库" style="margin-right:20px; width: 150px" @click.native="getAreas(row)">
            <el-option v-for="item in areas" :key="item.id" :label="item.area" :value="item.id" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作数目" width="150" align="center">
        <template scope="{row}">
          <el-input v-model="row.delnum" size="small" placeholder="请输入数目" />
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
    <el-dialog :title="title" :visible.sync="formVisible">
      <el-form ref="dataForm" label-position="left" label-width="100px" style="margin-left:50px;">
        <el-form-item label="意见">
          <el-input v-model="desc" style="width:350px;" type="textarea" :rows="3" placeholder="" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData">
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
import { createOrder, getArea } from '@/api/inStore'
import { getGoods } from '@/api/goods'
import { getSupplier } from '@/api/related'
import { getWaitingList } from '@/api/inStore'

const goodsTypeOptions = [
  { key: 1, display_name: '药品' },
  { key: 2, display_name: '医疗器械' },
  { key: 3, display_name: '辅助医疗用品' },
  { key: 4, display_name: '放射物资及危险品' }
]
const areaTypeOptions = [
  { key: 0, display_name: '暂存区' },
  { key: 1, display_name: '普通存储区' },
  { key: 2, display_name: '处理品区' }
]

const areaTypeKeyValue = areaTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})
export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    goodsTypeFilter(type) {
      return goodsTypeOptions[--type].display_name
    }
  },
  data() {
    return {
      listQuery: {
        supplierId: '',
        goodsId: ''
      },
      areas: [],
      goods: [],
      suppliers: [],
      goodsTypeOptions,
      areaTypeOptions,
      title: '创建入库单',
      formVisible: false,
      desc: '',
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
    this.getGoods()
    this.getList()
    this.nowtimer = setInterval(this.gettime, 1000)
  },
  methods: {
    getRowKeys(row) {
      return row.id
    },
    getSuppliers() {
      console.log(this.$store.getters.userId)
      this.suppliers = []
      getSupplier().then(res => {
        this.suppliers = res.data.supplierList
      })
    },
    getGoods() {
      console.log(this.$store.getters.userId)
      this.goods = []
      getGoods().then(res => {
        this.goods = res.data.goodsList
      })
    },
    getAreas(row) {
      const type = row.goods.goodsType
      this.areas = []
      getArea({ goodsType: type }).then(res => {
        res.data.area.forEach(item => {
          const data = {}
          data.id = item.id
          data.area = item.houseName + '-' + areaTypeKeyValue[item.areaType]
          this.areas.push(data)
        })
      })
    },
    getList() {
      this.listLoading = true
      getWaitingList(this.listQuery).then(response => {
        this.tableData = response.data.goodsBatches
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.getList()
    },
    gettime() {
      this.nowDate = new Date().toLocaleString()
    },
    handleCreate(row) {
      this.formVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      const create = {}
      const goods = []
      this.multipleSelection.forEach(select => {
        const g = {}
        g.id = select.id
        g.delnum = parseInt(select.delnum)
        g.areaId = select.areaId
        goods.push(g)
      })
      create.creatorId = this.$store.getters.userId
      create.desc = this.desc
      create.goodsBatches = goods
      console.log(create)
      createOrder(create).then(() => {
        this.$notify({
          title: 'Success',
          message: 'Created Successfully',
          type: 'success',
          duration: 2000
        })
        this.$router.push({
          name: 'scanInOrder'
        })
        this.formVisible = false
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
    },
    async projectList() {
      const res = await this.$api.projectList({})
      console.log('产品列表数据', res.data)
    },
    created() {
      this.projectList()
    }
  }
}
</script>

<style>
.header{
  background: #fff;
  margin-bottom: 20px;
  padding: 10px;
}
.content{
  background: #fff;
}

</style>
