<template>
  <div class="app-container">
    <div style="margin: 10px 0">
      <el-select v-model="listQuery.areaType" placeholder="货区存放类型" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in areaOptions" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
      </el-select>
      <el-select v-model="listQuery.goodsId" placeholder="物资名称" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option v-for="item in goodsOptions" :key="item.goodsId" :label="item.goodsName" :value="item.goodsId" />
      </el-select>
      <el-input v-model="listQuery.batchInfo" placeholder="batchInfo" clearable class="filter-item" style="width: 130px;margin-left: 10px" />
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
      <el-table-column label="" type="index" prop="index" sortable="custom" align="center" width="50" :index="(currentPage-1)*pageSize+1" fixed />
      <el-table-column v-if="false" label="" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.positionId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓库ID" prop="areaId" min-width="100" align="center" fixed>
        <template slot-scope="{row}">
          <span>{{ row.areaId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货架ID" min-width="100" align="center" fixed>
        <template slot-scope="{row}">
          <span>{{ row.shelveId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="仓储类型" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.goodsType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货区类型" width="120" align="center">
        <template slot-scope="{row}">
          <span>
            <el-tag :type="getTag(row.state)">
              {{ row.state }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="最大储量" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.capacity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="批次" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.batchInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物资名称" width="180" align="center">
        <template slot-scope="{row}">
          <span>{{ row.good }}</span>
        </template>
      </el-table-column>
      <el-table-column label="储量" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" prop="operation" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" :disabled="judge(row)" @click="handleCreate(row)">
            盘点
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
      <my-dialog :title="dialogTitle" :form-list="formList" :dialog-visible.sync="dialogFormVisible" :form="form" :update-method="create" />
    </div>
    <!--    <el-dialog title="创建盘点任务" :visible.sync="dialogFormVisible">-->
    <!--      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">-->
    <!--        <el-form-item label="仓库ID" prop="operation">-->
    <!--          {{ positionList.areaId }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="货架ID" prop="operation">-->
    <!--          {{ positionList.shelveId }}-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="任务描述" prop="remark">-->
    <!--          <el-input v-model="form.desc" type="textarea" :rows="2" placeholder="请输入任务描述" />-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="dialogFormVisible = false">-->
    <!--          取消-->
    <!--        </el-button>-->
    <!--        <el-button type="primary" @click="create()">-->
    <!--          确定-->
    <!--        </el-button>-->
    <!--      </div>-->

    <!--    </el-dialog>-->
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { searchPositions, createOrder, changeState, checkOrder } from '@/api/check'
import MyDialog from '../../components/Dialog/index'

export default {
  components: {
    MyDialog
  },
  // components: { Pagination },
  directives: { waves },
  data() {
    return {
      listQuery: {
        areaId: '',
        areaType: '',
        goodsId: '',
        batchInfo: '',
        state: ''
      },
      tableData: [],
      goodsOptions: [],
      areaOptions: [],
      mergeObj: {},
      createRequest: {},
      formList: [
        { label: '仓库ID', prop: 'id1', type: 'default' },
        { label: '货架ID', prop: 'id2', type: 'default' },
        { label: '任务描述', prop: 'remark', type: 'textarea', rows: 2, placeholder: '请输入任务描述' }
      ],
      changeReq: {},
      dialogTitle: '创建盘点任务',
      mergeArr: ['areaId'],
      form: {
        desc: ''
      },
      dialogFormVisible: false,
      judgeArr: [],
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
      const params = {}
      params.type = '1,2,3,4'
      params.id = ''
      checkOrder(params).then(response => {
        if (response.code === 200) {
          response.data.checkOrder.forEach(item => {
            this.judgeArr.push(item.positionId)
          })
        }
      })
      this.listLoading = true
      this.tableData = []
      searchPositions(this.listQuery).then(response => {
        response.data.positionList.forEach(item => {
          const data = {}
          data.positionId = item.positionId
          data.areaId = item.areaId
          data.shelveId = item.shelveId
          if (item.goodsType === 1) {
            data.goodsType = '药品'
          } else if (item.goodsType === 2) {
            data.goodsType = '医疗器械'
          } else if (item.goodsType === 3) {
            data.goodsType = '医疗辅助用品'
          } else if (item.goodsType === 4) {
            data.goodsType = '放射物资及危险品'
          } else {
            data.goodsType = '-'
          }
          data.capacity = item.capacity
          if (item.state === 1) {
            data.state = '普通存储区'
          } else if (item.state === 0) {
            data.state = '暂存区'
          } else if (item.state === 2) {
            data.state = '处理品区'
          }
          if (item.goods) {
            data.good = item.goods.goodsName
            data.num = item.goods.groundingNum + item.goods.unit
            if (item.batchInfo) {
              data.batchInfo = item.batchInfo
            } else {
              data.batchInfo = '-'
            }
          } else {
            data.good = '-'
            data.num = '-'
            data.batchInfo = '-'
          }
          this.tableData.push(data)
        })
        this.getSpanArr()
      })
      // Just to simulate the time of the request
      setTimeout(() => {
        this.listLoading = false
      }, 500)
    },
    getOpt() {
      this.goodsOptions = []
      this.areaOptions = []
      searchPositions().then(response => {
        console.log(response)
        response.data.positionList.forEach(item => {
          const goodsOpt = {}
          const areaOpt = {}
          if (item.goods) {
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
          }
          let flag = true
          this.areaOptions.forEach(i => {
            if (i.typeId === item.goodsType) {
              flag = false
            }
          })
          if (flag) {
            areaOpt.typeId = item.goodsType
            if (item.goodsType === 1) {
              areaOpt.typeId = 1
              areaOpt.typeName = '药品'
            } else if (item.goodsType === 2) {
              areaOpt.typeId = 2
              areaOpt.typeName = '医疗器械'
            } else if (item.goodsType === 3) {
              areaOpt.typeId = 3
              areaOpt.typeName = '医疗辅助用品'
            } else if (item.goodsType === 4) {
              areaOpt.typeId = 4
              areaOpt.typeName = '放射物资及危险品'
            }
            if (item.goodsType) {
              this.areaOptions.push(areaOpt)
            }
          }
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
    handleCreate(row) {
      this.form = {
        desc: ''
      }
      this.dialogFormVisible = true
      this.createRequest.creatorId = this.$store.getters.userId
      this.createRequest.positionId = row.positionId
      this.formList[0].value = row.areaId
      this.formList[1].value = row.shelveId
    },
    create() {
      this.createRequest.desc = this.form.desc
      createOrder(this.createRequest).then(response => {
        this.changeReq.state = 1
        this.changeReq.orderId = response.data.checkOrder.id
        this.changeReq.userId = this.$store.getters.userId
        this.changeReq.remark = '创建盘点任务'
        changeState(this.changeReq).then(resp => {
          if (resp.code === 200) {
            this.$message({
              showClose: true,
              type: 'success',
              message: '创建任务成功!'
            })
          } else {
            this.$message({
              showClose: true,
              type: 'error',
              message: '创建任务失败!'
            })
          }
          this.form = {
            desc: ''
          }
          this.dialogFormVisible = false
          this.getList()
        })
      })
    },
    // 合并areaId相同的行
    getSpanArr() {
      let count = 0 // 用来记录需要合并行的起始位置
      this.mergeObj['areaId'] = [] // 记录每一列的合并信息
      this.tableData.forEach((item, index) => {
        // index == 0表示数据为第一行，直接 push 一个 1
        if (index % this.pageSize === 0) {
          this.mergeObj['areaId'].push(1)
          count = index
        } else {
          // 判断当前行是否与上一行其值相等 如果相等 在 count 记录的位置其值 +1 表示当前行需要合并 并push 一个 0 作为占位
          if (item.areaId === this.tableData[index - 1]['areaId']) {
            this.mergeObj['areaId'][count] += 1
            this.mergeObj['areaId'].push(0)
          } else {
            // 如果当前行和上一行其值不相等
            count = index // 记录当前位置
            this.mergeObj['areaId'].push(1)// 重新push 一个 1
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
    },
    judge(row) {
      if (row.batchInfo !== '-') {
        // 存在盘点任务，禁止盘点
        if (this.judgeArr.indexOf(row.positionId) !== -1) {
          return true
        } else {
          return false
        }
      } else {
        // 不存在批次，禁止盘点
        return true
      }
    },
    getTag(state) {
      if (state === '普通存储区') {
        return 'success'
      } else if (state === '处理品区') {
        return 'danger'
      } else {
        return 'info'
      }
    }
  }
}
</script>
