<template>
  <div class="game-panel">
    <el-button
      type="danger"
      class="exitBtn"
      size="large"
      @click="handleLeaveRoom"
    >
      退出房间
    </el-button>
    <h1 class="title">
      {{ gameTitle }}
    </h1>
    <h1 v-if="status === 1">{{ '房间号: ' + roomId }}</h1>
    <h1 v-else>{{ '第' + round + '回合' }}</h1>
    <h1 v-show="status === 2 && isTurn">{{ '剩余时间: ' + countDown.text }}</h1>
    <div class="numbers">
      <template v-for="(number, index) in numbers" :key="number.key">
        <el-card class="number-item" @click="clickNumber(Number(index))">
          <template #header>
            <div class="card-header">
              <span class="course">{{ number.value }}</span>
            </div>
          </template>
          <div class="item"><span class="course-item">操作数字</span></div>
        </el-card>
      </template>
    </div>

    <!-- 修改数字对话框 -->
    <el-dialog v-model="changeDataDialogVisible" title="修改数字" width="30%">
      <ChangeData ref="changeDataRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="changeDataDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDialogConfirm">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 游戏结束对话框 -->
    <el-dialog
      v-model="gameOverDialogVisible"
      title="游戏结束"
      width="30%"
      :show-close="false"
    >
      <span class="game-over-content">
        {{ winner === userInfo.userAccount ? '胜利' : '失败' }}
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleExit">退出房间</el-button>
          <el-button type="primary" @click="handleGameAgain">
            再来一局
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

import ChangeData from './ChangeData.vue'
import appStore from '@/stores'
import { localCache, sessionCache } from '@/utils/cache'
import { ROOM_ID, ROUND_RESIDUE_TIME, IS_OWNER } from '@/constant/cache_keys'
import * as userService from '@/service/user'
import { ROUND_MAX_TIME } from '@/constant/game_config'

const { userInfo } = appStore.useUserStore
const gameStore = appStore.useGameStore

interface ICountDown {
  count: number
  text: string
  timer: any
}

const roundTime = gameStore.gameConfig.roundTime ?? ROUND_MAX_TIME
let countDown = reactive<ICountDown>({
  count: roundTime,
  text: '',
  timer: void 0
})

let roomId: string = localCache.getCache(ROOM_ID) ?? ''
if (roomId === '') {
  userService.getUserRoomId(userInfo.userAccount ?? '').then((res) => {
    roomId = res
    localStorage.setItem(ROOM_ID, roomId)
  })
}

const changeDataRef = ref<InstanceType<typeof ChangeData>>()
const changeDataDialogVisible = ref(false)
// const isMyTurn = ref<boolean>(false)
let gameTitle = ref<string>('游戏开始')
let currentProcessNumberIndex = ref(0)
const gameOverDialogVisible = ref(false)

const { numbers, round, isTurn, status, winner } = storeToRefs(gameStore)

// 游戏房间状态监控
watch(
  () => [status.value, isTurn.value],
  (newValue, oldValue) => {
    const isPartnerReconnect: boolean = oldValue[0] === 3 && newValue[0] === 2
    switch (status.value) {
      case 0:
        gameTitle.value = '房间未使用'
        break
      case 1:
        gameTitle.value = '等待玩家加入房间'
        break
      case 2:
        if (isTurn.value === true) {
          gameTitle.value = '你的回合'
          if (isPartnerReconnect) {
            const residueTime = sessionCache.getCache(ROUND_RESIDUE_TIME)
            const isOwner = sessionCache.getCache(IS_OWNER)
            if (!isOwner) {
              countDown.count = residueTime - 1 ?? roundTime
              sessionCache.deleteCache(ROUND_RESIDUE_TIME)
            } else {
              countDown.count -= 1
            }
          } else {
            countDown.count = roundTime - 1
          }
        } else {
          gameTitle.value = '对手回合'
          countDown.count = roundTime
        }
        break
      case 3:
        gameTitle.value = '游戏暂停中...'
        if (winner.value === 'GAME_OVER') {
          countDown.count += 1
          ElMessage.warning('对方退出游戏')
        }
        break
      case 4:
        gameTitle.value = '游戏结束'
        // todo: 考虑特殊情况？？暂时未找到特殊情况
        countDown.count = roundTime
        if (winner.value) {
          if (winner.value === 'GAME_OVER') {
            winner.value = userInfo.userAccount ?? ''
          }
          gameOverDialogVisible.value = true
        }
        break
    }
  }
)

/**
 * 点击数字
 */
function clickNumber(clickNumberIndex: number) {
  if (isTurn.value) {
    // 判断数字是否为0
    if (numbers.value[clickNumberIndex].value === 0) {
      ElMessage.warning('当前数字已减为0, 无法再操作')
      return
    }
    // 显示数字对话框
    currentProcessNumberIndex.value = clickNumberIndex
    changeDataDialogVisible.value = true
    setTimeout(() => {
      changeDataRef.value?.changeOldValue(numbers.value[clickNumberIndex].value)
      changeDataRef.value?.resetChangeValue()
    }, 0)
  }
}

/**
 * 处理点击数字修改按钮
 */
function handleDialogConfirm() {
  changeDataRef.value?.handleChangeNumber()
  changeDataDialogVisible.value = false
}

/**
 * 退出游戏房间
 */
function handleExit() {
  gameOverDialogVisible.value = false
  gameStore.closeGame()
}

/**
 * 点击"退出房间"按钮
 */
function handleLeaveRoom() {
  // 如果不是房主，且当前正好是自己的回合，记录下剩余时间
  const isOwner = sessionCache.getCache(IS_OWNER)
  if (isTurn.value && !isOwner) {
    sessionCache.setCache(ROUND_RESIDUE_TIME, countDown.count + 1)
  }
  // 断开连接
  gameStore.closeGame()
}

/**
 * 再来一局
 */
function handleGameAgain() {
  gameOverDialogVisible.value = false
  gameStore.closeGame()
}

/**
 * 设置1s倒计时
 */
function setOneSecondTimer() {
  countDown.timer && clearInterval(countDown.timer)
  countDown.timer = setInterval(() => {
    const tmp = countDown.count--
    countDown.text = `${tmp}秒`
    if (tmp <= 0) {
      // 清除掉定时器
      clearInterval(countDown.timer)
      countDown.count = roundTime
      countDown.text = ''
      // 主动修改任意数字
      gameStore.modifyNumberWhenTimeout()
    }
  }, 1000)
}
watch(
  () => countDown.count,
  () => {
    if (countDown.count !== roundTime && status.value != 3) {
      setOneSecondTimer()
    } else {
      countDown.timer && clearInterval(countDown.timer)
    }
  }
)
</script>

<style lang="less" scoped>
.game-panel {
  width: 100%;
  margin-bottom: 100px;
  .title {
    text-align: center;
    font-size: 40px;
  }
}

.exitBtn {
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.numbers {
  display: flex;
  flex: 1;

  .number-item {
    flex: 1;
    margin-right: 100px;
    margin-top: 40px;
    margin-bottom: 60px;
  }
  .number-item:hover {
    background: #f6f6f6;
  }
  .number-item:last-child {
    margin-right: 0;
  }

  .course {
    font-weight: bold;
    display: inline-block;
    font-size: 45px;
  }
  .course-item {
    line-height: 40px;
    color: grey;
    margin-top: 20px;
    font-size: 26px;
  }

  .el-card {
    cursor: pointer;
  }
  .el-card:hover {
    background: #f6f6f6;
  }
}
</style>
