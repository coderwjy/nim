import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import path from 'path'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      // '^/dev': {
      //   target: 'http://nim.coderwjy.icu:8472/nim',
      //   changeOrigin: true,
      //   ws: true,
      //   rewrite: (path) => path.replace(/^\/dev/, '')
      // }
      '^/dev': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        ws: true
      }
    }
  },
  plugins: [
    vue(),
    vueJsx(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          // 将pinia的全局库实例打包进vendor，避免和页面一起打包造成资源重复引入
          if (id.includes(path.resolve(__dirname, '/src/store/index.ts'))) {
            return 'vendor'
          }
        }
      }
    }
  }
})
