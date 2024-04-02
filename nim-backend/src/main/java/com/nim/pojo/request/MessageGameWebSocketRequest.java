package com.nim.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 游戏过程中，用户通过WebSocket向服务器传递的消息的结构
 */
@Data
public class MessageGameWebSocketRequest implements Serializable {

    private static final long serialVersionUID = 4410728727619207071L;

    /**
     * 参与的游戏房间ID
     */
    private String roomId;

    /**
     * 被选择的数据
     */
    private Integer chosenNumber;

    /**
     * 执行操作的数值
     */
    private Integer operateValue;

    /**
     * 执行的操作（0-减(default)；1-加）
     */
    private Integer operateType;

    /**
     * 下一个回合的操作对象
     */
    private String nextPlayer;
}
