<template>
  <div class="game-menu">
    <div class="title-div">
      <span class="title-span">夺标</span>
    </div>
    <div class="menu-div">
      <div class="fill-box"></div>
      <el-card class="menu">
        <template #header>
          <div class="card-header">
            <span class="menu-header-span">游戏菜单</span>
          </div>
        </template>
        <div
          class="menu-item"
          @click="createLocalGameDialogVisible = !createLocalGameDialogVisible"
        >
          <span class="menu-item-span">单机模式</span>
        </div>
        <div class="menu-item" @click="toOnlineGame">
          <span class="menu-item-span">在线模式</span>
        </div>
        <div class="menu-item" @click="gameIntroduce">
          <span class="menu-item-span">游戏简介</span>
        </div>
      </el-card>
      <div class="fill-box"></div>
    </div>

    <!-- 创建房间对话框 -->
    <el-dialog
      v-model="createLocalGameDialogVisible"
      title="房间设置"
      width="30%"
      :before-close="handleCreateLocalGameDialogClose"
    >
      <CreateLocalGame ref="createLocalGameRef" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createLocalGameDialogVisible = false">
            取消
          </el-button>
          <el-button type="primary" @click="handleCreateLocalGame">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import router from '@/router'
import CreateLocalGame from './cpns/CreateLocalGame.vue'

// 创建本地房间对话框是否弹出
const createLocalGameDialogVisible = ref<boolean>(false)

// 创建本地房间表单ref
const createLocalGameRef = ref<InstanceType<typeof CreateLocalGame>>()

// 创建本地房间
const handleCreateLocalGameDialogClose = (done: () => void) => {
  ElMessageBox.confirm('关闭创建房间界面?')
    .then(() => {
      done()
    })
    .catch(() => {
      // catch error
    })
}
const handleCreateLocalGame = () => {
  createLocalGameDialogVisible.value = false
  createLocalGameRef.value?.createLocalGameAction()
}

// 跳转到在线游戏
const toOnlineGame = () => {
  router.replace('/main/online')
}

// 游戏简介
const gameIntroduce = () => {
  ElMessage.info(
    '双方玩家每回合可减少任意卡片值，首先将所有卡片堆减至0的玩家获胜'
  )
}
</script>

<style lang="less" scoped>
@page-height: calc(100vh - 90px);
@title-height: 280px;
@menu-height: calc(@page-height - @title-height);

@menu-item-height: 50px;

.game-menu {
  height: @page-height;
  background: url('@/assets/img/login-bg.svg');

  .title-div {
    display: block;
    height: @title-height;

    .title-span {
      line-height: @title-height;
      font-size: 96px;
      font-weight: bold;
    }
  }

  .menu-div {
    display: flex;
    flex: 1;
    height: @menu-height;

    .fill-box {
      flex: 1;
    }

    .menu {
      flex: 1;
      margin-bottom: 20px;

      .menu-header-span {
        font-weight: bold;
        display: inline-block;
        font-size: 50px;
      }

      .menu-item {
        cursor: pointer;
        height: @menu-item-height;
        border: 3px solid #b7bdc3;
        margin-top: 14px;
      }
      .menu-item:active {
        background: #b7bdc3;
      }
      .menu-item:hover {
        background: #f6f6f6;
      }

      .menu-item-span {
        line-height: @menu-item-height;
        color: grey;
        margin-top: 20px;
        font-size: 24px;
      }
    }
  }
}
</style>
