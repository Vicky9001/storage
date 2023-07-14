<template>
  <div class="container">
    <div>
      <el-tag type="success" style="margin-left:32px;margin-top: 16px;margin-bottom: 16px" effect="dark">货架为空</el-tag>
      <el-tag type="danger" style="margin-left:30px;margin-top: 16px;margin-bottom: 16px" effect="dark">货架存在预警</el-tag>
      <el-tag style="margin-left:30px;margin-top: 16px;margin-bottom: 16px" effect="plain">货架被占用</el-tag>
    </div>
    <el-tabs v-model="activeIndex" type="border-card" :tab-position="tabPosition" @tab-click="handleTabClick">
      <el-tab-pane
        v-for="(area, index) in areas"
        :key="index"
        :label="area.type+'-'+area.name"
      >
        <div class="display">
          <el-button
            v-for="(area, index) in shelve"
            :key="index"
            ref="rectangles"
            :type="changeType(area)"
            class="rectangle"
            @click="handle(area)"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    <!--    详情弹窗-->
    <el-dialog :visible.sync="dialogVisible" width="50%">
      <el-descriptions title="详情" direction="vertical" :column="2" border style="width: 90% ">
        <el-descriptions-item label="货架ID">
          {{ info.id }}
        </el-descriptions-item>
        <el-descriptions-item label="预警信息">
          {{ info.warningRecords }}
        </el-descriptions-item>
        <el-descriptions-item label="最大储量">
          {{ info.capacity }}
        </el-descriptions-item>
        <el-descriptions-item label="存储物资">
          {{ info.good }}
        </el-descriptions-item>
        <el-descriptions-item label="存储数量">
          {{ info.num }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { visulization } from '@/api/visulization'
import { getArea } from '@/api/shelve'

export default {
  name: 'ComplexTable',
  // components: { Pagination },
  directives: { waves },
  data() {
    return {
      areas: [],
      shelve: [],
      tabPosition: 'left',
      activeIndex: 0,
      areaName: ['A', 'B', 'C', 'D'],
      goodsType: ['药品', '医疗器械', '医疗辅助用品', '放射物资及危险品'],
      type: ['暂存区', '普通存储区', '处理区'],
      dialogVisible: false,
      info: {}
    }
  },
  created() {
    this.getList()
    this.handleTabClick()
  },
  methods: {
    getList: async function() {
      const response = await visulization()
      let lastArea = []
      lastArea.id = 0
      lastArea.warning = false
      lastArea.ocupy = false
      let shelves = []
      let goods = []
      let shelveNum = 0
      if (response.code === 200) {
        for (const item of response.data.positionList) {
          console.log(lastArea.id)
          console.log(item.areaId)
          if (lastArea.id !== item.areaId) {
            if (lastArea.id !== 0) {
              lastArea.shelveNum = shelveNum
              lastArea.shelves = shelves
              lastArea.goods = goods
              const list = { id: lastArea.id }
              lastArea.type = await this.getType(list)
              this.areas.push(lastArea)
            }
            shelveNum = 0
            shelves = []
            goods = []
            lastArea = []
            lastArea.name = this.areaName[Math.floor((item.areaId - 1) / 3)]
            lastArea.id = item.areaId
            lastArea.warning = false
            lastArea.ocupy = false
          }
          const shelve = {}
          shelve.id = item.shelveId
          shelve.warningRecords = ''
          shelveNum++
          if (item.goods !== null) {
            lastArea.ocupy = true
            shelve.good = item.goods.goodsName
            shelve.num = item.goodsNum + item.goods.unit
            shelve.n = item.goodsNum
            if (goods.indexOf(item.goods.goodsName) === -1) {
              goods.push(item.goods.goodsName)
            }
          } else {
            shelve.good = '-'
            shelve.n = 0
          }
          if (item.warningRecords !== null && item.warningRecords.length !== 0) {
            item.warningRecords.forEach((i, index) => {
              if (index !== 0) {
                shelve.warningRecords = shelve.warningRecords + '、'
              }
              if (i.type === 1) {
                shelve.warningRecords = shelve.warningRecords + '库存过剩'
              } else if (i.type === 2) {
                shelve.warningRecords = shelve.warningRecords + '库存不足'
              } else if (i.type === 3) {
                shelve.warningRecords = shelve.warningRecords + '即将过期'
              } else if (i.type === 4) {
                shelve.warningRecords = shelve.warningRecords + '已过期'
              } else if (i.type === 5) {
                shelve.warningRecords = shelve.warningRecords + '积压预警'
              }
            })
            lastArea.warning = true
          } else {
            shelve.warningRecords = '-'
          }
          shelve.capacity = item.capacity
          shelves.push(shelve)
        }
        lastArea.shelveNum = shelveNum
        lastArea.shelves = shelves
        lastArea.goods = goods
        const list = { id: lastArea.id }
        lastArea.type = await this.getType(list)
        this.areas.push(lastArea)
      } else {
        this.$message({
          showClose: true,
          type: 'error',
          message: '仓库数据获取失败!'
        })
      }
      // Just to simulate the time of the request
      setTimeout(() => {
        this.listLoading = false
      }, 500)
    },
    handleTabClick() {
      this.shelve = this.areas[this.activeIndex].shelves
    },
    handle(area) {
      this.info = area
      this.dialogVisible = true
    },
    changeType(area) {
      if (area.good === '-') {
        return 'success'
      } else if (area.warningRecords !== '-') {
        return 'danger'
      }
    },
    async getType(list) {
      const response = await getArea(list)
      if (response.code === 200) {
        const a = response.data.areaList[0].areaType
        const res = this.type[a]
        return res
      }
    }
  }
}
</script>

<style>
.rectangle {
  width: 150px;
  height: 50px;
  margin-bottom: 20px;
  border:1px solid cornflowerblue;
}

.display {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  flex-wrap: wrap;
}
</style>
