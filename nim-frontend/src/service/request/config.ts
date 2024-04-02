/**
 * axios 配置
 * @author coderwjy
 */

let BASE_URL: string // 请求路径头 (根据当前环境配置)
const TIME_OUT = 3600 // 最大请求时延
const DEFAULT_SHOW_LOADING = true // 是否在发送请求时开启加载动画

if (process.env.NODE_ENV === 'development') {
  BASE_URL = '/dev'
} else if (process.env.NODE_ENV === 'production') {
  BASE_URL = '/nim-front'
} else {
  BASE_URL = '/test'
}

export { BASE_URL, TIME_OUT, DEFAULT_SHOW_LOADING }
