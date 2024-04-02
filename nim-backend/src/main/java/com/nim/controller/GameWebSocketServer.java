package com.nim.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nim.common.ErrorCode;
import com.nim.constant.RoomConstant;
import com.nim.exception.BusinessException;
import com.nim.pojo.domain.Room;
import com.nim.pojo.request.MessageGameWebSocketRequest;
import com.nim.pojo.request.OpenGameWebSocketRequest;
import com.nim.pojo.vo.GameNumberVO;
import com.nim.pojo.vo.GameWebSocketVO;
import com.nim.service.RoomService;
import com.nim.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ServerEndpoint("/websocket/{gameConnectConfig}")
@Component
public class GameWebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的WebSocket对象。
     */
    private static final ConcurrentHashMap<String, GameWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收user账号
     */
    private String userAccount;

    /**
     * 当前连接这是否为房主
     */
    private boolean isOwner;

    /**
     * 房间号
     */
    private String roomId;

    private static RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        GameWebSocketServer.roomService = roomService;
    }


    /**
     * 连接建立成功调用的方法
     *
     * @param session 与客户端连接的会话
     * @param gameConnectConfig 用户传入的连接信息（JSON->OpenGameWebSocketRequest）
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("gameConnectConfig") String gameConnectConfig) throws IOException {
        // 解析传入参数
        OpenGameWebSocketRequest gameConnectInfo = this.parseJSONToObj(gameConnectConfig, OpenGameWebSocketRequest.class);
        // 记录客户端连接
        this.session = session;
        this.userAccount = gameConnectInfo.getUserAccount();
        this.isOwner = gameConnectInfo.getIsOwner();
        // 记录连接
        if (webSocketMap.containsKey(this.userAccount)) {
            webSocketMap.get(this.userAccount).close();
            webSocketMap.remove(userAccount);
            webSocketMap.put(userAccount, this);
        } else{
            webSocketMap.put(userAccount, this);
        }
        // 针对创建连接对象是否为房主，分别处理
        if (this.isOwner) {
            ownerOnOpen(gameConnectInfo);
        } else {
            partnerOnOpen(gameConnectInfo);
        }
    }

    private void ownerOnOpen(OpenGameWebSocketRequest gameConnectInfo) {
        // 修改数据库中本局信息
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getOwner, this.userAccount);
        Room room = roomService.getOne(roomQueryWrapper);
        this.roomId = room.getId();
        if (room.getRoomStatus() != RoomConstant.UNUSED) {
            roomService.resetGameRoom(this.roomId);
        }
        String numbers = RandomUtils.generateRandomNumbers(gameConnectInfo.getCount(), gameConnectInfo.getMinNumberValue(), gameConnectInfo.getMaxNumberValue());
        room.setNumbers(numbers);
        if (gameConnectInfo.getIsOwnerFirst()) {
            room.setFirstPlayer(this.userAccount);
        }
        room.setRoomStatus(RoomConstant.WAITING);
        if (!roomService.updateById(room)) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR, "创建游戏时修改数据库失败");
        }
        // 发送信息
        sendInfo(this.userAccount, 0, false, numbers, room.getRoomStatus());
    }

    private void partnerOnOpen(OpenGameWebSocketRequest gameConnectInfo) {
        // 验证房间是否存在
        String roomId = gameConnectInfo.getRoomId();
        Room room = roomService.getById(roomId);
        if (null == room) {
            webSocketMap.remove(userAccount);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间号不存在");
        }
        // 验证是否为自己房间号
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getOwner, gameConnectInfo.getUserAccount());
        Room myRoom = roomService.getOne(roomQueryWrapper);
        if (myRoom.getId().equals(roomId)) {
            webSocketMap.remove(userAccount);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入自己的房间");
        }
        this.roomId = roomId;
        // 验证房间状态
        boolean isContinue = false;
        switch (room.getRoomStatus()) {
            case RoomConstant.UNUSED:
                webSocketMap.remove(userAccount);
                throw new BusinessException(ErrorCode.RESULT_ERROR, "房主暂时不在线");
            case RoomConstant.END:
                webSocketMap.remove(userAccount);
                throw new BusinessException(ErrorCode.RESULT_ERROR, "该房间已在游戏对局中");
            case RoomConstant.PLAYING:
            case RoomConstant.PAUSE:
                if (this.userAccount.equals(room.getPartner())) { // 继续游戏
                    isContinue = true;
                    // 发送一条消息，通知前端正在连接中
                    int nowRound = room.getRound();
                    sendInfo(userAccount, nowRound, !isOwnerTurn(nowRound, !Objects.equals(room.getFirstPlayer(), userAccount)), room.getNumbers(), room.getRoomStatus(), "");
                } else {
                    webSocketMap.remove(userAccount);
                    throw new BusinessException(ErrorCode.RESULT_ERROR, "该房间已在游戏对局中");
                }
                break;
            default:
                break;
        }
        // 修改数据库
        room.setRoomStatus(RoomConstant.PLAYING);
        if (!isContinue) { // 加入新游戏
            if (StringUtils.isBlank(room.getFirstPlayer())) {
                room.setFirstPlayer(this.userAccount);
                gameConnectInfo.setIsOwnerFirst(false);
            } else {
                gameConnectInfo.setIsOwnerFirst(true);
            }
            room.setPartner(this.userAccount);
            room.setRound(1);
        }
        if (!roomService.updateById(room)) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR, "加入游戏时修改数据库错误");
        }
        // 发送消息
        if (isContinue) {
            boolean ownerTurn = isOwnerTurn(room.getRound(), !room.getFirstPlayer().equals(this.userAccount));
            sendInfo(userAccount, room.getRound(), !ownerTurn, room.getNumbers(), room.getRoomStatus(), parseWinner(room.getWinner(), room));
            sendInfo(room.getOwner(), room.getRound(), ownerTurn, room.getNumbers(), room.getRoomStatus(), parseWinner(room.getWinner(), room));
        } else {
            sendInfo(userAccount, room.getRound(), !gameConnectInfo.getIsOwnerFirst(), room.getNumbers(), room.getRoomStatus());
            sendInfo(room.getOwner(), room.getRound(), gameConnectInfo.getIsOwnerFirst(), room.getNumbers(), room.getRoomStatus());
        }
    }

    private boolean isOwnerTurn(int round, boolean isOwnerFirst) {
        int res = round % 2;
        if (isOwnerFirst) {
            return res != 0;
        } else {
            return res == 0;
        }
    }

    private String parseWinner(int winnerCode, Room room) {
        switch (winnerCode) {
            case RoomConstant.WINNER_OWNER:
                return room.getOwner();
            case RoomConstant.WINNER_PARTNER:
                return room.getPartner();
            default:
                return "";
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 删除与当前客户端连接的记录
        webSocketMap.remove(userAccount);
        // 其他收尾工作
        Room room = roomService.getById(this.roomId);
        if (this.isOwner) { // 房主的退出游戏操作
            roomService.resetGameRoom(this.roomId);
            // 发送消息给参与方，提示游戏已经结束
            String partnerAccount = room.getPartner();
            if (StringUtils.isNotBlank(partnerAccount)) {
                sendInfo(partnerAccount, room.getRound(), false, room.getNumbers(), RoomConstant.END, RoomConstant.GAME_OVER);
            }
        } else { // 参与方的退出游戏操作
            room.setRoomStatus(RoomConstant.PAUSE);
            if (!roomService.updateById(room)) {
                roomService.resetGameRoom(room.getId());
                sendInfo(room.getOwner(), room.getRound(), false, room.getNumbers(), RoomConstant.END, RoomConstant.GAME_OVER);
            } else {
                sendInfo(room.getOwner(), room.getRound(), false, room.getNumbers(), RoomConstant.PAUSE, RoomConstant.GAME_OVER);
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message) {
        // 参数校验
        if (StringUtils.isBlank(message)) {
//            sendInfo(this.userAccount, room.getRound(), true, numbersStr, room.getRoomStatus(), "", true, "游戏操作失败：传入修改数据为空");
//            return;
            throw new BusinessException(ErrorCode.NULL_ERROR, "传入修改的数据为空");
        }
        MessageGameWebSocketRequest requestMessage = parseJSONToObj(message, MessageGameWebSocketRequest.class);
        // 从数据库中获取当前房间对象
        Room room = roomService.getById(this.isOwner ? this.roomId : requestMessage.getRoomId());
        // 获取修改前数据
        String numbersStr = room.getNumbers();
        Map<String, GameNumberVO> numbers = JSONObject.parseObject(numbersStr)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> JSONObject.parseObject(String.valueOf(entry.getValue()), GameNumberVO.class)));
        // 查询修改数据，执行修改
        boolean isModify = false;
        for (Map.Entry<String, GameNumberVO> entry : numbers.entrySet()) {
            Integer oldValue = entry.getValue().getValue();
            if (oldValue.equals(requestMessage.getChosenNumber())) {
                isModify = true;
                // 执行修改操作
                if (Objects.equals(requestMessage.getOperateType(), RoomConstant.OPERATE_ADD)) {
                    GameNumberVO gameNumberVO = new GameNumberVO();
                    gameNumberVO.setValue(oldValue + requestMessage.getOperateValue());
                    entry.setValue(gameNumberVO);
                } else {
                    int newValue = oldValue - requestMessage.getOperateValue();
                    if (newValue < 0) newValue = 0;
                    GameNumberVO gameNumberVO = new GameNumberVO();
                    gameNumberVO.setValue(newValue);
                    entry.setValue(gameNumberVO);
                }
                break;
            }
        }
        if (!isModify) { // 数字列表中未找到该数字
            sendInfo(this.userAccount, room.getRound(), true, numbersStr, room.getRoomStatus(), "", true, "操作失败：未找到操作数");
            return;
        }
        // 将修改内容存入数据库
        String newNumberStr = JSON.toJSONString(numbers);
        room.setNumbers(newNumberStr);
        // 判断游戏是否结束
        boolean gameOver = isGameOver(numbers);
        if (gameOver) {
            room.setRoomStatus(RoomConstant.END);
            room.setWinner(this.isOwner ? RoomConstant.WINNER_OWNER : RoomConstant.WINNER_PARTNER);
        } else {
            room.setRound(room.getRound() + 1);
        }
        if (!roomService.updateById(room)) {
            sendInfo(this.userAccount, gameOver ? room.getRound() : room.getRound() - 1, true, numbersStr, room.getRoomStatus(), "", true, "操作失败：数据库操作失败，请重新操作");
            return;
        }
        // 通知双方进行下一回合游戏
        String yourPartnerAccount = this.isOwner ? room.getPartner() : room.getOwner();
        if (gameOver) {
            sendInfo(userAccount, room.getRound(), false, newNumberStr, room.getRoomStatus(), userAccount);
            sendInfo(yourPartnerAccount, room.getRound(),false, newNumberStr, room.getRoomStatus(), userAccount);
        } else {
            sendInfo(userAccount, room.getRound(), false, newNumberStr, room.getRoomStatus());
            sendInfo(yourPartnerAccount, room.getRound(), true, newNumberStr, room.getRoomStatus());
        }
    }

    /**
     * 判断游戏是否结束
     * @param numbers 当前数字列表
     * @return 结束 -> true; 未结束 -> false
     */
    private boolean isGameOver(Map<String, GameNumberVO> numbers) {
        boolean isOver = true;
        for (Map.Entry<String, GameNumberVO> entry : numbers.entrySet()) {
            if (entry.getValue().getValue() != 0) {
                isOver = false;
                break;
            }
        }
        return isOver;
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println("用户错误:"+this.userAccount +",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     *
     * @param message 消息（JSON：GameWebSocketVO）
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.RESULT_ERROR, "服务端发送数据失败");
        }
    }

    /**
     * 指定对象发送数据
     *
     * @param message 消息内容（JSON：GameWebSocketVO）
     * @param destinationUserAccount 消息的接收方的用户账号
     */
    public void sendInfo(String message, String destinationUserAccount) {
        if (StringUtils.isNotBlank(destinationUserAccount) && webSocketMap.containsKey(destinationUserAccount)) {
            webSocketMap.get(destinationUserAccount).sendMessage(message);
        } else {
            Room room = roomService.getById(this.roomId);
            if (room.getRoomStatus() != RoomConstant.END) {
                room.setRoomStatus(RoomConstant.PAUSE);
            }
            roomService.updateById(room);
            throw new BusinessException(ErrorCode.RESULT_ERROR, "用户'" + destinationUserAccount + "',不在线！");
        }
    }
    /**
     * 指定对象发送数据
     */
    public void sendInfo(String destinationUserAccount, int round, boolean isTurn, String numbers, int status, String winner, boolean isError, String errorDescription) {
        GameWebSocketVO message = new GameWebSocketVO();
        message.setDestination(destinationUserAccount);
        message.setRound(round);
        message.setIsTurn(isTurn);
        message.setStatus(status);
        message.setNumbers(numbers);
        message.setWinner(winner);
        message.setIsError(isError);
        message.setErrorDescription(errorDescription);
        sendInfo(JSON.toJSONString(message), destinationUserAccount);
    }
    public void sendInfo(String destinationUserAccount, int round, boolean isTurn, String numbers, int status, String winner) {
        sendInfo(destinationUserAccount, round, isTurn, numbers, status, winner, false, "");
    }
    public void sendInfo(String destinationUserAccount, int round, boolean isTurn, String numbers, int status) {
        sendInfo(destinationUserAccount, round, isTurn, numbers, status, "", false, "");
    }


    /**
     * 主动断开连接
     */
    public void close() throws IOException {
        this.session.close();
        this.onClose();
    }

    /**
     * 获得此时的在线人数
     *
     * @return 在线人数
     */
    public static synchronized int getOnlineCount() {
        return webSocketMap.size();
    }

    /**
     * 在线人数加1
     */
    public static synchronized void addOnlineCount() {
        System.out.println("call +1");
        GameWebSocketServer.onlineCount++;
    }

    /**
     * 在线人数减1
     */
    public static synchronized void subOnlineCount() {
        System.out.println("call -1");
        GameWebSocketServer.onlineCount--;
    }

    /**
     * 将前端通过WebSocket传入的JSON字符串转化为Java对象
     *
     * @param jsonString JSON字符串（不带前后的{}）
     * @param objClass 待转成的对象类型的class对象
     * @return 转换后的对象
     * @param <T> 待转成的对象类型
     */
    private <T> T parseJSONToObj(String jsonString, Class<? extends T> objClass) {
        return JSON.parseObject("{" + jsonString + "}", objClass);
    }
}
