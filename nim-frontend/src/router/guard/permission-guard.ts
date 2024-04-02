import type { Router } from 'vue-router'
import { localCache } from '@/utils/cache'
import { TOKEN_KEY } from '@/constant/cache_keys'

/**
 * 路由守卫 - 监听授权页面的授权
 * @param router 路由对象
 */
export const createPermissionGuard = (router: Router): void => {
  router.beforeEach((to) => {
    // login page
    if (to.path !== '/login' && to.path !== '/register') {
      const token = localCache.getCache(TOKEN_KEY)
      // is login
      if (!token || token === 'undefined') {
        return '/login'
      }
    }
  })
}
