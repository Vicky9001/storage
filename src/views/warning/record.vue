<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.type" placeholder="记录类型" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in recordTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.goodsId" placeholder="物资名称" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in goodsOptions" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
      </el-select>
      <el-date-picker v-model="listQuery.date" type="date" placeholder="记录日期" class="filter-item" style="width: 165px;margin-left: 10px" />
      <el-button v-waves class="filter-item" style="margin-left: 10px" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 90% ;margin-top:20px;"
    >
      <el-table-column label="" type="index" prop="index" sortable="custom" align="center" width="50" :index="(currentPage-1)*pageSize+1" fixed />
      <el-table-column v-if="false" label="" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物资名称" min-width="130" align="center" fixed>
        <template slot-scope="{row}">
          <span>{{ goodsOptions.find(item => item.goodsId === row.goodsId).goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警记录类型" prop="type" min-width="130" align="center" fixed>
        <template slot-scope="{row}">
          <el-tag :type="getTagType(row.type)">{{ recordTypeOptions.find(item => item.key === row.type).display_name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="仓位编号" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.positionId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物资数量" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常数量" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.extraNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="异常时长" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.extraTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录日期" width="130" align="center">
        <template slot-scope="{row}">
          <span>{{ row.time }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      align="center"
      :current-page="currentPage"
      :page-sizes="[1,10,20,30]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.length"
      style="width: 90% ;margin-top:20px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { getGoods } from '@/api/outStore'
import { warningRecord } from '@/api/warning'

const recordTypeOptions = [
  { key: 1, display_name: '库存过剩', tag_type: 'success' },
  { key: 2, display_name: '库存不足', tag_type: 'success' },
  { key: 3, display_name: '即将过期', tag_type: 'warning' },
  { key: 4, display_name: '已过期', tag_type: 'warning' },
  { key: 5, display_name: '积压预警', tag_type: 'danger' }
]

const recordTypeKeyValue = recordTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  filters: {
    recordTypeFilter(type) {
      return recordTypeKeyValue[type]
    }
  },
  data() {
    return {
      listQuery: {
        state: 0,
        type: '',
        goodsId: '',
        time: ''
      },
      recordTypeOptions,
      tableData: [],
      goodsOptions: [],
      dialogFormVisible: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.getOpt()
    this.getList()
  },
  methods: {
    getList: function() {
      this.listLoading = true
      this.tableData = []
      warningRecord(this.listQuery).then(response => {
        response.data.warningRecord.forEach(item => {
          const data = {}
          data.id = item.id
          data.type = item.type
          data.goodsId = item.goodsId
          data.positionId = item.positionId
          data.num = item.num
          data.extraNum = item.extraNum
          data.extraTime = item.extraTime
          data.time = item.time
          this.tableData.push(data)
        })
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    getOpt() {
      getGoods().then(res => {
        res.data.goodsList.forEach(item => {
          const data = {}
          data.goodsName = item.goodsName
          data.goodsId = item.goodsId
          this.goodsOptions.push(data)
        })
      })
    },
    handleFilter() {
      this.getList()
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
    getTagType(type) {
      return recordTypeOptions.find(item => item.key === type).tag_type
    }
  }
}
</script>

