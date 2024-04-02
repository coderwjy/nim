package com.nim.mapper;

import com.nim.pojo.domain.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【room(游戏房间表)】的数据库操作Mapper
 *
 * @author coderwjy
 * @Entity com.nim.pojo.domain.Room
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {

}




