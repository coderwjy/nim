import { randomIntInRange } from '@/utils/random'

export function easyMode(
  numberList: number[],
  num: number,
  nonZeroIndexList: number[]
): number[] {
  //随机选择一堆数
  const chooseIdx =
    nonZeroIndexList[randomIntInRange(0, nonZeroIndexList.length - 1)]
  // 随机减少数字
  if (numberList[chooseIdx] > 1) {
    numberList[chooseIdx] -=
      1 + randomIntInRange(0, Math.min(numberList[chooseIdx] - 1, 5))
  } else {
    numberList[chooseIdx]--
  }
  return numberList
}
