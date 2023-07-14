<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-input v-model="listQuery.userName" placeholder="用户名" style="width: 160px;margin-right: 10px" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.realName" placeholder="真实姓名" style="width: 160px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.roles" placeholder="角色" clearable class="filter-item" style="width: 160px;margin-left: 10px">
        <el-option v-for="item in options" :key="item.roleId" :label="item.roleName" :value="item.roleId" />
      </el-select>
      <el-button v-waves class="filter-item" style="margin-left: 10px" type="primary" icon="el-icon-search" @click="handleFilter">
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
      <el-table-column type="selection" align="center" width="55" />
      <el-table-column label="账号名称" min-width="160" align="center">
        <template slot-scope="{row}">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="真实姓名" width="120" align="center">
        <template slot-scope="{row}">
          <span>{{ row.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色" width="320" align="center">
        <template slot-scope="{row}">
          <span v-if="row.roles !== ''">
            <el-tag
              v-for="tag in row.roles"
              :key="tag.roleId"
              :disable-transitions="false"
              :style="{ margin: '10px' }"
              class="tag"
            >{{ tag.roleName }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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

    <!--    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />-->

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="loginRules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item v-if="dialogStatus === '编辑'" label="id" prop="userId">
          <el-input v-model="temp.userId" :disabled="true" />
        </el-form-item>
        <el-form-item label="账号名称" prop="userName">
          <el-input v-model="temp.userName" placeholder="请输入账号名称" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="temp.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="temp.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="temp.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item v-if="dialogStatus === '编辑'" label="角色" prop="roles">
          <el-select v-model="roleIds" class="filter-item" placeholder="请选择角色" multiple style="width: 311px;">
            <el-option v-for="item in options" :key="item.roleId" :label="item.roleName" :value="item.roleId" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='新增'?createData():updateData()">
          确定
        </el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { addUser, deleteUser, getUserList, updateRoles, updateUserInfo } from '@/api/user'
import { getRoles } from '@/api/user'

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      listLoading: true,
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
      },
      listQuery: {
        userName: '',
        realName: '',
        roles: ''
      },
      temp: {
        userId: '',
        userName: '',
        realName: '',
        password: '',
        phone: '',
        roles: ''
      },
      roleIds: '',
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      tableData: [],
      multiSelection: [],
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10, // 每页的数据条数
      options: []
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    getOptions() {
      getRoles().then(res => {
        res.data.roleList.forEach(item => {
          const data = {}
          data.roleName = item.roleName
          data.roleId = item.roleId
          this.options.push(data)
        })
      })
      console.log(this.options)
    },
    getList() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
        console.log(this.listQuery)
        this.tableData = response.data.userlist
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.temp = {
        userId: '',
        userName: '',
        realName: '',
        password: '',
        phone: '',
        roles: ''
      }
      this.roleIds = ''
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          addUser({
            userName: this.temp.userName,
            realName: this.temp.realName,
            password: this.temp.password,
            phone: this.temp.phone
          }).then(() => {
            this.getList()
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.roleIds = this.temp.roles.map(role => role.roleId)
      this.dialogStatus = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateUserInfo(this.temp).then(() => {
            console.log({ userId: this.temp.userId, roleIds: this.roleIds })
            updateRoles({ userId: this.temp.userId, roleIds: this.roleIds }).then(() => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify({
                title: 'Success',
                message: 'Update Successfully',
                type: 'success',
                duration: 2000
              })
            })
          })
        }
      })
    },
    handleDelete() {
      const delData = []
      this.multipleSelection.forEach(select => {
        const del = {}
        del.userId = select.userId
        delData.push(del)
      })
      deleteUser(delData).then(response => {
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
