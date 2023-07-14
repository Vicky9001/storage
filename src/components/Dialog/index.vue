<template>
  <el-dialog :title="title" :visible.sync="dialogVisible">
    <el-form ref="dataForm" :model="form" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
      <el-form-item v-for="(item, index) in formList" :key="index" :label="item.label" :prop="item.prop">
        <template v-if="item.type === 'input'">
          <el-input v-model="form[item.prop]" :type="item.inputType" :rows="item.rows" :placeholder="item.placeholder" oninput="value=value.replace(/[^\d]/g,'')" />
        </template>
        <template v-else-if="item.type === 'textarea'">
          <el-input v-model="form[item.prop]" type="textarea" :rows="item.rows" :placeholder="item.placeholder" />
        </template>
        <template v-else-if="item.type === 'slot'">
          <slot :name="item.slotName" />
        </template>
        <template v-else-if="item.type === 'default'">
          {{ item.value }}
        </template>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleUpdate">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'MyDialog',
  props: {
    title: {
      type: String,
      required: true
    },
    formList: {
      type: Array,
      required: true
    },
    dialogVisible: {
      type: Boolean,
      required: true
    },
    form: {
      type: Object,
      required: true
    },
    updateMethod: {
      type: Function,
      required: true
    }
  },
  methods: {
    handleUpdate() {
      // 这里使用$emit来触发update事件
      // this.$emit('update')
      this.updateMethod()
    }
  }
}
</script>
