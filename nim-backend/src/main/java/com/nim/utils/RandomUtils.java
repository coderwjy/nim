package com.nim.utils;

import com.alibaba.fastjson2.JSON;
import com.nim.pojo.vo.GameNumberVO;

import java.util.HashMap;
import java.util.Random;

public class RandomUtils {

    /**
     * 生成随机的起始数据
     *
     * @param count 数据个数
     * @param Scope 数据大小（传入10、100、1000）
     * @return 随机数JSON，插入数据库的numbers列
     */
    public static String generateRandomNumbers(int count, int min, int max) {
        HashMap<String, GameNumberVO> numbers = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            GameNumberVO number = new GameNumberVO();
            number.setValue(randomInRange(min, max));
            numbers.put((i + 1) + "", number);
        }
        return JSON.toJSONString(numbers);
    }

    /**
     * 生成一个范围内的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 一个随机数
     */
    public static int randomInRange(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }



}
