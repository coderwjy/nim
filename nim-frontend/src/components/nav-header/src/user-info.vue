<template>
  <div>
    <el-dropdown @command="handleDropDownClick">
      <span class="el-dropdown-link">
        <el-avatar :size="30" :src="circleUrl"></el-avatar>
        <span class="name">{{
          userInfo?.nickname || userInfo.userAccount
        }}</span>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="退出系统">退出系统</el-dropdown-item>
          <!-- <el-dropdown-item>个人中心</el-dropdown-item> -->
          <!-- <el-dropdown-item>修改密码</el-dropdown-item> -->
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="ts" setup>
import {
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElAvatar
} from 'element-plus'
import appStore from '@/stores'
import router from '@/router'

import { USER_ACCOUNT, USER_PASSWORD } from '@/constant/cache_keys'
import { localCache } from '@/utils/cache'

const { userInfo } = appStore.useUserStore
const circleUrl =
  'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const handleDropDownClick = () => {
  // 退出登录
  clearLocalStorage()
  // 跳到登录页面
  router.push('/login')
}

const clearLocalStorage = () => {
  // 记录下保存的账号密码
  const cacheUserAccount = localCache.getCache(USER_ACCOUNT)
  const cacheUserPassword = localCache.getCache(USER_PASSWORD)
  // 清空本地缓存
  localCache.clearCache()
  // 重新赋值上账号密码
  if (cacheUserAccount) {
    localCache.setCache(USER_ACCOUNT, cacheUserAccount)
  }
  if (cacheUserPassword) {
    localCache.setCache(USER_PASSWORD, cacheUserPassword)
  }
}
</script>

<style scoped lang="less">
.el-dropdown-link {
  display: flex;
  cursor: pointer;
  align-items: center;

  .name {
    margin-left: 10px;
  }
}
</style>
