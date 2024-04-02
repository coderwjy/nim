package com.nim.service;

import com.nim.pojo.domain.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nim.pojo.domain.User;
import com.nim.pojo.vo.UserVO;

/**
 * 针对表【room(游戏房间表)】的数据库操作Service
 *
 * @author coderwjy
 */
public interface RoomService extends IService<Room> {

    /**
     * 创建游戏房间
     *
     * @param user 用户信息
     * @return 游戏房间ID
     */
    String createGameRoom(User user);

    /**
     * 创建游戏房间（重载）
     *
     * @param userVO 脱敏后的用户信息
     * @return 游戏房间ID
     */
    String createGameRoom(UserVO userVO);

    /**
     * 重置游戏房间信息
     *
     * @param roomId 游戏房间ID
     * @return 是否重置成功
     */
    boolean resetGameRoom(String roomId);
}
