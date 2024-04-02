package com.nim.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameWebSocketVO implements Serializable {

    private static final long serialVersionUID = 2953977041742023401L;

    /**
     * 发送消息的对象（单播为指定用户账号；广播为：all；部分广播为：batch）
     */
    private String destination;

    /**
     * 是否是当前角色的回合
     */
    private Boolean isTurn;

    /**
     * 当前数据
     */
    private String numbers;

    /**
     * 当前游戏状态
     */
    private Integer status;

    /**
     * 游戏获胜方（存在获胜方-获胜方account；胜负未分：''；游戏结束：GAME_OVER）
     */
    private String winner;

    /**
     * 回合数
     */
    private Integer round;

    /**
     * 是否出现错误
     */
    private Boolean isError;

    /**
     * 错误描述
     */
    private String errorDescription;
}
