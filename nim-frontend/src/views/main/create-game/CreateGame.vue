<template>
  <el-button
    type="warning"
    :icon="ArrowLeft"
    style="float: left"
    @click="backBtnClick"
  >
    返回首页
  </el-button>
  <div class="create-game">
    <el-card class="create-game-item" @click="createRoomDialogVisible = true">
      <template #header>
        <div class="card-header">
          <span class="course">创建房间</span>
        </div>
      </template>
      <div class="item">
        <span class="course-item course-item-first">创建个人房间</span>
      </div>
      <div class="item"><span class="course-item">邀请你的好友</span></div>
      <div class="item">
        <span class="course-item">一起加入精彩的对局吧</span>
      </div>
    </el-card>
    <el-card class="create-game-item" @click="joinRoomDialogVisible = true">
      <template #header>
        <div class="card-header">
          <span class="course">加入房间</span>
        </div>
      </template>
      <div class="item">
        <span class="course-item course-item-first"> 请输入房间号 </span>
      </div>
      <div class="item"><span class="course-item">加入好友房间</span></div>
      <div class="item">
        <span class="course-item">进行一场精彩的对局吧</span>
      </div>
    </el-card>

    <!-- 创建房间对话框 -->
    <el-dialog
      v-model="createRoomDialogVisible"
      title="房间设置"
      width="30%"
      :before-close="handleCreateRoomDialogClose"
    >
      <CreateRoom ref="createRoomRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createRoomDialogVisible = false"> 取消 </el-button>
          <el-button type="primary" @click="handleCreateRoom"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 加入房间对话框 -->
    <el-dialog
      v-model="joinRoomDialogVisible"
      title="加入房间"
      width="30%"
      :before-close="handleJoinRoomDialogClose"
    >
      <JoinRoom ref="joinRoomRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="joinRoomDialogVisible = false"> 取消 </el-button>
          <el-button type="primary" @click="handleJoinRoom"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessageBox } from 'element-plus'

import { sessionCache } from '@/utils/cache'
import { IS_OWNER } from '@/constant/cache_keys'
import CreateRoom from './cpns/CreateRoom.vue'
import JoinRoom from './cpns/JoinRoom.vue'
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回首页按钮
const backBtnClick = () => {
  router.replace({
    path: '/'
  })
}

// 创建房间 / 加入房间 对话框是否弹出
const createRoomDialogVisible = ref<boolean>(false)
const joinRoomDialogVisible = ref<boolean>(false)

// 创建房间 / 加入房间 表单ref
const createRoomRef = ref<InstanceType<typeof CreateRoom>>()
const joinRoomRef = ref<InstanceType<typeof JoinRoom>>()

// 创建房间
const handleCreateRoomDialogClose = (done: () => void) => {
  ElMessageBox.confirm('关闭创建房间界面?')
    .then(() => {
      done()
    })
    .catch(() => {
      // catch error
    })
}
const handleCreateRoom = () => {
  // 关闭游戏配置对话框
  createRoomDialogVisible.value = false
  // 将是否为房主的信息写入sessionStorage
  sessionCache.setCache(IS_OWNER, true)
  // 调用创建房间函数
  createRoomRef.value?.createRoomAction()
}

// 加入房间
const handleJoinRoomDialogClose = (done: () => void) => {
  ElMessageBox.confirm('关闭加入房间界面?')
    .then(() => {
      done()
    })
    .catch(() => {
      // catch error
    })
}
const handleJoinRoom = () => {
  // 关闭加入房间对话框
  joinRoomDialogVisible.value = false
  // 将是否为房主的信息写入sessionStorage
  sessionCache.setCache(IS_OWNER, false)
  // 调用加入房间函数
  joinRoomRef.value?.joinRoomAction()
}
</script>

<style lang="less" scoped>
.create-game {
  display: flex;
  flex: 1;
  margin-left: 240px;

  .create-game-item {
    flex: 1;
    margin-right: 240px;
    margin-top: 150px;
    margin-bottom: 145px;
  }
  .create-game-item:hover {
    background: #f6f6f6;
  }

  .create-game-item:last-child {
    margin-right: 0;
  }

  .course {
    font-weight: bold;
    display: inline-block;
    font-size: 50px;
  }
  .course-item {
    line-height: 36px;
    color: grey;
    margin-top: 20px;
    font-size: 20px;
  }
  .course-item-first {
    font-size: 25px;
    color: #409eff;
    line-height: 100px;
  }

  .el-card {
    cursor: pointer;
  }
}
</style>
