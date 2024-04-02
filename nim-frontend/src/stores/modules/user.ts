import { ref } from 'vue'
import { defineStore } from 'pinia'
import { ElMessage } from 'element-plus'

import * as userService from '@/service/user'
import router from '@/router'
import type {
  IUserLoginRequest,
  IUserInfo,
  IUserRegistRequest,
  IUserRegistResponse
} from '@/service/user/type'
import { localCache } from '@/utils/cache'
import {
  USER_INFO_KEY,
  TOKEN_KEY,
  ROOM_ID,
  USER_ACCOUNT
} from '@/constant/cache_keys'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>('')
  const userInfo = ref<IUserInfo>({})

  function setToken(newToken: string) {
    token.value = newToken
  }

  function setUserInfo(newUserInfo: IUserInfo) {
    userInfo.value = newUserInfo
  }

  async function userLogin(loginRequestParams: IUserLoginRequest) {
    const res = await userService.userLogin(loginRequestParams)
    if (res?.userAccount) {
      // 存储用户信息
      userInfo.value = res
      // 获取用户房间号
      const roomId = await userService.getUserRoomId(res.userAccount)
      // 设置本地缓存
      localCache.setCache(USER_INFO_KEY, userInfo.value)
      token.value = '3CE7dleY8GQaMGe3UlayMvRaY61dIeTonzm9O0krfO6'
      localCache.setCache(TOKEN_KEY, token.value) // todo:后续改为后端获取
      localCache.setCache(ROOM_ID, roomId)
      // 弹出欢迎信息
      const welcomeName = userInfo.value?.nickname ?? userInfo.value.userAccount
      ElMessage.success(`登录成功，欢迎${welcomeName}`)
      await router.replace('/main')
      return true
    } else {
      ElMessage.error('登录失败，请检查账户密码')
      return false
    }
  }

  async function userRegister(registerRequestParams: IUserRegistRequest) {
    const registerResponse: IUserRegistResponse =
      await userService.userRegister(registerRequestParams)
    if (registerResponse && registerResponse?.userId) {
      // 注册成功，将用户账号写入缓存
      localCache.setCache(USER_ACCOUNT, registerResponse.userAccount)
      return registerResponse
    }
  }

  function loadLocalLogin() {
    const token = localCache.getCache(TOKEN_KEY)
    if (token) {
      setToken(token)
    }
    const userInfo = localCache.getCache(USER_INFO_KEY)
    if (userInfo) {
      setUserInfo(userInfo)
    }
  }

  return { token, userInfo, userLogin, userRegister, loadLocalLogin }
})
