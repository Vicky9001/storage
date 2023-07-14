<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-input v-model="listQuery.roleName" placeholder="角色名" style="width: 200px;margin-right: 20px" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
    </div>
    <el-table
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 95%;margin-top:20px;"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.roleId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色名" min-width="225" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色描述" width="500" align="center">
        <template slot-scope="{row}">
          <span>{{ row.roleDesc }}</span>
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

  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { searchRole, getRoles } from '@/api/user'

export default {
  name: 'ComplexTable',
  directives: { waves },
  data() {
    return {
      listQuery: {
        roleName: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
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
  methods: {
    getList() {
      getRoles().then(response => {
        this.tableData = response.data.roleList
        console.log(this.tableData)
      }).catch(error => {
        console.log(error)
      })
    },
    handleFilter() {
      searchRole(this.listQuery.roleName).then(response => {
        console.log(this.listQuery.roleName)
        this.tableData = response.data.roleList
      })
    },
    resetTemp() {
      this.temp = {
        roleId: '',
        roleName: '',
        roleDesc: ''
      }
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
