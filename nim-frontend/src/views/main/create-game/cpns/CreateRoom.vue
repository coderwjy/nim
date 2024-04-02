<template>
  <div class="create-room">
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
      <el-form-item label="回合时长">
        <el-input-number v-model.number="gameConfig.roundTime" :min="10" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

import appStore from '@/stores'
import type {
  ICreateOnlineGameConfig,
  IOpenGameWebSocketRequest
} from '@/service/game/type'
import router from '@/router'
import { ROUND_MAX_TIME } from '@/constant/game_config'

const { userInfo } = appStore.useUserStore
const gameStore = appStore.useGameStore

const gameConfig = ref<ICreateOnlineGameConfig>({
  count: 3,
  isOwnerFirst: true,
  minNumberValue: 10,
  maxNumberValue: 100,
  roundTime: ROUND_MAX_TIME
})

async function createRoomAction() {
  gameStore.gameConfig = gameConfig.value
  const openGameWebSocketConfig: IOpenGameWebSocketRequest = {
    userAccount: userInfo.userAccount ?? '',
    isOwner: true,
    roomId: '',
    count: gameConfig.value.count,
    isOwnerFirst: gameConfig.value.isOwnerFirst,
    minNumberValue: gameConfig.value.minNumberValue,
    maxNumberValue: gameConfig.value.maxNumberValue
  }
  await gameStore.openGameWebSocket(openGameWebSocketConfig)
  await router.push('/main/game')
}

defineExpose({ createRoomAction })
</script>

<style lang="less" scoped></style>
