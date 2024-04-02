import { request } from '../'
import type {
  IUserLoginRequest,
  IUserInfo,
  IUserRegistRequest,
  IUserRegistResponse
} from './type'

export enum UserAPI {
  Login = '/user/login',
  Register = '/user/register',
  Room = '/user/roomId'
}

/**
 * 用户登录
 *
 * @param loginRequestParams 用户登录接口请求参数
 * @returns 用户信息
 */
export function userLogin(loginRequestParams: IUserLoginRequest) {
  return request.post<IUserInfo>({
    url: UserAPI.Login,
    data: loginRequestParams
  })
}

/**
 * 用户注册
 *
 * @param registerRequestParams 用户注册接口请求参数
 * @returns 用户id
 */
export function userRegister(registerRequestParams: IUserRegistRequest) {
  return request.post<IUserRegistResponse>({
    url: UserAPI.Register,
    data: registerRequestParams
  })
}

/**
 * 获取用户的房间号
 *
 * @param userAccount
 * @returns
 */
export function getUserRoomId(userAccount: string) {
  return request.get<string>({
    url: UserAPI.Room,
    params: {
      userAccount
    }
  })
}
