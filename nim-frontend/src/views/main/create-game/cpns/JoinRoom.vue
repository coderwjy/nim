<template>
  <div class="join-room">
    <el-form :model="gameConfig" label-width="120px">
      <el-form-item label="房间号">
        <el-input v-model="gameConfig.roomId" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

import type { IOpenGameWebSocketRequest } from '@/service/game/type'
import appStore from '@/stores'
import router from '@/router'
import { localCache } from '@/utils/cache'
import { JOIN_ROOM_ID } from '@/constant/cache_keys'

const { userInfo } = appStore.useUserStore
const gameStore = appStore.useGameStore
const gameConfig = ref({
  roomId: localCache.getCache(JOIN_ROOM_ID) ?? ''
})

async function joinRoomAction() {
  const openGameWebSocketConfig: IOpenGameWebSocketRequest = {
    userAccount: userInfo.userAccount ?? '',
    isOwner: false,
    roomId: gameConfig.value.roomId,
    count: 0,
    isOwnerFirst: false,
    minNumberValue: 0,
    maxNumberValue: 0
  }
  await gameStore.openGameWebSocket(openGameWebSocketConfig)
  localCache.setCache(JOIN_ROOM_ID, openGameWebSocketConfig.roomId)
  await router.push('/main/game')
}

defineExpose({ joinRoomAction })
</script>

<style lang="less" scoped></style>
