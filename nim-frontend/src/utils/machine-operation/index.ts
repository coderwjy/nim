import { randomIntInRange } from '@/utils/random'
import { diffMode } from '@/utils/machine-operation/diff-mode'
import { easyMode } from '@/utils/machine-operation/easy-mode'

export default function (
  numberList: number[],
  num: number,
  difficulty: number,
  useDifficulty: boolean
): number[] {
  // 记录非0位置的编号
  const nonZeroIndexList: number[] = []
  for (let i = 0; i < num; i++) {
    if (numberList[i] != 0) {
      nonZeroIndexList.push(i)
    }
  }

  const choose = useDifficulty
    ? randomIntInRange(0, Math.round(1 + Math.pow(2 * difficulty, 2)))
    : 1

  // -- 认真模式
  if (choose != 0) {
    return diffMode(numberList, num, nonZeroIndexList)
  }
  // 笨蛋模式，随机选择一堆数，并随机减少数字
  else {
    return easyMode(numberList, num, nonZeroIndexList)
  }
}
