<template>
  <div class="nav-header">
    <template v-if="isAdmin">
      <el-icon @click="handleFoldClick">
        <component :is="isFold ? Fold : Expand" />
      </el-icon>
    </template>
    <template v-else>
      <el-icon><Operation /></el-icon>
    </template>
    <div class="content">
      <div></div>
      <div class="right">
        <el-row class="buttons">
          <span>
            <el-icon><ChatDotRound /></el-icon>
          </span>
          <span>
            <el-icon><CollectionTag /></el-icon>
          </span>
          <span>
            <span class="dot"></span>
            <el-icon><Bell /></el-icon>
          </span>
        </el-row>
        <user-info />
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { ElRow } from 'element-plus'
import { Expand, Fold } from '@element-plus/icons-vue'

import UserInfo from './user-info.vue'

const props = defineProps({
  isAdmin: {
    type: Boolean,
    default: false
  }
})
const isAdmin = ref<boolean>(props.isAdmin)
const emit = defineEmits(['foldChange'])

const isFold = ref<Boolean>(false)

const handleFoldClick = () => {
  isFold.value = !isFold.value
  emit('foldChange', isFold.value)
}
</script>
<style scoped lang="less">
.nav-header {
  display: flex;
  height: 48px;
  padding: 0 20px 0 0;
  flex: 1;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;

  & > i {
    display: flex;
    height: 100%;
    margin-right: 10px;
    font-size: 28px;
    cursor: pointer;
    align-items: center;
  }

  .content {
    display: flex;
    flex: 1;
    justify-content: space-between;
    align-items: center;
  }

  .right {
    display: flex;
    flex-direction: row;

    .buttons {
      margin-right: 10px;

      i,
      span {
        position: relative;
        display: flex;
        width: 40px;
        height: 100%;
        font-size: 20px;
        cursor: pointer;
        justify-content: center;
        align-items: center;
      }

      i:hover {
        background: #f6f6f6;
      }

      .dot {
        position: absolute;
        top: 6px;
        right: 6px;
        z-index: 10;
        width: 6px;
        height: 6px;
        background: red;
        border-radius: 100%;
      }
    }

    // .el-button {
    //   display: flex;
    //   width: 30px;
    //   height: 30px;
    //   min-height: 30px;
    //   justify-content: center;
    //   align-items: center;

    //   .is-circle {
    //     padding: 7px;
    //   }
    // }
  }
}
</style>
