<template>
  <div class="app-container">
    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 95%;margin-top:20px;"
    >
      <el-table-column label="内配单号" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人名称" width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.creatorName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" min-width="80" align="center">
        <template slot-scope="{row}">
          <span>{{ row.time }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内配单描述" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="进度" width="150" align="center">
        <template slot-scope="{row}">
          <span>{{ row.state | stateFilter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button icon="el-icon-search" size="mini" @click="scanDesc(row)">
            详情
          </el-button>
          <el-button icon="el-icon-check" type="primary" size="mini" @click="handleReserve(row)">
            内配完成
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
      style="width: 95% ;margin-top:20px;"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <el-dialog :title="textMap['approve']" :visible.sync="reserveFormVisible">
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="margin-left:50px;">
        <el-form-item label="审批意见">
          <el-input v-model="form.remark" style="width:350px;" type="textarea" :rows="3" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reserveData">
          确定
        </el-button>
        <el-button type="info" @click="reserveFormVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="textMap['desc']" :visible.sync="DescVisible" width="900px">
      <el-form ref="dataForm" :model="form" label-position="left" style="margin-left:70px;">
        <el-row>
          <el-col :span="10">
            <el-form-item label="创建人名称">
              <el-input v-model="form.creatorName" readonly style="margin-right:20px; width: 180px" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="创建时间 ">
              <el-input v-model="form.time" readonly style="margin-right:20px; width: 180px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="内配单描述">
          <el-input v-model="form.desc" readonly style="margin-right:20px; width: 500px" />
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="内配单状态">
              <el-select v-model="form.state" :disabled="true" style="margin-right:20px; width: 180px">
                <el-option v-for="item in stateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="内配数目">
              <el-input v-model="form.num" readonly style="margin-right:20px; width: 100px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-table
              :data="form.originP"
              border
              fit
              highlight-current-row
              style="width: 90% ;margin-top:20px;"
            >
              <el-table-column label="初始仓库名" width="105" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.houseName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="初始仓库类型" width="110" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.houseType| housetypeFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column label="初始货区类型" width="110" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.areaType| areatypeFilter }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="11">
            <el-table
              :data="form.targetP"
              border
              fit
              highlight-current-row
              style="width: 90% ;margin-top:20px;"
            >
              <el-table-column label="目标仓库名" width="105" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.houseName }}</span>
                </template>
              </el-table-column>
              <el-table-column label="目标仓库类型" width="110" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.houseType| housetypeFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column label="目标货区类型" width="110" align="center">
                <template slot-scope="{row}">
                  <span>{{ row.area.areaType| areatypeFilter }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-table
          :data="form.goods"
          border
          fit
          highlight-current-row
          style="width: 87% ;margin-top:30px;"
        >
          <el-table-column label="货物名称" width="120" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.goodsName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物信息" width="200" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.goodsInfo }}</span>
            </template>
          </el-table-column>
          <el-table-column label="单位" width="70" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.unit }}</span>
            </template>
          </el-table-column>
          <el-table-column label="仓库库存" width="97" align="center">
            <template slot-scope="{row}">
              <span>{{ row.goods.groundingNum }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物单重" width="100" align="center">
            <template slot-scope="{row}">
              <span>{{ row.unitWeight }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货物单价" width="100" align="center">
            <template slot-scope="{row}">
              <span>{{ row.unitPrice }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import waves from '@/directive/waves' // waves directive
import { getOrder, changeState } from '@/api/transfer'

const stateTypeOptions = [
  { key: 0, display_name: ' ' },
  { key: 1, display_name: '待审核' },
  { key: 2, display_name: '审核通过内配单' },
  { key: 3, display_name: '退回内配单' },
  { key: 4, display_name: '内配完成' }
]

const houseTypeOptions = [
  { key: 1, display_name: '药品' },
  { key: 2, display_name: '医疗器械' },
  { key: 3, display_name: '医疗辅助物资' },
  { key: 4, display_name: '放射物资及危险品' }
]
const areaTypeOptions = [
  { key: 0, display_name: '暂存区' },
  { key: 1, display_name: '普通存储区' },
  { key: 2, display_name: '处理品区' }
]

const opOptions = [
  { key: 11, display_name: '创建内配' },
  { key: 12, display_name: '审核通过' },
  { key: 13, display_name: '退回' },
  { key: 14, display_name: '内配完成确认' }
]
const houseTypeKeyValue = houseTypeOptions.reduce((h, cur) => {
  h[cur.key] = cur.display_name
  return h
}, {})
const stateTypeKeyValue = stateTypeOptions.reduce((states, cur) => {
  states[cur.key] = cur.display_name
  return states
}, {})
const areaTypeKeyValue = areaTypeOptions.reduce((area, cur) => {
  area[cur.key] = cur.display_name
  return area
}, {})

const opKeyValue = opOptions.reduce((ops, cur) => {
  ops[cur.key] = cur.display_name
  return ops
}, {})

export default {
  name: 'ComplexTable',
  directives: { waves },
  filters: {
    stateFilter(type) {
      return stateTypeKeyValue[type]
    },

    housetypeFilter(type) {
      return houseTypeKeyValue[type]
    },
    areatypeFilter(type) {
      return areaTypeKeyValue[type]
    },
    opFilter(type) {
      return opKeyValue[type]
    }
  },
  data() {
    return {
      stateTypeOptions,
      houseTypeOptions,
      areaTypeOptions,
      form: {
        id: '',
        creatorName: '',
        time: '',
        state: '',
        desc: '',
        num: '',
        task: [],
        goods: [],
        originP: '',
        targetP: ''
      },
      state: null,
      opOptions,
      options: [],
      DescVisible: false,
      textMap: {
        update: '更新内配单',
        desc: '详情页面'
      },
      listQueryReturn: [],
      tableData: [],
      tableData1: [],
      multiSelection: [],
      reserveFormVisible: false,
      nowDate: null,
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10, // 每页的数据条数
      tabClickIndex: null,
      tabClickLabel: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getOrder({ 'type': 2 }, { 'id': this.$store.getters.userId }).then(response => {
        this.tableData = response.data.transferOrder
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.form = {
        id: '',
        creatorName: '',
        time: '',
        state: '',
        desc: '',
        num: '',
        task: [],
        goods: [],
        originP: '',
        targetP: ''
      }
    },
    scanDesc(row) {
      this.form = Object.assign({}, row) // copy obj
      this.form.originP = []
      this.form.targetP = []
      this.form.originP.push(row.originP)
      this.form.targetP.push(row.targetP)
      this.DescVisible = true
      console.log(this.form)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleReserve(row) {
      this.form = Object.assign({}, row) // copy obj
      this.form.id = row.id
      this.reserveFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    reserveData() {
      const reserve1 = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          reserve1.state = 14
          reserve1.id = parseInt(this.form.id)
          reserve1.remark = this.form.remark
          reserve1.operator = parseInt(this.$store.getters.userId)
          changeState(reserve1).then(() => {
            this.reserveFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Reserve Successfully',
              type: 'success',
              duration: 2000
            })
          })
          setTimeout(() => {
            this.getList()
          }, 500)
        }
      })
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
