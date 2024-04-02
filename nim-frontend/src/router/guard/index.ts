import router from '@/router'
import { createPermissionGuard } from './permission-guard'

export const setupRouterGuard = () => {
  // add router guard
  createPermissionGuard(router)
}
