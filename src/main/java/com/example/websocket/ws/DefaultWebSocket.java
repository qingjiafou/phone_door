package com.example.websocket.ws;

import cn.hutool.json.JSONUtil;
import com.example.websocket.constant.WsConst;
import com.example.websocket.handler.DevMessageHandler;
import com.example.websocket.to.UnifiedTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/29 21:26
 * @update none
 */
@Slf4j
@Service
public class DefaultWebSocket implements WebSocket {

    @Autowired
    DevMessageHandler devMessageHandler;

    private final AtomicInteger connectionCount = new AtomicInteger(0);

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void handleOpen(WebSocketSession session) {
        String devId = (String) session.getAttributes().get(WsConst.deviceIdParamKey);
        sessions.put(devId, session);
        int count = connectionCount.incrementAndGet();
        log.info("设备{}上线,当前设备数：{}", devId, count);
    }

    @Override
    public void handleClose(WebSocketSession session) {
        String devId = (String) session.getAttributes().get(WsConst.deviceIdParamKey);
        sessions.remove(devId);
        int count = connectionCount.decrementAndGet();
        log.info("设备{}下线,当前设备数：{}", devId, count);
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
        log.info("收到原始信息：{}", message);
        UnifiedTo res = devMessageHandler.handle(session, message);
        if (null != res) {
            try {
                this.sendMessage(session, JSONUtil.toJsonStr(res));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(String deviceId, TextMessage message) throws IOException {
        WebSocketSession dev = sessions.get(deviceId);
        if (null != dev) {
            dev.sendMessage(message);
        } else {
            log.error("设备不存在");
            throw new RuntimeException("设备不存在");
        }
    }

    @Override
    public void sendMessage(String deviceId, String message) throws IOException {
        this.sendMessage(deviceId, new TextMessage(message));
    }

    @Override
    public void sendMessage(String deviceId, UnifiedTo to) throws IOException {
        this.sendMessage(deviceId, new TextMessage(JSONUtil.toJsonStr(to)));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (!session.isOpen()) {
                continue;
            }
            this.sendMessage(session, message);
        }
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (!session.isOpen()) {
                continue;
            }
            session.sendMessage(message);
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id：{}", error.getMessage(), session.getId());
        log.error("", error);
    }

    @Override
    public Set<WebSocketSession> getSessions() {
        return (Set<WebSocketSession>) sessions.values();
    }

    @Override
    public int getConnectionCount() {
        return connectionCount.get();
    }
}
