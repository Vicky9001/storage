<template>
  <div class="position">
    <div
      v-for="(area, index) in area"
      :key="index"
      ref="rectangles"
      :class="{ white: area.good==='-', red: area.warningRecords!=='-' }"
      class="rectangle1"
    >
      <div class="info" style="white-space: pre-wrap;">
        <div>id：{{ area.id }}</div>
        <div>最大储量：{{ area.capacity }}</div>
        <div>物资：{{ area.good }}</div>
        <div v-if="area.good!=='-'">物资数量：{{ area.num }}</div>
        <div v-else>物资数量：{{ area.n }}</div>
        <div>预警信息：{{ area.warningRecords }}</div>
      </div>
    </div>
    <!--    <div class="area-info" v-if="selectedAreaIndex !== -1" :style="areaStyle">-->
    <!--      <div>id：{{ selectedArea.id }}</div>-->
    <!--      <div>最大储量：{{ selectedArea.capacity }}</div>-->
    <!--      <div>物资：{{ selectedArea.good }}</div>-->
    <!--      <div v-if="selectedArea.good !== '-'">物资数量：{{ selectedArea.num}}</div>-->
    <!--      <div v-else>物资数量：{{ selectedArea.n}}</div>-->
    <!--      <div>预警信息：{{ selectedArea.warningRecords}}</div>-->
    <!--    </div>-->
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
export default {
  name: 'ComplexTable',
  directives: { waves },
  data() {
    return {
      area: {}
      // selectedAreaIndex: -1,
      // selectedArea: {},
      // mousePosition: { x: 0, y: 0 }
    }
  },
  mounted() { // 使用mounted钩子函数，确保DOM元素被渲染后再修改背景色
    for (let i = 0; i < this.$refs.rectangles.length; i++) {
      const rectangle = this.$refs.rectangles[i]
      const num = this.area[i].n
      const color = `rgba(0,255,0,${0.1 + num / 10})` // 通过数量计算对应的颜色值
      rectangle.style.backgroundImage = `linear-gradient(to bottom, #9AFF9A 0%, ${color} 100%)`
    }
  },
  created() {
    this.area = this.$route.query.area
  }
  // computed: {
  //   areaStyle() {
  //     return {
  //       position: 'fixed',
  //       top: this.mousePosition.y + 10 + 'px',
  //       left: this.mousePosition.x + 10 + 'px',
  //       zIndex: 999,
  //       fontSize: '1.2rem',
  //       padding: '20px'
  //     }
  //   }
  // }
  // methods: {
  //   showArea(index) {
  //     this.selectedAreaIndex = index
  //     this.selectedArea = this.area[index]
  //     this.mousePosition.x = event.clientX
  //     this.mousePosition.y = event.clientY
  //   },
  //   hideArea() {
  //     this.selectedAreaIndex = -1
  //     this.selectedArea = {}
  //   }
  // }
}
</script>

<style>
.rectangle1 {
  width: 200px;
  height: auto;
  margin-top: 20px;
  margin-right: 50px;
  text-align: center;
  line-height: 50px;
  cursor: pointer;
  border:1px solid #000;
  align-items: stretch;
}

.red {
  background-color: red;
}

.green {
  background-color: forestgreen;
}

.white {
  background-color: white;
}

.position {
  display: flex;
  flex-direction: row;
  /*justify-content: space-around;*/
  flex-wrap: wrap;
  /*align-content: space-around;*/
}

.info {
  display: flex;
  flex-direction: column;
}

/*.area-info {*/
/*  background-color: white;*/
/*  border:1px solid #000;*/
/*}*/
</style>
