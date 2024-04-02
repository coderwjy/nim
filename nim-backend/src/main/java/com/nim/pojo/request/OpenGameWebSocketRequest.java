package com.nim.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 连接GameWebSocket的请求类
 */
@Data
public class OpenGameWebSocketRequest implements Serializable {

    private static final long serialVersionUID = -1273904659084787538L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 当前连接这是否为房主
     */
    private Boolean isOwner;

    /**
     * 房间号（非房主接入需要携带）
     */
    private String roomId;

    /**
     * 本局数据个数
     */
    private Integer count;

    /**
     * 房主是否先手
     */
    private Boolean isOwnerFirst;

    /**
     * 数据区间最小值
     */
    private Integer minNumberValue;

    /**
     * 数据区间最大值
     */
    private Integer maxNumberValue;
}
