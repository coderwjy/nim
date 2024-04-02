package com.nim.controller;

import com.nim.common.BaseResponse;
import com.nim.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    /**
     * 获取在线总人数
     *
     * @return 目前正在游戏中的总人数
     */
    @GetMapping("/onlineCount")
    public BaseResponse<Integer> getOnlineCount() {
        int onlineCount = GameWebSocketServer.getOnlineCount();
        return ResultUtils.success(onlineCount);
    }

}
