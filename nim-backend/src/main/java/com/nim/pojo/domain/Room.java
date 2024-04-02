package com.nim.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 游戏房间表
 *
 * @author coderwjy
 * @TableName room
 */
@TableName(value ="room")
@Data
public class Room implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 房主
     */
    private String owner;

    /**
     * 参与者
     */
    private String partner;

    /**
     * 游戏房间状态（0-未启用，1-等待玩家加入，2-游戏进行中，3-游戏暂停，4-游戏已结束）
     */
    private Integer roomStatus;

    /**
     * 先手方
     */
    private String firstPlayer;

    /**
     * 游戏数字（JSON {序号: 数据}）
     */
    private String numbers;

    /**
     * 获胜方（0-胜负未分，1-房主获胜，2-参与者获胜）
     */
    private Integer winner;

    /**
     * 回合数
     */
    private Integer round;

    /**
     * 房主技能列表（JSON {技能: 数量}）
     */
    private String ownerSkill;

    /**
     * 参与者技能（JSON {技能: 数量}）
     */
    private String partnerSkill;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}