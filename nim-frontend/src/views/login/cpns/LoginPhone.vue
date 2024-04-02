<template>
  <div class="login-phone">
    <el-form
      ref="formRef"
      :model="phone"
      :rules="rules"
      status-icon
      label-width="60px"
    >
      <el-form-item label="手机号" label-width="66px" prop="number">
        <el-input
          v-model="phone.number"
          placeholder="请输入您的手机号"
          oninput="value=value.replace(/\D/g,'')"
        />
      </el-form-item>
      <el-form-item label="验证码" label-width="66px" prop="code">
        <div class="verify-code">
          <el-input
            v-model="phone.code"
            placeholder="请输入验证码"
            type="number"
            oninput="if (value.length > 6) value=value.slice(0, 6)"
          />
          <el-button
            type="primary"
            class="verify-code-btn"
            @click="handleVerifyCodeGet"
          >
            获取验证码
          </el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'

import { rules } from '../config/phone-config'

const formRef = ref<FormInstance>()
const phone = reactive({
  number: '', // 手机号
  code: '' // 验证码
})

const loginAction = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      ElMessage.error('暂未开通手机登录功能')
    }
  })
}
const handleVerifyCodeGet = () => {
  ElMessage.error('暂未开通手机登录功能')
}

defineExpose({ loginAction })
</script>

<style scoped lang="less">
.verify-code {
  display: flex;

  .el-button {
    margin-left: 10px;
  }

  :deep(input::-webkit-outer-spin-button),
  :deep(input::-webkit-inner-spin-button) {
    -webkit-appearance: none;
  }
  :deep(input[type='number']) {
    -moz-appearance: textfield;
  }
}
</style>
