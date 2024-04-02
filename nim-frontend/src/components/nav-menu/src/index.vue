<template>
  <div class="sile-menu">
    <div class="logo">
      <img class="img" src="https://cn.vitejs.dev/logo.svg" alt="logo" />
      <span v-show="!isCollapse" class="title">Vue3Admin</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
// import { ElMenu, ElSubmenu, ElMenuItem } from 'element-plus'

const props = defineProps({
  collapse: {
    type: Boolean,
    default: false
  }
})

const isCollapse = ref<Boolean>(props.collapse)

watch(
  () => props.collapse,
  () => {
    isCollapse.value = props.collapse
  }
)
</script>

<style scoped lang="less">
@import '@/assets/css/_var.less';
// 混合
.selectActiveColor {
  color: white !important;
}

.sile-menu {
  height: 100%;
  // width: 100%;

  // logo 布局
  .logo {
    display: flex;
    height: 28px;
    padding: 12px 10px 8px 10px;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;

    .img {
      height: 100%;
      margin: 0 10px;
    }

    .title {
      font-size: 16px;
      font-weight: 700;
      color: white;
    }
  }

  .el-menu-vertical {
    // 没有设置100%, 右边会突出（因为子布局比父亲大）
    width: 100%;
    border-right: none;

    // 目录
    .el-submenu {
      // 二级菜单 ( 默认背景 )
      .el-menu-item {
        padding-left: 50px !important;
        background-color: @side-lighten-bg-color !important;
      }
    }

    // hover 高亮
    .el-menu-item:hover {
      .selectActiveColor(); // 菜单
    }

    .el-menu-item:hover i::before {
      .selectActiveColor(); // 菜单 icon
    }

    .el-submenu__title:hover span {
      .selectActiveColor(); // 目录
    }

    .el-submenu__title:hover i::before {
      .selectActiveColor(); // 目录 icon
    }

    // 二级菜单选中
    .el-menu-item.is-active {
      color: white !important;
      background-color: @side-sel-bg-color !important;
    }
  }
}
</style>
