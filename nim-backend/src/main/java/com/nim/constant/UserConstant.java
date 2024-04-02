package com.nim.constant;

/**
 * 用户常量
 *
 * @author coderwjy
 */
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";


    // -- 账号密码校验参数

    /**
     * 账号最短长度
     */
    int ACCOUNT_MIN_LEN = 4;

    /**
     * 账号最长长度
     */
    int ACCOUNT_MAX_LEN = 10;

    /**
     * 密码最短长度
     */
    int PASSWORD_MIN_LEN = 6;

    /**
     * 密码最长长度
     */
    int PASSWORD_MAX_LEN = 18;

    /**
     * 盐值，混淆密码
     */
     String PASSWORD_SALT = "coderwjy";


    //  ------- 权限 --------

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
