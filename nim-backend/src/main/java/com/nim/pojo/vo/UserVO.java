package com.nim.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回给前端的用户对象（经过脱敏处理）
 *
 * @author coderwjy
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -4367055250913111836L;

    /**
     * id
     */
    private String id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
