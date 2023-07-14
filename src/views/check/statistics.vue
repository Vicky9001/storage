<template>
  <div class="container">
    <el-card style="width:1000px ;height: 75px;margin: 30px 30px 30px 100px;background: #3c6eff">
      <el-button type="danger">今日数据</el-button>
      <el-button>本周数据</el-button>
      <el-button>月度统计</el-button>
      <el-button>年度统计</el-button>
    </el-card>
    <el-card style="width:1000px;margin: 30px 30px 30px 100px;padding-left:20px;background: #efefef">
      <el-col class="card-panel-col">
        <div class="card-panel-description">
          <div class="card-panel-text">
            采购次数
          </div>
          <CountTo :start-val="0" :end-val="purchaseNum" :duration="2000" style="font-size: 30px;" />
        </div>
      </el-col>
      <el-col class="card-panel-col">
        <div class="card-panel-description">
          <div class="card-panel-text">
            入库次数
          </div>
          <CountTo :start-val="0" :end-val="inNum" :duration="2000" style="font-size: 30px;" />
        </div>
      </el-col>
      <el-col class="card-panel-col">
        <div class="card-panel-description">
          <div class="card-panel-text">
            出库次数
          </div>
          <CountTo :start-val="0" :end-val="outNum" :duration="2000" style="font-size: 30px;" />
        </div>
      </el-col>
      <el-col class="card-panel-col">
        <div class="card-panel-description">
          <div class="card-panel-text">
            内配次数
          </div>
          <CountTo :start-val="0" :end-val="transferNum" :duration="2000" style="font-size: 30px;" />
        </div>
      </el-col>
      <el-col class="card-panel-col">
        <div class="card-panel-description">
          <div class="card-panel-text">
            费用流水
          </div>
          ￥<CountTo :start-val="0" :end-val="totalPrice" :duration="2000" style="font-size: 30px;" />
        </div>
      </el-col>
    </el-card>
    <el-card style="width:850px ;height: 450px;margin: 30px 30px 30px 176px">
      <!--    <el-card class="el-card-define">-->
      <div id="echarts1" style="width:700px ;height: 400px;" />
    </el-card>
    <el-card style="width:850px ;height: 450px;margin: 30px 30px 30px 176px">
      <!--    <el-card class="el-card-define">-->
      <div id="echarts2" style="width:700px ;height: 400px;" />
    </el-card>
  </div>
</template>

<script>
import echarts from 'echarts'
import CountTo from 'vue-count-to'
import { getTransferNum } from '@/api/transfer'
import { getOutNum } from '@/api/outStore'
import { getPurchaseNum } from '@/api/purchase'
import { getInNum, getOrderPrice } from '@/api/inStore'
import { statistics, getOut } from '@/api/check'

export default {
  name: 'Echarts1',
  components: {
    CountTo
  },
  data() {
    return {
      // option配置
      transferNum: null,
      outNum: null,
      inNum: null,
      totalPrice: null,
      purchaseNum: null,
      option: {
        title: {
          text: '入库金额统计图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['入库']
        },
        grid: {
          left: '20%',
          right: '20%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          name: '入库时间',
          type: 'category',
          data: []
        },
        yAxis: {
          name: '金额/（元）',
          type: 'value'
        },
        series: [
          {
            name: '入库',
            data: [150, 100, 100, 218],
            type: 'line'
          }
        ]
      },
      option2: {
        title: {
          text: '出库金额统计图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['出库']
        },
        grid: {
          left: '20%',
          right: '20%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          name: '出库时间',
          type: 'category',
          data: []
        },
        yAxis: {
          name: '金额/（元）',
          type: 'value'
        },
        series: [
          {
            name: '入库',
            data: [],
            type: 'line'
          }
        ]
      }
    }
  },
  mounted() {
    this.echartsInit()
    this.echartsInit2()
  },
  created() {
    this.getList()
  },
  methods: {
    echartsInit() {
      statistics().then(response => {
        if (response.code === 200) {
          const timeList = []
          const xy = []
          response.data.warehousing.forEach(item => {
            const a = []
            a.push(item.time)
            a.push(item.goodsPrice)
            xy.push(a)
            const time = new Date(item.time).toLocaleDateString({
              timeZone: 'UTC',
              year: 'numeric',
              month: '2-digit',
              day: '2-digit'
            }).split('/').join('-')
            // alert(time)
            timeList.push(time)
          })
          for (let i = 0; i < timeList.length; i++) {
            for (let j = i + 1; j < timeList.length; j++) {
              if (timeList[i] > timeList[j]) {
                let temp = {}
                temp = timeList[i]
                timeList[i] = timeList[j]
                timeList[j] = temp
              }
            }
          }
          const data = []
          for (let i = 0; i < timeList.length; i++) {
            for (let j = 0; j < xy.length; j++) {
              xy[j][0] = new Date(xy[j][0]).toLocaleDateString({
                timeZone: 'UTC',
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
              }).split('/').join('-')
              if (timeList[i] === xy[j][0]) {
                data.push(xy[j][1])
                break
              }
            }
          }
          this.option.xAxis.data = timeList
          this.option.series[0].data = data
        }
        echarts.init(document.getElementById('echarts1')).setOption(this.option)
      })
    },
    echartsInit2() {
      getOut().then(response => {
        if (response.code === 200) {
          const timeList = []
          const xy = []
          response.data.out.forEach(item => {
            const a = []
            a.push(item.time)
            a.push(item.goodsPrice)
            xy.push(a)
            const time = new Date(item.time).toLocaleDateString({
              timeZone: 'UTC',
              year: 'numeric',
              month: '2-digit',
              day: '2-digit'
            }).split('/').join('-')
            // alert(time)
            timeList.push(time)
          })
          for (let i = 0; i < timeList.length; i++) {
            for (let j = i + 1; j < timeList.length; j++) {
              if (timeList[i] > timeList[j]) {
                let temp = {}
                temp = timeList[i]
                timeList[i] = timeList[j]
                timeList[j] = temp
              }
            }
          }
          const data = []
          for (let i = 0; i < timeList.length; i++) {
            for (let j = 0; j < xy.length; j++) {
              xy[j][0] = new Date(xy[j][0]).toLocaleDateString({
                timeZone: 'UTC',
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
              }).split('/').join('-')
              if (timeList[i] === xy[j][0]) {
                data.push(xy[j][1])
                break
              }
            }
          }
          this.option2.xAxis.data = timeList
          this.option2.series[0].data = data
        }
        echarts.init(document.getElementById('echarts2')).setOption(this.option2)
      })
      // 在生命周期中挂载
      echarts.init(document.getElementById('echarts1')).setOption(this.option)
    },
    getList() {
      getTransferNum().then(response => {
        this.transferNum = response.data.transferNum
        console.log(this.transferNum)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
      getOutNum().then(response => {
        this.outNum = response.data.outNum
        console.log(this.outNum)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
      getInNum().then(response => {
        this.inNum = response.data.warehousingNum
        console.log(this.inNum)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
      getOrderPrice().then(response => {
        this.totalPrice = response.data.price
        console.log(this.totalPrice)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
      getPurchaseNum().then(response => {
        this.purchaseNum = response.data.purchaseNum
        console.log(this.purchaseNum)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  max-height: 1200px;
  position: relative;
  margin: 0 auto 60px;
}

.card-panel-col {
  width: 180px;
  margin-bottom: 32px;
}
.card-panel-description {
  text-align: center;
  font-weight: bold;
  margin: 15px;
}

.card-panel-text {
  line-height: 18px;
  color: rgba(0, 0, 0, 0.45);
  font-size: 18px;
  margin-bottom: 12px;
}
</style>
