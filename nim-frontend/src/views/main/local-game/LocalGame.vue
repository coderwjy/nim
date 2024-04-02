<template>
  <div class="local-game">
    <LocalGamePanel />
  </div>
</template>

<script lang="ts" setup>
import LocalGamePanel from './cpns/LocalGamePanel.vue'
</script>

<style lang="less" scoped>
.local-game {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: url('@/assets/img/login-bg.svg');
}
</style>
