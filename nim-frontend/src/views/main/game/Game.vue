<template>
  <div class="game">
    <GamePanel />
  </div>
</template>

<script lang="ts" setup>
import GamePanel from './cpns/GamePanel.vue'
</script>

<style lang="less" scoped>
.game {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: url('@/assets/img/login-bg.svg');
}
</style>
