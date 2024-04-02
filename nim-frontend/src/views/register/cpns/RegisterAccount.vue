<template>
  <div class="register">
    <el-form
      ref="registerFormRef"
      :model="registerForm"
      label-position="left"
      status-icon
      :rules="rules"
      label-width="80px"
      class="demo-ruleForm"
      @keyup.enter="registerAction"
    >
      <el-form-item label="账号" prop="userAccount">
        <el-input
          v-model="registerForm.userAccount"
          type="text"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="密码" prop="userPassword">
        <el-input
          v-model="registerForm.userPassword"
          type="password"
          show-password
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item label="重复密码" prop="checkPassword">
        <el-input
          v-model="registerForm.checkPassword"
          type="password"
          show-password
          autocomplete="off"
        />
      </el-form-item>
      <!-- <el-form-item>
        <el-button type="primary" @click="handleRegister">注册</el-button>
        <el-button @click="registerAction">重置</el-button>
      </el-form-item> -->
    </el-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import router from '@/router'
import { type FormInstance, ElMessage } from 'element-plus'

import appStore from '@/stores'
import type { IUserRegistRequest } from '@/service/user/type'
import { rules } from '../config/account-register-config'

const registerFormRef = ref<FormInstance>()
const registerForm = reactive<IUserRegistRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

function registerAction() {
  registerFormRef.value?.validate(async (valid) => {
    if (valid) {
      const registerInfo = await appStore.useUserStore.userRegister(
        registerForm
      )
      if (registerInfo) {
        ElMessage.success(`用户 ${registerInfo.userAccount} 注册成功!`)
        await router.push('/login') // 跳转到登录界面
      }
    }
  })
}
// const formReset = () => {
//   registerFormRef.value?.resetFields()
// }

defineExpose({ registerAction })
</script>

<style lang="less" scoped></style>
