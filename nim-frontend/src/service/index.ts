import { ElMessage } from 'element-plus'
import Request from './request'
import { BASE_URL, TIME_OUT } from './request/config'

const request = new Request({
  baseURL: BASE_URL,
  timeout: TIME_OUT,
  interceptors: {
    requestInterceptor: (config) => {
      // 请求携带 token
      const token = '' // 这里从 vuex 中获取，或从 localStorage 中拿
      if (token && config?.headers) {
        config.headers.Authorization = `Bearer ${token}`
      }
      return config
    },
    requestInterceptorCatch: (error) => {
      return error
    },
    responseInterceptor: (res) => {
      const data = res.data
      if (data?.code !== 0) {
        ElMessage.error(data?.description)
        throw new Error(data?.description)
      }
      return data?.data
    },
    responseInterceptorCatch: (error) => {
      ElMessage.error(error)
      return error
    }
  }
})

export { request }
