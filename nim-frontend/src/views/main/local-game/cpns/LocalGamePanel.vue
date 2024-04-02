<template>
  <div class="local-game-panel">
    <el-button type="danger" class="exitBtn" size="large" @click="handleExit">
      退出游戏
    </el-button>
    <h1 class="title">
      {{ gameTitle }}
    </h1>
    <h1>{{ '第' + round + '回合' }}</h1>
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
        {{ winner }}
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
import { ref, reactive, watch, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

import router from '@/router'
import appStore from '@/stores'
import { ROUND_MAX_TIME } from '@/constant/game_config'
import ChangeData from './ChangeData.vue'

interface ICountDown {
  count: number
  text: string
  timer: any
}

const roundTime =
  appStore.useLocalGameStore.gameConfig.roundTime ?? ROUND_MAX_TIME

let countDown = reactive<ICountDown>({
  count: roundTime,
  text: '',
  timer: void 0
})

const changeDataRef = ref<InstanceType<typeof ChangeData>>()
const changeDataDialogVisible = ref(false)

let gameTitle = ref<string>('游戏开始')
let currentProcessNumberIndex = ref(0)
const gameOverDialogVisible = ref(false)

const { numbers, round, isTurn, status, winner } = storeToRefs(
  appStore.useLocalGameStore
)

// 房间状态修改监视
watch(
  () => [status.value, isTurn.value],
  () => {
    switch (status.value) {
      case 0:
        gameTitle.value = '房间未使用'
        break
      case 1:
        gameTitle.value = '等待玩家加入房间'
        break
      case 2:
        if (isTurn.value) {
          gameTitle.value = '你的回合'
          countDown.count = roundTime - 1
        } else {
          gameTitle.value = '机器回合'
          countDown.count = roundTime
        }
        break
      case 3:
        gameTitle.value = '游戏暂停中...'
        countDown.count = roundTime
        break
      case 4:
        gameTitle.value = '游戏结束'
        countDown.count = roundTime
        if (winner.value !== '') {
          gameOverDialogVisible.value = true
        }
        break
    }
  }
)

// 游戏结束监视
watch(
  () => winner.value,
  () => {
    if (winner) {
      gameOverDialogVisible.value = true
    }
  }
)

/**
 * 点击数字
 */
function clickNumber(clickNumberIndex: number) {
  if (!isTurn.value) {
    return
  }
  // 判断数字是否为0
  if (numbers.value[clickNumberIndex].value === 0) {
    ElMessage.warning('当前数字已减为0, 无法再操作')
    return
  }
  // 显示数字对话框
  currentProcessNumberIndex.value = clickNumberIndex
  changeDataDialogVisible.value = true
  setTimeout(() => {
    changeDataRef.value?.formInit(
      String(clickNumberIndex),
      numbers.value[clickNumberIndex].value
    )
  }, 0)
}

/**
 * 处理数字修改
 */
function handleDialogConfirm() {
  if (!changeDataRef.value?.handleChangeNumber()) {
    ElMessage.error('修改数字失败，请重试')
    return
  }
  changeDataDialogVisible.value = false
  if (appStore.useLocalGameStore.checkWinner()) {
    status.value = 4
    winner.value = '恭喜你赢下本局游戏'
  } else {
    appStore.useLocalGameStore.machineOperate()
  }
}

/**
 * 退出游戏
 */
function handleExit() {
  gameOverDialogVisible.value = false
  router.replace('/main')
}

/**
 * 再来一局
 */
function handleGameAgain() {
  gameOverDialogVisible.value = false
  appStore.useLocalGameStore.startLocalGame()
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
      appStore.useLocalGameStore.modifyNumberWhenTimeout()
    }
  }, 1000)
}
// 计时器状况监视
watch(
  () => countDown.count,
  () => {
    if (countDown.count !== roundTime && status.value != 3 && isTurn.value) {
      setOneSecondTimer()
    } else {
      countDown.timer && clearInterval(countDown.timer)
    }
  }
)

onMounted(() => {
  appStore.useLocalGameStore.startLocalGame()
})
</script>

<style lang="less" scoped>
.local-game-panel {
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
