<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.type" placeholder="预警类型" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in ruleTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.goodsId" placeholder="物资名称" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in goodsOptions" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
      </el-select>
      <el-button v-waves class="filter-item" style="margin-left: 10px" type="primary" icon="el-icon-search" @click="handleFilter">
        查找
      </el-button>
      <el-button v-waves class="filter-item" style="margin-left: 10px" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建
      </el-button>
    </div>

    <div>
      <el-tag type="success" style="margin-left:32px;margin-top: 16px" effect="dark">过期预警：当物资距离过期时长小于过期阈值（天），或物资已过期时发出预警</el-tag>
      <el-tag type="success" style="margin-left:30px;margin-top: 16px" effect="dark">积压预警：当物资入库时长大于积压阈值（天）时发出预警</el-tag>
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
      <el-table-column label="预警类型" prop="type" width="120" align="center" fixed>
        <template slot-scope="{row}">
          <el-tag :type="getTagType(row.type)">{{ ruleTypeOptions.find(item => item.key === row.type).display_name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="物资名称" min-width="120" align="center" fixed>
        <template slot-scope="{row}">
          <span>{{ goodsOptions.find(item => item.goodsId === row.goodsId).goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则描述" width="300" align="center">
        <template slot-scope="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上限值" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.max }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下限值" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.min }}</span>
        </template>
      </el-table-column>
      <el-table-column label="过期阈值" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expiration }}</span>
        </template>
      </el-table-column>
      <el-table-column label="积压阈值" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.storeAge }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" prop="operation" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" @click="handleDelete(row)">
            删除
          </el-button>
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
    <!--    创建按钮弹窗-->
    <el-dialog title="新增预警规则" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="预警类型" prop="type" :rules="[{ required: true, message: '请选择预警类型', trigger: 'blur' }]">
          <el-select v-model="temp.type" class="filter-item" placeholder="请选择预警类型" style="width: 311px;">
            <el-option v-for="item in ruleTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="货物类型" prop="goodsId" :rules="[{ required: true, message: '请选择货物类型', trigger: 'blur' }]">
          <el-select v-model="temp.goodsId" class="filter-item" placeholder="请选择货物类型" style="width: 311px;">
            <el-option v-for="item in goodsOptions" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="temp.type === 1" label="上限值" prop="max" :rules="[{ validator: validateMax }]">
          <el-input v-model="temp.max" type="textarea" rows="1" placeholder="请输入上限值" />
        </el-form-item>
        <el-form-item v-if="temp.type === 1" label="下限值" prop="min" :rules="[{ validator: validateMin }]">
          <el-input v-model="temp.min" type="textarea" rows="1" placeholder="请输入下限值" />
        </el-form-item>
        <el-form-item v-if="temp.type === 2" label="过期阈值" prop="expiration">
          <el-input v-model="temp.expiration" type="textarea" rows="1" placeholder="请输入过期阈值" />
        </el-form-item>
        <el-form-item v-if="temp.type === 3" label="积压阈值" prop="storeAge">
          <el-input v-model="temp.storeAge" type="textarea" rows="1" placeholder="请输入积压阈值" />
        </el-form-item>
        <el-form-item label="规则描述" prop="remark">
          <el-input v-model="temp.desc" type="textarea" rows="2" placeholder="请输入规则描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="create()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { getGoods } from '@/api/outStore'
import { create, del, warning } from '@/api/warning'

const ruleTypeOptions = [
  { key: 1, display_name: '库存预警', tag_type: 'success' },
  { key: 2, display_name: '过期预警', tag_type: 'warning' },
  { key: 3, display_name: '积压预警', tag_type: 'danger' }
]

const ruleTypeKeyValue = ruleTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  filters: {
    ruleTypeFilter(type) {
      return ruleTypeKeyValue[type]
    }
  },
  data() {
    return {
      listQuery: {
        type: '',
        goodsId: ''
      },
      temp: {
        type: '',
        goodsId: '',
        creatorId: '',
        max: '',
        min: '',
        expiration: '',
        storeAge: '',
        desc: ''
      },
      ruleTypeOptions,
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
      warning(this.listQuery).then(response => {
        response.data.warning.forEach(item => {
          const data = {}
          data.id = item.id
          data.type = item.type
          data.goodsId = item.goodsId
          data.desc = item.desc
          data.max = item.max
          data.min = item.min
          data.expiration = item.expiration
          data.storeAge = item.storeAge
          data.realName = item.user.realName
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
    resetTemp() {
      this.temp = {
        type: '',
        goodsId: '',
        creatorId: '',
        max: '',
        min: '',
        expiration: '',
        storeAge: '',
        desc: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    create() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.creatorId = this.$store.getters.userId
          create(this.temp).then(() => {
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
    handleDelete(data) {
      del(data).then(response => {
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
    getTagType(type) {
      return ruleTypeOptions.find(item => item.key === type).tag_type
    },
    validateMax(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入上限值'))
      // } else if (value <= this.temp.min) {
      //   callback(new Error('上限值必须大于下限值'))
      } else {
        callback()
      }
    },
    validateMin(rule, value, callback) {
      if (value === '') {
        callback(new Error('请输入下限值'))
      // } else if (value >= this.temp.max) {
      //   callback(new Error('下限值必须小于上限值'))
      } else {
        callback()
      }
    }
  }
}
</script>

