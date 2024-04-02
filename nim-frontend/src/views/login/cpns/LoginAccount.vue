<template>
  <div class="login-account">
    <el-form
      ref="formRef"
      :model="account"
      :rules="rules"
      status-icon
      label-width="60px"
    >
      <el-form-item label="账号" prop="userAccount">
        <el-input
          v-model="account.userAccount"
          placeholder="请输入您的账号"
          :maxlength="ACCOUNT_MAX_LEN"
          @keyup.enter="keyUpLogin"
        />
      </el-form-item>
      <el-form-item label="密码" prop="userPassword">
        <el-input
          v-model="account.userPassword"
          placeholder="请输入您的密码"
          :maxlength="PASSWORD_MAX_LEN"
          show-password
          @keyup.enter="keyUpLogin"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'

import type { IUserLoginRequest } from '@/service/user/type'
import appStore from '@/stores'
import { localCache } from '@/utils/cache'
import { USER_ACCOUNT, USER_PASSWORD } from '@/constant/cache_keys'
import { rules } from '../config/account-config'
import { ACCOUNT_MAX_LEN, PASSWORD_MAX_LEN } from '@/constant/account_rules'
import { encryption, decryption } from '@/utils/encryption'

const emits = defineEmits(['handleLoginClick'])

const formRef = ref<FormInstance>()
const account = reactive<IUserLoginRequest>({
  userAccount: localCache.getCache(USER_ACCOUNT) ?? '', // 账号
  userPassword: decryption(localCache.getCache(USER_PASSWORD) ?? '') // 密码
})

function loginAction(isKeepPassword: boolean) {
  formRef.value?.validate((valid) => {
    if (valid) {
      // 1.判断是否需要记住密码
      if (isKeepPassword) {
        localCache.setCache(USER_ACCOUNT, account.userAccount)
        localCache.setCache(USER_PASSWORD, encryption(account.userPassword))
      } else {
        localCache.deleteCache(USER_ACCOUNT)
        localCache.deleteCache(USER_PASSWORD)
      }
      // 2.登录
      appStore.useUserStore.userLogin(account)
    }
  })
}

function keyUpLogin() {
  emits('handleLoginClick')
}

defineExpose({ loginAction })
</script>

<style scoped></style>
