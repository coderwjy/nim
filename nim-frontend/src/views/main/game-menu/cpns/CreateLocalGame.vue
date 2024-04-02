<template>
  <div class="create-local-game">
    <el-form :model="gameConfig" label-width="120px">
      <el-form-item label="数字数量">
        <el-input-number v-model.number="gameConfig.count" :min="2" />
      </el-form-item>
      <el-form-item label="是否先手">
        <el-switch v-model="gameConfig.isOwnerFirst" />
      </el-form-item>
      <el-form-item label="数字大小范围">
        <el-input-number
          v-model.number="gameConfig.minNumberValue"
          :min="0"
          size="small"
        />
        <span>&nbsp;&nbsp;~&nbsp;&nbsp;</span>
        <el-input-number
          v-model.number="gameConfig.maxNumberValue"
          :min="gameConfig.minNumberValue"
          size="small"
        />
      </el-form-item>
      <el-form-item label="游戏模式">
        <el-select v-model="gameConfig.gameMode">
          <el-option
            v-for="mode in gameModeList"
            :key="mode.value"
            :label="mode.label"
            :value="mode.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="回合时长">
        <el-input-number v-model.number="gameConfig.roundTime" :min="5" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

import type { ICreateLocalGameConfig } from '@/service/game/type'
import router from '@/router'
import appStore from '@/stores'
import { ROUND_MAX_TIME } from '@/constant/game_config'

const gameModeList = [
  {
    value: 0,
    label: '新手'
  },
  {
    value: 1,
    label: '普通'
  },
  {
    value: 2,
    label: '困难'
  },
  {
    value: 3,
    label: '亚洲'
  }
]

const gameConfig = ref<ICreateLocalGameConfig>({
  count: 3,
  isOwnerFirst: true,
  minNumberValue: 10,
  maxNumberValue: 100,
  gameMode: 0,
  roundTime: ROUND_MAX_TIME
})

async function createLocalGameAction() {
  appStore.useLocalGameStore.setLocalGameConfig(gameConfig.value)
  await router.push('/main/local-game')
}

defineExpose({ createLocalGameAction })
</script>

<style lang="less" scoped></style>
