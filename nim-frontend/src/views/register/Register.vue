<template>
  <div class="register">
    <RegisterPanel />
  </div>
</template>

<script lang="ts" setup>
import RegisterPanel from './cpns/RegisterPanel.vue'
</script>

<style lang="less" scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: url('@/assets/img/login-bg.svg');
}
</style>
