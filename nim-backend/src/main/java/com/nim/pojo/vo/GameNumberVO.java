package com.nim.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 游戏中Number的存储类型（Number：Map<Integer, GameNumberVO>）
 */
@Data
public class GameNumberVO implements Serializable {

    private static final long serialVersionUID = -227578783443831648L;

    /**
     * 数据值
     */
    private Integer value;
}
