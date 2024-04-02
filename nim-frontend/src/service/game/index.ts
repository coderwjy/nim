import { request } from '../'
import GameWebSocket from './socket'

/**
 * 查询当前连接用户数
 *
 * @returns
 */
export function getOnlineCount() {
  return request.get<number>({
    url: '/game/onlineCount'
  })
}

export { GameWebSocket }
