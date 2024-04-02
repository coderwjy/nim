import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

import type {
  IOpenGameWebSocketRequest,
  IGameWebSocketResponse,
  IMessageGameWebSocketRequest
} from './type'
import appStore from '@/stores'
import router from '@/router'
import { HOST, PORT, PREFIX } from '@/constant/server'

class GameWebSocket {
  socket: WebSocket

  constructor(openConfig: IOpenGameWebSocketRequest) {
    // 建立WebSocket连接
    let requestInfo = JSON.stringify(openConfig)
    requestInfo = requestInfo.substring(1, requestInfo.length - 1)
    this.socket = new WebSocket(
      `ws://${HOST}:${PORT}${PREFIX}/websocket/${requestInfo}`
    )
    // 设置监听器
    this.socket.onmessage = function (event: MessageEvent) {
      const data: IGameWebSocketResponse = JSON.parse(event.data)
      // 验证destination
      const { userInfo } = appStore.useUserStore
      if (
        data.destination !== userInfo.userAccount &&
        data.destination !== 'ALL' &&
        data.destination !== 'BATCH'
      ) {
        return
      }

      // 判断操作是否错误
      if (data.isError) {
        ElMessage.error(data.errorDescription)
        return
      }
      // 存储信息
      const { numbers, isTurn, round, status, winner } = storeToRefs(
        appStore.useGameStore
      )
      try {
        numbers.value = JSON.parse(data.numbers)
      } catch (e) {
        numbers.value = {}
      } finally {
        isTurn.value = data.isTurn
        round.value = data.round
        status.value = data.status
        winner.value = data.winner
      }
    }

    this.socket.onclose = async function (ev: CloseEvent) {
      ElMessage.warning(ev.code + ': 游戏结束')
      await router.replace('/main/online')
    }
  }

  sendMessage(message: IMessageGameWebSocketRequest) {
    let messageStr = JSON.stringify(message)
    messageStr = messageStr.substring(1, messageStr.length - 1)
    this.socket.send(messageStr)
  }

  async close() {
    this.socket.close()
    await router.replace('/main/online')
  }
}

export default GameWebSocket
