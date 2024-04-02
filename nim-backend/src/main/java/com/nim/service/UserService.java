package com.nim.service;

import com.nim.pojo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nim.pojo.vo.UserRegisterVO;
import com.nim.pojo.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 针对表【user(用户)】的数据库操作Service
 *
 * @author coderwjy
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 重复输入密码
     * @return 用户账号 和 用户专属房间号
     */
    UserRegisterVO userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码（未加密）
     * @param request HttpServletRequest，用于设置登录态
     * @return 脱敏后的用户信息
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request HttpServletRequest，用于移除用户登录态
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 通过用户账号获取用户信息
     *
     * @param userAccount 用户账号
     * @return 用户完整信息
     */
    User getUserByAccount(String userAccount);

    /**
     * 通过用户账号获取用户信息
     *
     * @param userAccount 用户账号
     * @return 脱敏后的用户信息
     */
    UserVO getSafetyUserByAccount(String userAccount);

    /**
     * 通过用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 脱敏后的用户信息
     */
    UserVO getSafetyUserById(String userId);

    /**
     * 获取用户的房间号
     *
     * @param userAccount 用户账号
     * @return 用户房间号
     */
    String getUserRoomId(String userAccount);
}
