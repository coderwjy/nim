import { ref } from 'vue'
import { defineStore } from 'pinia'

import { GameWebSocket } from '@/service/game'
import type {
  IOpenGameWebSocketRequest,
  IMessageGameWebSocketRequest
} from '@/service/game/type'
import { localCache } from '@/utils/cache'
import { JOIN_ROOM_ID } from '@/constant/cache_keys'
import type { ICreateOnlineGameConfig } from '@/service/game/type'

export const useGameStore = defineStore('game', () => {
  let gameWebSocket: GameWebSocket

  const gameConfig = ref<ICreateOnlineGameConfig>({
    count: 3,
    isOwnerFirst: true,
    maxNumberValue: 10,
    minNumberValue: 100
  })
  const numbers = ref<any>({})
  const isTurn = ref<boolean>(false)
  const round = ref<number>(0)
  const status = ref<number>(0)
  const winner = ref<string>('')

  function openGameWebSocket(config: IOpenGameWebSocketRequest) {
    gameWebSocket = new GameWebSocket(config)
  }

  function sendMessage(message: IMessageGameWebSocketRequest) {
    gameWebSocket.sendMessage(message)
  }

  function modifyNumber(
    roomId: string,
    chosenNumber: number,
    operateValue: number
  ) {
    const message = {
      roomId,
      chosenNumber,
      operateValue,
      operateType: 0,
      nextPlayer: ''
    } as IMessageGameWebSocketRequest
    sendMessage(message)
  }

  function modifyNumberWhenTimeout() {
    for (const i in numbers.value) {
      const oldValue = numbers.value[i].value
      if (oldValue > 0) {
        const message = {
          roomId: localCache.getCache(JOIN_ROOM_ID) ?? '',
          chosenNumber: oldValue,
          operateValue: 1,
          operateType: 0,
          nextPlayer: ''
        } as IMessageGameWebSocketRequest
        sendMessage(message)
        break
      }
    }
  }

  function closeGame() {
    gameWebSocket.close()
  }

  return {
    gameConfig,
    numbers,
    isTurn,
    round,
    status,
    winner,
    openGameWebSocket,
    sendMessage,
    modifyNumber,
    modifyNumberWhenTimeout,
    closeGame
  }
})
