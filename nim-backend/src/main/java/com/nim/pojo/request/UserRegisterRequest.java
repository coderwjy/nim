package com.nim.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author coderwjy
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1625286288766366204L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
