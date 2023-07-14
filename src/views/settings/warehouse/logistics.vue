<template>
  <div class="app-container">
    <span>物流费用设定</span>
    <el-table
      :data="logisticsData"
      border
      fit
      highlight-current-row
      style="width: 60% ;margin-top:20px;margin-left:150px;"
    >
      <el-table-column type="index" align="center" width="55" />
      <el-table-column label="物流类型" width="285" align="center">
        <template v-slot="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位价格(单位:元)" width="285" align="center">
        <template v-slot="{row}">
          <span>{{ row.unitPrice }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-divider />
    <span>供应商距离</span>
    <div style="margin-left:150px;">
      <el-input v-model="listQuery.name" placeholder="供应商名" style="width: 200px;margin-right: 20px;margin-top: 20px" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
    </div>
    <el-table
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 60% ;margin-top:20px;margin-left:150px;"
    >
      <el-table-column type="index" align="center" width="50" />
      <el-table-column label="供应商名" width="285" align="center">
        <template v-slot="{row}">
          <span>{{ row.s.supplierName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="距离(单位:km)" width="285" align="center">
        <template v-slot="{row}">
          <span>{{ row.distance }}</span>
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
      style="width: 60% ;margin-top:20px;margin-left:150px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive

import { getDistance, getLogisticsExpense } from '@/api/logistics' // secondary package based on el-pagination

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  data() {
    return {
      listQuery: {
        name: ''
      },
      logisticsData: [],
      tableData: [],
      multiSelection: [],
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.getLogisticsList()
    this.getDistanceList()
  },
  methods: {
    getLogisticsList() {
      getLogisticsExpense().then(response => {
        console.log(this.listQuery)
        this.logisticsData = response.data.logisticsList
      }).catch(error => {
        console.log(error)
      })
    },
    getDistanceList() {
      getDistance(this.listQuery).then(response => {
        console.log(this.listQuery)
        this.tableData = response.data.distanceList
      }).catch(error => {
        console.log(error)
      })
    },
    handleFilter() {
      this.getDistanceList()
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
