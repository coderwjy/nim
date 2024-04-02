export interface IUserLoginRequest {
  userAccount: string
  userPassword: string
}

export interface IUserRegistRequest extends IUserLoginRequest {
  checkPassword: string
}

export interface IUserRegistResponse {
  userId: string
  userAccount: string
  gameRoomId: string
}

export interface IUserInfo {
  id?: string
  userAccount?: string
  phone?: string
  email?: string
  userRole?: number
  nickname?: string
  avatarUrl?: string
  gender?: number
  userStatus?: number
  createTime?: Date
  updateTime?: Date
}
