package com.nim.controller;

import com.nim.common.BaseResponse;
import com.nim.common.ErrorCode;
import com.nim.common.ResultUtils;
import com.nim.exception.BusinessException;
import com.nim.pojo.request.UserLoginRequest;
import com.nim.pojo.request.UserRegisterRequest;
import com.nim.pojo.vo.UserRegisterVO;
import com.nim.pojo.vo.UserVO;
import com.nim.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public BaseResponse<UserRegisterVO> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        UserRegisterVO userRegisterVO = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userRegisterVO);
    }

    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        UserVO userVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userVO);
    }

    @GetMapping("/roomId")
    public BaseResponse<String> getUserRoomId(@RequestParam("userAccount") String userAccount) {
        if (StringUtils.isBlank(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入用户账号为空");
        }
        String userRoomId = userService.getUserRoomId(userAccount);
        return ResultUtils.success(userRoomId);
    }
}
