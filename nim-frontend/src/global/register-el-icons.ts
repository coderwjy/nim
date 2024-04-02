import type { App } from 'vue'
import {
  Avatar,
  Bell,
  ChatDotRound,
  CollectionTag,
  Expand,
  Fold,
  Iphone,
  Operation,
  ArrowLeft,
  WarningFilled
} from '@element-plus/icons-vue'

const icons = [
  Avatar,
  Iphone,
  Fold,
  Expand,
  ChatDotRound,
  CollectionTag,
  Bell,
  Operation,
  ArrowLeft,
  WarningFilled
]

export default function (app: App): void {
  for (const icon of icons) {
    app.component(icon.name, icon)
  }
}
