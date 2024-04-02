package com.nim.mapper;

import com.nim.pojo.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 针对表【user(用户)】的数据库操作Mapper
 *
 * @author coderwjy
 * @Entity com.nim.pojo.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




