<template>
  <div class="login-panel">
    <h1 class="title">用户登录</h1>
    <el-tabs type="border-card" class="el-tabs" v-model="activeTabName" stretch>
      <el-tab-pane name="account">
        <template #label>
          <span class="custom-tabs-label">
            <el-icon><Avatar /></el-icon>
            <span>账号密码登录</span>
          </span>
        </template>
        <LoginAccount
          ref="loginAccountRef"
          @handleLoginClick="handleLoginClick"
        />
      </el-tab-pane>
      <el-tab-pane name="phone">
        <template #label>
          <span class="custom-tabs-label">
            <el-icon><Iphone /></el-icon>
            <span>手机验证登录</span>
          </span>
        </template>
        <LoginPhone ref="loginPhoneRef" />
      </el-tab-pane>
    </el-tabs>

    <div class="account-control">
      <el-checkbox v-model="isKeepPassword">记住密码</el-checkbox>
      <div @click="handleRegister">
        <span class="register-text">注册</span>
      </div>
      <!-- <el-link
        :underline="false"
        type="primary"
        href="https://github.com/coderwjy"
        target="_blank"
        >忘记密码
      </el-link> -->
    </div>

    <el-button type="primary" class="login-button" @click="handleLoginClick">
      立即登录
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

import router from '@/router'

import LoginAccount from './LoginAccount.vue'
import LoginPhone from './LoginPhone.vue'

const isKeepPassword = ref(true)
const loginAccountRef = ref<InstanceType<typeof LoginAccount>>()
const loginPhoneRef = ref<InstanceType<typeof LoginPhone>>()
const activeTabName = ref<string>('account')

const handleLoginClick = () => {
  if (activeTabName.value === 'account') {
    loginAccountRef.value?.loginAction(isKeepPassword.value)
  } else {
    loginPhoneRef.value?.loginAction()
  }
}

const handleRegister = () => {
  router.push('/register')
}
</script>

<style scoped lang="less">
.login-panel {
  width: 320px;
  margin-bottom: 150px;
  .title {
    text-align: center;
  }
}
.el-tabs .custom-tabs-label .el-icon {
  vertical-align: middle;
}
.el-tabs .custom-tabs-label span {
  vertical-align: middle;
  margin-left: 4px;
}
.account-control {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;

  .register-text {
    text-align: center;
    line-height: 32px;
    font-size: 14px;
    font-weight: bold;
    color: #5a9cf9;
    cursor: pointer;
  }
}
.login-button {
  width: 100%;
  margin-top: 10px;
}
</style>
