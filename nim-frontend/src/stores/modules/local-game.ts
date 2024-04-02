import { ref } from 'vue'
import { defineStore } from 'pinia'

import type { ICreateLocalGameConfig } from '@/service/game/type'
import { generateRandomNumbers } from '@/utils/random'
import machineOperation from '@/utils/machine-operation'

export const useLocalGameStore = defineStore('localGame', () => {
  const gameConfig = ref<ICreateLocalGameConfig>({
    count: 3,
    isOwnerFirst: true,
    maxNumberValue: 10,
    minNumberValue: 100,
    gameMode: 0
  })

  const numbers = ref<any>({})
  const round = ref<number>(0)
  const status = ref<number>(0)
  const isTurn = ref<boolean>(false)
  const winner = ref<string>('')

  /**
   * 初始化游戏配置
   * @param config 游戏房间配置
   */
  function setLocalGameConfig(config: ICreateLocalGameConfig) {
    gameConfig.value = config
  }

  /**
   * 开始游戏
   */
  async function startLocalGame() {
    const config = gameConfig.value
    numbers.value = generateRandomNumbers(
      config.count,
      config.minNumberValue,
      config.maxNumberValue
    )
    const ownerFirst = gameConfig.value.isOwnerFirst
    status.value = ownerFirst ? 2 : 3
    if (ownerFirst) {
      isTurn.value = true
      round.value = 1
    } else {
      await machineOperate()
    }
  }

  /**
   * 修改数字
   * @param i 数字所在下标
   * @param oldNumber 修改前数据
   * @param changeNumber 待减去的值
   */
  function modifyNumber(
    i: string,
    oldNumber: number,
    changeNumber: number
  ): boolean {
    if (oldNumber == 0) {
      return false
    }
    if (numbers.value[i].value != oldNumber) {
      return false
    }
    const newValue = numbers.value[i].value - changeNumber
    numbers.value[i].value = newValue < 0 ? 0 : newValue
    return true
  }

  /**
   * 玩家超时后仍未处理，系统执行操作
   */
  function modifyNumberWhenTimeout() {
    // 选择数字-1
    for (const i in numbers.value) {
      const oldValue = numbers.value[i].value
      if (oldValue > 0) {
        modifyNumber(i, oldValue, 1)
      }
    }
    // 判断游戏是否结束
    if (checkWinner()) {
      status.value = 4
      winner.value = '恭喜你赢下本局游戏'
      return
    }
    // 交给机器操作
    setTimeout(async () => {
      await machineOperate()
    }, 1000)
  }

  /**
   * 机器操作
   */
  async function machineOperate() {
    isTurn.value = false
    round.value += 1
    switch (gameConfig.value.gameMode) {
      case 0:
        await machineModeEntry()
        break
      case 1:
      case 2:
        await machineModeEasy(gameConfig.value.gameMode, true)
        break
      case 3:
        await machineModeAsian()
        break
    }
  }

  /**
   * 判断游戏是否结束
   */
  function checkWinner(): boolean {
    let isWin = true
    for (const i in numbers.value) {
      if (numbers.value[i].value != 0) {
        isWin = false
        break
      }
    }
    return isWin
  }

  // 机器操作

  async function machineModeEntry() {
    setTimeout(() => {
      for (const i in numbers.value) {
        const oldValue = numbers.value[i].value
        if (oldValue > 0) {
          modifyNumber(i, oldValue, 1)
        }
      }
      afterMachineOperate()
    }, 1500)
  }

  async function machineModeEasy(difficulty: number, useDifficulty: boolean) {
    setTimeout(() => {
      let numberList = parseNumberObjToList(numbers.value)
      numberList = machineOperation(
        numberList,
        gameConfig.value.count,
        difficulty,
        useDifficulty
      )
      numbers.value = parseNumberListToObj(numberList)
      afterMachineOperate()
    }, 1000)
  }

  async function machineModeAsian() {
    await machineModeEasy(1, false)
  }

  function afterMachineOperate() {
    if (checkWinner() && !isTurn.value) {
      // 机器获胜
      winner.value = '很遗憾，您输掉了对局'
      status.value = 4
    } else {
      // 人获胜
      isTurn.value = true
      round.value += 1
    }
  }

  // -- 辅助函数
  function parseNumberObjToList(numbers: {
    [key: string]: { key: number; value: number }
  }): number[] {
    const numberList: number[] = []
    Object.values(numbers).forEach((value) => {
      numberList.push(value.value)
    })
    return numberList
  }

  function parseNumberListToObj(numberList: number[]): {
    [key: string]: { key: number; value: number }
  } {
    const numberObj: { [key: string]: { key: number; value: number } } = {}
    let idx = 0
    numberList.forEach((number) => {
      numberObj[String(idx++)] = {
        key: idx,
        value: number
      }
    })
    return numberObj
  }

  return {
    gameConfig,
    numbers,
    round,
    status,
    isTurn,
    winner,
    setLocalGameConfig,
    startLocalGame,
    modifyNumber,
    modifyNumberWhenTimeout,
    machineOperate,
    checkWinner
  }
})
