<template>
  <div>
    <el-form label-width="120px">
      <el-form-item label="原始值">
        <el-input v-model.number="oldNumberValue" disabled />
      </el-form-item>
      <el-form-item label="修改量">
        <el-input-number v-model.number="changeValue" :min="1" />
      </el-form-item>
      <el-form-item label="修改后值">
        <el-input v-model.number="newNumberValue" disabled />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'

import appStore from '@/stores'

// 表单数据
const oldNumberValue = ref(0)
const changeValue = ref(1)
const newNumberValue = ref(oldNumberValue.value)
const changeIndex = ref('0')

/**
 * 初始化表单数据
 * @param index 修改的数字下标
 * @param valueBeforeModify 修改前值
 */
function formInit(index: string, valueBeforeModify: number) {
  changeIndex.value = index
  oldNumberValue.value = valueBeforeModify
  changeValue.value = 1
}

watch(
  () => changeValue.value,
  () => {
    const residue = oldNumberValue.value - changeValue.value
    newNumberValue.value = residue < 0 ? 0 : residue
  }
)
watch(
  () => oldNumberValue.value,
  () => {
    const residue = oldNumberValue.value - changeValue.value
    newNumberValue.value = residue < 0 ? 0 : residue
  }
)

// 处理数据修改
function handleChangeNumber(): boolean {
  return appStore.useLocalGameStore.modifyNumber(
    changeIndex.value,
    oldNumberValue.value,
    changeValue.value
  )
}

defineExpose({ handleChangeNumber, formInit })
</script>

<style lang="less" scoped></style>
