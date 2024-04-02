import axios from 'axios'
import type { AxiosInstance } from 'axios'
import type { RequestInterceptors, RequestConfig } from './type'
import { ElLoading, ElMessage } from 'element-plus'
import { DEFAULT_SHOW_LOADING } from './config'

class Request {
  instance: AxiosInstance
  interceptors?: RequestInterceptors
  showLoding: boolean
  loading?: { close: () => void }

  constructor(config: RequestConfig) {
    // 创建 axios 实例 (允许创建多个不同实例对象)
    this.instance = axios.create(config)

    // 保存 config 中基本信息
    this.showLoding = config.showLoding ?? DEFAULT_SHOW_LOADING // 是否显示 loading 动画
    this.interceptors = config.interceptors // 拦截器

    // 所有实例的拦截器
    this.instance.interceptors.request.use(
      (config) => {
        if (this.showLoding) {
          this.loading = ElLoading.service({
            lock: true,
            text: '正在请求数据...',
            background: 'rgba(0, 0, 0, 0.5)'
          })
        }
        return config
      },
      (err) => {
        ElMessage.error(err)
        return err
      }
    )
    this.instance.interceptors.response.use(
      (res) => {
        this.loading?.close() // 收到响应结果后，关闭加载动画
        return res
      },
      (err) => {
        this.loading?.close() // 请求失败，关闭加载动画
        // 根据 HttpErrorCode 显示不同的错误信息
        throw new Error(err)
      }
    )

    // 当前单个实例的拦截器 (实例创建时传入)
    this.instance.interceptors.request.use(
      this.interceptors?.requestInterceptor,
      this.interceptors?.requestInterceptorCatch
    )
    this.instance.interceptors.response.use(
      this.interceptors?.responseInterceptor,
      this.interceptors?.responseInterceptorCatch
    )
  }

  request<T>(config: RequestConfig<T>): Promise<T> {
    return new Promise((resolve, reject) => {
      // 单个请求的拦截器 (这里一般不拦截错误)
      if (config.interceptors?.requestInterceptor) {
        config = config.interceptors.requestInterceptor(config)
      }
      // 处理是否显示加载动画
      if (config.showLoding === false && this.showLoding) {
        this.showLoding = false
      } else if (config.showLoding && !this.showLoding) {
        this.showLoding = true
      }
      // 发送请求
      this.instance
        .request<any, T>(config)
        .then((res) => {
          // 单个响应的拦截器
          if (config.interceptors?.responseInterceptor) {
            res = config.interceptors.responseInterceptor(res)
          }
          this.showLoding = DEFAULT_SHOW_LOADING // 还原默认的加载动画选项
          resolve(res)
        })
        .catch((err) => {
          this.showLoding = DEFAULT_SHOW_LOADING // 还原默认的加载动画选项
          reject(err)
        })
    })
  }

  get<T>(config: RequestConfig<T>): Promise<T> {
    return this.request<T>({ ...config, method: 'GET' })
  }

  post<T>(config: RequestConfig<T>): Promise<T> {
    return this.request<T>({ ...config, method: 'POST' })
  }

  delete<T>(config: RequestConfig<T>): Promise<T> {
    return this.request<T>({ ...config, method: 'DELETE' })
  }

  patch<T>(config: RequestConfig<T>): Promise<T> {
    return this.request<T>({ ...config, method: 'PATCH' })
  }
}

export default Request
