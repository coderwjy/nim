package com.nim.constant;

public interface RoomConstant {

    //  ------- 房间状态 --------

    /**
     * 房间未启用
     */
    int UNUSED = 0;

    /**
     * 等待玩家加入
     */
    int WAITING = 1;

    /**
     * 游戏进行中
     */
    int PLAYING = 2;

    /**
     * 游戏暂停
     */
    int PAUSE = 3;

    /**
     * 游戏结束
     */
    int END = 4;


    // ------- room数据库中winner字段 --------

    /**
     * 胜负未分
     */
    int WINNER_UNDEFINED = 0;

    /**
     * 房主获胜
     */
    int WINNER_OWNER = 1;

    /**
     * 参与者获胜
     */
    int WINNER_PARTNER = 2;


    // ------- 游戏过程中，服务器发送WebSocket时，消息对象中的常量 --------

    /**
     * 一方主动结束游戏后，winner字段
     */
    String GAME_OVER = "GAME_OVER";


    /**
     * 发送目的地为所有对象
     */
    String SEND_DESTINATION_ALL = "ALL";

    /**
     * 发送目的地为某些对象
     */
    String SEND_DESTINATION_BATCH = "BATCH";


    // ------- 游戏中对数字对操作类型 --------
    /**
     * 减（默认操作）
     */
    final Integer OPERATE_SUB = 0;

    /**
     * 加
     */
    final Integer OPERATE_ADD = 1;

}
