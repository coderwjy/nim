/**
 * 创建在线游戏的配置项
 */
export interface ICreateOnlineGameConfig {
  count: number
  isOwnerFirst: boolean
  minNumberValue: number
  maxNumberValue: number
  roundTime?: number
}

/**
 * 创建单机游戏的配置项
 */
export interface ICreateLocalGameConfig extends ICreateOnlineGameConfig {
  gameMode: number
}

/**
 * 连接 online GameWebSocket 的请求接口参数
 */
export interface IOpenGameWebSocketRequest extends ICreateOnlineGameConfig {
  userAccount: string
  isOwner: boolean
  roomId: string
}

/**
 * 服务端通过WebSocket传回的数据类型
 */
export interface IGameWebSocketResponse {
  destination: string
  isTurn: boolean
  numbers: string
  status: number
  winner: string
  round: number
  isError: boolean
  errorDescription: string
}

/**
 * 客户端通过WebSocket请求服务器的数据类型
 */
export interface IMessageGameWebSocketRequest {
  roomId: string
  chosenNumber: number
  operateValue: number
  operateType: number
  nextPlayer: string
}

/**
 * 数字的类型（引入技能机制后扩展）
 */
export interface INumber {
  value: number
}

/**
 * 所有数字类型
 */
// export interface INumbers<T = INumber> {

// }
