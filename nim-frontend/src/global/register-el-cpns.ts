import type { App } from 'vue'
import {
  ElButton,
  ElForm,
  ElFormItem,
  ElTabPane,
  ElTabs,
  ElIcon,
  ElInput,
  ElInputNumber,
  ElCheckbox,
  ElLink,
  ElCard
} from 'element-plus'

const components = [
  ElButton,
  ElForm,
  ElFormItem,
  ElTabs,
  ElTabPane,
  ElIcon,
  ElInput,
  ElInputNumber,
  ElCheckbox,
  ElLink,
  ElCard
]

export default function (app: App): void {
  for (const component of components) {
    app.component(component.name, component)
  }
}
