<template>
  <div class="container">
    <el-card class="box-card1" style="margin-top:20px;margin-bottom: 20px">
      <h1 class="title" style="margin: 30px;">自动化仓储管理系统</h1>
      <div>
        <p class="subtitle" style="margin-left: 81px; margin-right: 81px">本系统可以实现对医药及器械进行采购管理、入库管理、出库管理、库存管理、仓库设定管理、用户权限管理、相关方管理和报表查询等功能。对不同种类药品设置不同优先级，根据药品、器械的贮存日期、过期日期或保修日期提供不同的智能化处理方案，实现医疗自动化仓储管理。</p>
      </div>
      <div>
        <img src="@/views/dashboard/image/img_5.png" alt="UI Storyboard" class="image">
      </div>
    </el-card>
    <div class="grid-container" style="margin-top:50px;">
      <div class="grid-item">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>库存管理</span>
          </div>
          <div>
            <img src="@/views/dashboard/image/img.png" style="margin-top:50px;" alt="库存管理" class="image1">
          </div>
        </el-card>
      </div>
      <div class="grid-item">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>采购管理</span>
          </div>
          <div>
            <img src="@/views/dashboard/image/img_1.png" alt="采购管理" class="image3">
          </div>
        </el-card>
      </div>
      <div class="grid-item">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>入库管理</span>
          </div>
          <div>
            <img src="@/views/dashboard/image/img_2.png" alt="入库管理" class="image1">
          </div>
        </el-card>
      </div>
      <div class="grid-item">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>出库管理</span>
          </div>
          <div>
            <img src="@/views/dashboard/image/img_3.png" style="margin-top:30px;" alt="出库管理" class="image2">
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1200px;
  max-height: 1300px;
  padding: 20px;
  text-align: center;
  margin: 0 auto 40px;
}

.title {
  font-family: 微软雅黑;
  font-size: 24px;
  font-weight: bold;
  color: #032f5a;
  margin-bottom: 10px;
}

.subtitle {
  font-family: 华文中宋;
  font-size: 15px;
  letter-spacing: 3px;
  line-height: 25px;
  color: #3a3d3d;
  margin-bottom: 20px;
}

.grid-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.grid-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 280px;
}
.box-card {
  width: 400px;
  height: 300px;
}
.box-card1 {
  width: 960px;
  height: 600px;
  margin: 0 auto;
}

.image {
  max-width: 40%;
  max-height: 40%;
  object-fit: cover;
}
.image1 {
  max-width: 80%;
  max-height: 80%;
  object-fit: cover;
}
.image2 {
  max-width: 90%;
  max-height: 90%;
  object-fit: cover;
}
.image3 {
  max-width: 65%;
  max-height: 40%;
  object-fit: cover;
}
</style>
