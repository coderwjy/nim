import { randomIntInRange } from '@/utils/random'

export function diffMode(
  numberList: number[],
  num: number,
  nonZeroIndexList: number[]
): number[] {
  //随机选择一堆数
  const chooseIdx =
    nonZeroIndexList[randomIntInRange(0, nonZeroIndexList.length - 1)]
  let flag = 0
  //在该堆下进行操作
  for (let i = 1; i <= numberList[chooseIdx]; i++) {
    numberList[chooseIdx] -= i
    let res = numberList[0]
    for (let j = 1; j < num; j++) {
      res = res ^ numberList[j]
    }
    if (res == 0) {
      flag = 1
      break
    } else {
      numberList[chooseIdx] += i
    }
  }
  // 如果操作未成功
  if (flag == 0) {
    let sum = 0
    for (let i = 0; i < num; i++) {
      if (numberList[i] != 0) {
        sum++
      }
    }
    if (sum == 1) {
      numberList[chooseIdx] = 0
    } else {
      diffMode(numberList, num, nonZeroIndexList)
    }
  }
  return numberList
}
