<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.type" placeholder="盘点任务类型" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in typeOptions" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
      </el-select>
      <el-select v-model="listQuery.creatorId" placeholder="创建人" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in creatorOptions" :key="item.creatorId" :label="item.creatorName" :value="item.creatorId" />
      </el-select>
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
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="" type="index" prop="index" sortable="custom" align="center" width="50" :index="(currentPage-1)*pageSize+1" fixed />
      <el-table-column v-if="false" label="ID" min-width="50" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.creatorName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务描述" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="false" label="pId" width="50" align="center">
        <template slot-scope="{row}">
          <span>{{ row.positionId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物资名称" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录数量" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.recordNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单价" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unitPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="进度" width="180" align="center">
        <template slot-scope="{row}">
          <span>
            <el-tag :type="getTag(row.state)">
              {{ row.state }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="实际数量" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.realNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="差异数量" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.diffNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="差异金额" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.diffPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" prop="operation" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="danger" size="small" :disabled="judge(row)" @click="handleAbnormal(row)">
            驳回
          </el-button>
          <el-button type="success" size="small" :disabled="judge(row)" @click="handelNormal(row)">
            审核通过
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
    <!--    操作按钮弹窗-->
    <div>
      <my-dialog :title="dialogTitle" :form-list="formList" :dialog-visible.sync="dialogFormVisible" :form="form" :update-method="update" />
    </div>
    <!--    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">-->
    <!--      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">-->
    <!--        <el-form-item label="操作" prop="operation">-->
    <!--          &lt;!&ndash;          <span>&ndash;&gt;-->
    <!--          &lt;!&ndash;            <el-tag&ndash;&gt;-->
    <!--          &lt;!&ndash;              :disable-transitions="false"&ndash;&gt;-->
    <!--          &lt;!&ndash;              :style="{ margin: '10px' }"&ndash;&gt;-->
    <!--          &lt;!&ndash;            >&ndash;&gt;-->
    <!--          {{ dialogStatus }}-->
    <!--          &lt;!&ndash;              </el-tag>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </span>&ndash;&gt;-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="pId" prop="operation">-->
    <!--          {{ formList.pId }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="物资名称" prop="operation">-->
    <!--          {{ formList.goodsName }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="记录数量" prop="operation">-->
    <!--          {{ formList.recordNum }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="单价" prop="operation">-->
    <!--          {{ formList.unitPrice }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="实际数量" prop="operation">-->
    <!--          {{ formList.realNum }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="备注" prop="remark">-->
    <!--          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="dialogFormVisible = false">-->
    <!--          取消-->
    <!--        </el-button>-->
    <!--        <el-button type="primary" @click="update()">-->
    <!--          确定-->
    <!--        </el-button>-->
    <!--      </div>-->

    <!--    </el-dialog>-->
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { changeState, checkOrder } from '@/api/check'
import MyDialog from '../../components/Dialog/index'
export default {
  components: {
    MyDialog
  },
  directives: { waves },
  data() {
    return {
      listQuery: {
        type: '',
        creatorId: ''
      },
      tableData: [],
      typeOptions: [{ typeId: 1, typeName: '数据未录入' },
        { typeId: 2, typeName: '审核中' },
        { typeId: 3, typeName: '审核通过' },
        { typeId: 4, typeName: '审核未通过' }],
      creatorOptions: [],
      mergeObj: {},
      updateRequest: {},
      changeReq: {},
      mergeArr: ['areaId'],
      form: {
        remark: ''
      },
      formList: [
        { label: 'pId', prop: 'operation', type: 'default', value: '' },
        { label: '物资名称', prop: 'goodsName', type: 'default', value: '' },
        { label: '记录数量', prop: 'recordNum', type: 'default', value: '' },
        { label: '单价', prop: 'unitPrice', type: 'default', value: '' },
        { label: '实际数量', prop: 'realNum', type: 'default', value: '' },
        { label: '备注', prop: 'remark', type: 'textarea', rows: 2, placeholder: '请输入备注' }
      ],
      dialogFormVisible: false,
      dialogStatus: '',
      dialogTitle: '',
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
      const req = {}
      if (!this.listQuery.type) {
        req.type = '1, 2, 3, 4'
        req.id = ''
      } else {
        req.type = this.listQuery.type
        req.id = this.listQuery.creatorId
      }
      checkOrder(req).then(response => {
        if (response.code === 200) {
          response.data.checkOrder.forEach(item => {
            const data = {}
            data.id = item.id
            data.creatorName = item.creatorName
            data.time = item.time
            if (item.desc !== '') {
              data.desc = item.desc
            } else {
              data.desc = '-'
            }
            data.positionId = item.positionId
            data.goodsName = item.goodsName
            data.recordNum = item.recordNum + item.unit
            data.unitPrice = item.unitPrice + '元'
            if (item.state === 1) {
              data.state = '数据未录入'
            } else if (item.state === 2) {
              data.state = '审核中'
            } else if (item.state === 3) {
              data.state = '审核通过'
            } else if (item.state === 4) {
              data.state = '审核未通过'
            }
            if (item.realNum) {
              data.realNum = item.realNum + item.unit
              data.diffNum = item.diffNum + item.unit
              data.diffPrice = item.diffPrice + '元'
            } else {
              data.realNum = '-'
              data.diffNum = '-'
              data.diffPrice = '-'
            }
            this.tableData.push(data)
          })
        }
      })
      // Just to simulate the time of the request
      setTimeout(() => {
        this.listLoading = false
      }, 500)
    },
    getOpt() {
      this.creatorOptions = []
      const req = {}
      req.type = '1, 2, 3, 4'
      req.id = ''
      checkOrder(req).then(response => {
        if (response.code === 200) {
          response.data.checkOrder.forEach(item => {
            const creatorOpt = {}
            let flag = true
            this.creatorOptions.forEach(i => {
              if (i.creatorId === item.creatorId) {
                flag = false
              }
            })
            if (flag) {
              creatorOpt.creatorId = item.creatorId
              creatorOpt.creatorName = item.creatorName
              this.creatorOptions.push(creatorOpt)
            }
          })
        }
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
    handelNormal(row) {
      this.form = {
        remark: ''
      }
      this.dialogTitle = '审核通过'
      this.dialogFormVisible = true
      this.changeReq = {}
      this.changeReq.orderId = row.id
      this.changeReq.state = 3
      this.changeReq.userId = this.$store.getters.userId
      this.formList[0].value = row.positionId
      this.formList[1].value = row.goodsName
      this.formList[2].value = row.recordNum
      this.formList[3].value = row.unitPrice
      this.formList[4].value = row.realNum
    },
    handleAbnormal(row) {
      this.form = {
        remark: ''
      }
      this.dialogTitle = '驳回'
      this.dialogFormVisible = true
      this.changeReq = {}
      this.changeReq.orderId = row.id
      this.changeReq.state = 4
      this.changeReq.userId = this.$store.getters.userId
      this.formList[0].value = row.positionId
      this.formList[1].value = row.goodsName
      this.formList[2].value = row.recordNum
      this.formList[3].value = row.unitPrice
      this.formList[4].value = row.realNum
    },
    update() {
      console.log(this.changeReq)
      changeState(this.changeReq).then(resp => {
        if (resp.code === 200) {
          this.$message({
            showClose: true,
            type: 'success',
            message: '操作成功!'
          })
        } else {
          this.$message({
            showClose: true,
            type: 'error',
            message: '操作失败!'
          })
        }
        this.dialogFormVisible = false
        this.getList()
      })
    },
    judge(row) {
      // 启用按钮
      if (row.state === '审核中') {
        return false
      } else {
        return true
      }
    },
    getTag(state) {
      if (state === '审核通过') {
        return 'success'
      } else if (state === '审核未通过') {
        return 'danger'
      } else if (state === '数据未录入') {
        return 'info'
      } else if (state === '审核中') {
        return 'warning'
      }
    }
  }
}
</script>
