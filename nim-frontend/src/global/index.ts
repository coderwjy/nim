import type { App } from 'vue'
import registerElComponents from './register-el-cpns'
import registerElIcons from './register-el-icons'

export function globalRegister(app: App): void {
  app.use(registerElComponents) // register element-plus components (auto import)
  app.use(registerElIcons) // register element-plus icons (auto import)
}
