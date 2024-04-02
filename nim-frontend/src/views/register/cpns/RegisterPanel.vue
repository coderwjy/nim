<template>
  <div class="login-panel">
    <h1 class="title">用户注册</h1>
    <div class="back-login">
      <el-button type="info" size="small" @click="backToLogin">
        返回登录
      </el-button>
    </div>
    <el-tabs type="border-card" class="el-tabs" stretch>
      <RegisterAccount ref="registerAccountRef" />
    </el-tabs>

    <el-button
      type="primary"
      class="register-button"
      @click="handleRegisterClick"
    >
      注册
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import router from '@/router'
import RegisterAccount from './RegisterAccount.vue'

const registerAccountRef = ref<InstanceType<typeof RegisterAccount>>()

function handleRegisterClick() {
  registerAccountRef.value?.registerAction()
}

function backToLogin() {
  router.replace('/login')
}
</script>

<style scoped lang="less">
.login-panel {
  width: 320px;
  margin-bottom: 150px;
  .title {
    text-align: center;
    margin-bottom: 10px;
  }
  .back-login {
    margin-bottom: 5px;
    text-align: right;
  }
}
.el-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}
.el-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}
.register-button {
  width: 100%;
  margin-top: 10px;
}
</style>
