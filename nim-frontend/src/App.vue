<script setup lang="ts">
import { ElConfigProvider } from 'element-plus'
</script>

<template>
  <ElConfigProvider size="default" :z-index="2000">
    <div class="app">
      <router-view></router-view>
    </div>
  </ElConfigProvider>
</template>

<style scoped>
.app {
  height: 100%;
}
</style>
