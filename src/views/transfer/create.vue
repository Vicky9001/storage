<template>
  <div class="app-container">
    <el-table
      ref="multipleTable"
      :data="tableData.slice((currentPage-1)*pageSize,currentPage*pageSize)"
      border
      fit
      highlight-current-row
      style="width: 80%;margin-top:40px;margin-bottom: 40px;margin-left: 70px;margin-right: 70px"
    >
      <el-table-column prop="areaType" label="货区类型" :formatter="areasType" width="302" align="center" />
      <el-table-column prop="goodsName" label="货物名" width="300" align="center" />
      <el-table-column prop="goodsNum" label="货物数量" width="300" align="center" />
    </el-table>
    <el-divider />
    <el-form ref="dataForm" :model="form" :rules="rules" label-position="left" style="margin-left:120px;">
      <el-row>
        <el-col :span="12">
          <el-form-item label="目标货区类型">
            <el-select v-model="form.targetArea" placeholder="请选择目标货区" style="margin-right:20px; width: 150px">
              <el-option v-for="item in areaTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内配数目 " prop="num">
            <el-input v-model="form.num" placeholder="请输入内配数目" autocomplete="off" style="margin-right:20px; width: 180px" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="margin-top:20px;">
        <el-form-item label="移库说明">
          <el-input v-model="form.desc" style="width:680px;" type="textarea" :rows="3" placeholder="请输入移库说明" />
        </el-form-item>
      </el-form>
      <el-button style="margin-top:20px;" type="primary" @click="handleCreate">
        创建
      </el-button>
    </el-form>
  </div>

</template>

<script>
import waves from '@/directive/waves'
import { createOrder } from '@/api/transfer'// waves directive

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
    areaTypeFilter(type) {
      return areaTypeKeyValue[type]
    }
  },
  data() {
    return {
      tableData: [],
      areaTypeOptions,
      originPostion: '',
      creatorId: '',
      form: {
        targetArea: '',
        num: '',
        desc: '',
        exceedMax: ''
      },
      rules: {
        num: [
          { required: true, message: '请再次输入', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              console.log(this.tableData[0].goodsNum)
              if (value > this.tableData[0].goodsNum) {
                callback(new Error('输入不能大于现有商品数'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      },
      listQueryReturn: [],
      multiSelection: [],
      nowDate: null,
      refresh: false,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10 // 每页的数据条数
    }
  },
  created() {
    this.mounted()
  },
  methods: {
    areasType(row, column, cellValue) {
      const areasTypes = [
        { key: 0, display_name: '暂存区' },
        { key: 1, display_name: '普通存储区' },
        { key: 2, display_name: '处理品区' }
      ]
      const areasTypesObj = areasTypes.find(type => type.key === row.areaType)
      return areasTypesObj ? areasTypesObj.display_name : ''
    },
    mounted() {
      // Get the `row` parameter from the query string
      const row = this.$route.query.row

      // Assign the values to the corresponding form fields
      this.tableData.push({
        areaType: row.area.areaType,
        goodsName: row.goods.goodsName,
        goodsNum: row.goodsNum,
        originPosition: row.positionId
      })
      console.log(this.tableData)
      console.log(this.tableData[0].originPosition)
    },
    handleCreate() {
      const create1 = {}
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          create1.targetArea = this.form.targetArea
          create1.num = this.form.num
          create1.desc = this.form.desc
          create1.originPosition = this.tableData[0].originPosition
          create1.creatorId = parseInt(this.$store.getters.userId)
          createOrder(create1).then(() => {
            this.$notify({
              title: 'Success',
              message: 'Create Successfully',
              type: 'success',
              duration: 500
            })
            this.$router.push('/transfer/searchTransfer')
          })
        }
      })
    },
    resetTemp() {
      this.form = {
        targetArea: '',
        num: '',
        desc: '',
        originPostion: ''
      }
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
