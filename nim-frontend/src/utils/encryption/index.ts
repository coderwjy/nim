import CryptoJS from 'crypto-js'

const key = CryptoJS.enc.Utf8.parse('123456789asdfghj') // 十六位十六进制数作为密钥
const iv = CryptoJS.enc.Utf8.parse('asdfghj123456789') // 十六位十六进制数作为密钥偏移量

export function encryption(data: any) {
  const srcs = CryptoJS.enc.Utf8.parse(data)
  const encrypted = CryptoJS.AES.encrypt(srcs, key, {
    iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  })
  return encrypted.ciphertext.toString().toUpperCase()
}

export function decryption(cipertext: string) {
  const encryptedHexStr = CryptoJS.enc.Hex.parse(cipertext)
  const srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr)
  const decrypt = CryptoJS.AES.decrypt(srcs, key, {
    iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  })
  const decryptedStr = decrypt.toString(CryptoJS.enc.Utf8)
  return decryptedStr.toString()
}
