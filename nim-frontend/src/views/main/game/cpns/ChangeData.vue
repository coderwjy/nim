<template>
  <div class="change-data">
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
import { localCache } from '@/utils/cache'
import { JOIN_ROOM_ID } from '@/constant/cache_keys'

// 表单数据
const oldNumberValue = ref(0)
const changeValue = ref(1)
const newNumberValue = ref(oldNumberValue.value)
function changeOldValue(newValue: number) {
  oldNumberValue.value = newValue
}
function resetChangeValue() {
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

// 游戏信息
const { modifyNumber } = appStore.useGameStore

// 处理数据修改
function handleChangeNumber() {
  modifyNumber(
    localCache.getCache(JOIN_ROOM_ID) ?? '',
    oldNumberValue.value,
    changeValue.value
  )
}

defineExpose({ handleChangeNumber, changeOldValue, resetChangeValue })
</script>

<style lang="less" scoped></style>
