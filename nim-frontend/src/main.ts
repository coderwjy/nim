import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'element-plus/theme-chalk/index.css'

import './assets/css/index.less'
import App from './App.vue'
import router from './router'
import { globalRegister } from './global'
import { registerStore } from '@/stores'
import { setupRouterGuard } from '@/router/guard'

const app = createApp(App)

app.use(createPinia())
app.use(router)

setupRouterGuard()
app.use(globalRegister) // 按需引入element-plus组件
registerStore() // 注册pinia状态管理库

app.mount('#app')
