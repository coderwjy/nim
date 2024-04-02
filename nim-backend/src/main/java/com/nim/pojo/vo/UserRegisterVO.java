package com.nim.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册后，返回给前端的信息
 *
 * @author coderwjy
 */
@Data
public class UserRegisterVO implements Serializable {

    private static final long serialVersionUID = -4381453604849901926L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户专属房间号
     */
    private String gameRoomId;
}
