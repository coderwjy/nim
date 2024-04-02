package com.nim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nim.common.ErrorCode;
import com.nim.exception.BusinessException;
import com.nim.pojo.domain.Room;
import com.nim.pojo.domain.User;
import com.nim.pojo.vo.UserVO;
import com.nim.service.RoomService;
import com.nim.mapper.RoomMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 针对表【room(游戏房间表)】的数据库操作Service实现
 *
 * @author coderwjy
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{

    private final RoomMapper roomMapper;

    @Autowired
    public RoomServiceImpl(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    /**
     * 创建游戏房间
     *
     * @param user 用户信息
     * @return 游戏房间ID
     */
    @Override
    public String createGameRoom(User user) {
        // 参数校验
        if (null == user) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息为空");
        }
        // 查询数据库中是否已有该用户的房间（保证一个用户只能有一个房间）
        String userAccount = user.getUserAccount();
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getOwner, userAccount);
        if (roomMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已为该用户分配房间，请勿重复分配");
        }
        // 根据用户信息创建房间
        Room room = new Room();
        room.setOwner(userAccount);
        room.setPartner("");
        room.setFirstPlayer("");
        room.setNumbers("");
        room.setWinner(0);
        room.setRound(0);
        room.setOwnerSkill("");
        room.setPartnerSkill("");
        // 插入数据库
        if (!this.save(room)) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR, "数据库插入游戏房间失败");
        }
        return room.getId();
    }

    @Override
    public String createGameRoom(UserVO userVO) {
        User user = new User();
        user.setUserAccount(userVO.getUserAccount());
        return createGameRoom(user);
    }

    /**
     * 重置游戏房间信息
     *
     * @param roomId 游戏房间ID
     * @return 是否重置成功
     */
    @Override
    public boolean resetGameRoom(String roomId) {
        // 参数校验
        if (StringUtils.isAnyBlank(roomId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入游戏房间ID为空");
        }
        // 根据房间ID查询房间信息
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Room::getId, roomId);
        Room room = roomMapper.selectOne(queryWrapper);
        // 重置房间信息
        room.setPartner("");
        room.setRoomStatus(0);
        room.setFirstPlayer("");
        room.setNumbers("");
        room.setWinner(0);
        room.setRound(0);
        room.setOwnerSkill("");
        room.setPartnerSkill("");
        // 更新数据库
        if (roomMapper.updateById(room) != 1) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR, "重置游戏房间信息失败");
        } else {
            return true;
        }
    }

}




