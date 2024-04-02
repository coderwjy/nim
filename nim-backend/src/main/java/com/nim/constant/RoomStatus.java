package com.nim.constant;

/**
 * 游戏房间状态
 */
public enum RoomStatus {
    /**
     * 房间未启用
     */
    UNUSED(0),

    /**
     * 等待玩家加入
     */
    WAITING(1),

    /**
     * 游戏进行中
     */
    PLAYING(2),

    /**
     * 游戏暂停
     */
    PAUSE(3),

    /**
     * 游戏结束
     */
    END(4)
    ;

    private final int status;

    public int getStatus() {
        return status;
    }

    RoomStatus(int status) {
        this.status = status;
    }
}
