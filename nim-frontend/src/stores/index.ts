import { useUserStore } from './modules/user'
import { useGameStore } from './modules/game'
import { useLocalGameStore } from '@/stores/modules/local-game'

export interface IAppStore {
  useUserStore: ReturnType<typeof useUserStore>
  useGameStore: ReturnType<typeof useGameStore>
  useLocalGameStore: ReturnType<typeof useLocalGameStore>
}

const appStore: IAppStore = {} as IAppStore

/**
 * 注册 app 状态库
 */
export const registerStore = () => {
  appStore.useUserStore = useUserStore()
  appStore.useGameStore = useGameStore()
  appStore.useLocalGameStore = useLocalGameStore()

  appStore.useUserStore.loadLocalLogin() // 加载localStorage中的存储内容
}

export default appStore
