package com.nim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nim.common.ErrorCode;
import com.nim.exception.BusinessException;
import com.nim.mapper.UserMapper;
import com.nim.pojo.domain.Room;
import com.nim.pojo.domain.User;
import com.nim.pojo.vo.UserRegisterVO;
import com.nim.pojo.vo.UserVO;
import com.nim.service.RoomService;
import com.nim.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nim.constant.UserConstant.*;

/**
 * 针对表【user(用户)】的数据库操作Service实现
 *
 * @author coderwjy
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserMapper userMapper;
    private final RoomService roomService;
    
    @Autowired
    public UserServiceImpl(UserMapper userMapper, RoomService roomService) {
        this.userMapper = userMapper;
        this.roomService = roomService;
    }


    /**
     * 用户注册
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 重复输入密码
     * @return 用户账号 和 用户专属房间号
     */
    @Override
    public UserRegisterVO userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        // 参数为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入参数为空");
        }
        // 长度
        if (userAccount.length() < ACCOUNT_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userAccount.length() > ACCOUNT_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过长");
        }
        if (userPassword.length() < PASSWORD_MIN_LEN || checkPassword.length() < PASSWORD_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (userPassword.length() > PASSWORD_MAX_LEN || checkPassword.length() > PASSWORD_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过长");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号中不能包含特殊字符");
        }
        // 密码和校验密码不相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        // 账户不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + userPassword).getBytes());

        // 3. 写入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        if (!this.save(user)) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR, "数据库插入用户失败");
        }

        // 4. 根据用户信息创建房间
        String roomId = roomService.createGameRoom(user);

        // 5. 构造返回对象
        UserRegisterVO userRegisterVO = new UserRegisterVO();
        userRegisterVO.setUserId(user.getId());
        userRegisterVO.setUserAccount(userAccount);
        userRegisterVO.setGameRoomId(roomId);
        return userRegisterVO;
    }

    /**
     * 用户登录
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码（未加密）
     * @param request HttpServletRequest，用于设置登录态
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        // 参数为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 长度
        if (userAccount.length() < ACCOUNT_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userAccount.length() > ACCOUNT_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号超过最大长度");
        }
        if (userPassword.length() < PASSWORD_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (userPassword.length() > PASSWORD_MAX_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码超过最大长度");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号中不能包含特殊字符");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((PASSWORD_SALT + userPassword).getBytes());
        // 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (null == user) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败，用户名与密码不匹配");
        }
        // 3. 用户脱敏
        UserVO safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request HttpServletRequest，用于移除用户登录态
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 通过用户账号获取用户信息
     * 
     * @param userAccount 用户账号
     * @return 用户完整信息
     */
    @Override
    public User getUserByAccount(String userAccount) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return user;
    }

    /**
     * 通过用户账号获取用户信息
     *
     * @param userAccount 用户账号
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO getSafetyUserByAccount(String userAccount) {
        return getSafetyUser(getUserByAccount(userAccount));
    }

    /**
     * 通过用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO getSafetyUserById(String userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        User user = userMapper.selectOne(queryWrapper);
        return getSafetyUser(user);
    }

    /**
     * 获取用户的房间号
     *
     * @param userAccount 用户账号
     * @return 用户房间号
     */
    @Override
    public String getUserRoomId(String userAccount) {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUserAccount, userAccount);
        Long userCount = userMapper.selectCount(userQueryWrapper);
        if (userCount != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getOwner, userAccount);
        Room room = roomService.getOne(roomQueryWrapper);
        return room.getId();
    }

    /**
     * 用户脱敏
     *
     * @param originUser 完整用户信息
     * @return 脱敏后的用户信息
     */
    private UserVO getSafetyUser(User originUser) {
        UserVO userVO = new UserVO();
        userVO.setId(originUser.getId());
        userVO.setUserAccount(originUser.getUserAccount());
        userVO.setPhone(originUser.getPhone());
        userVO.setEmail(originUser.getEmail());
        userVO.setUserRole(originUser.getUserRole());
        userVO.setNickname(originUser.getNickname());
        userVO.setAvatarUrl(originUser.getAvatarUrl());
        userVO.setGender(originUser.getGender());
        userVO.setUserStatus(originUser.getUserStatus());
        userVO.setCreateTime(originUser.getCreateTime());
        userVO.setUpdateTime(originUser.getUpdateTime());
        return userVO;
    }
}




