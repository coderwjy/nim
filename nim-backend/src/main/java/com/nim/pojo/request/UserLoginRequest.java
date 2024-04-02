package com.nim.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author coderwjy
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1423584643253651839L;

    private String userAccount;

    private String userPassword;
}
