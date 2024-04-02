/**
 * 生成一个范围内的随机数（int数字）
 * @param min 最小值
 * @param max 最大值
 */
export const randomIntInRange = (min: number, max: number): number => {
  return Math.floor(min + Math.random() * (max - min + 1))
}

/**
 * 生成随机的起始数据
 * @param count 数据个数
 * @param min 最小值
 * @param max 最大值
 */
export const generateRandomNumbers = (
  count: number,
  min: number,
  max: number
): any => {
  const numbers: any = {}
  for (let i = 0; i < count; i++) {
    const strI = String(i)
    numbers[strI] = {
      key: i,
      value: randomIntInRange(min, max)
    }
  }
  return numbers
}
