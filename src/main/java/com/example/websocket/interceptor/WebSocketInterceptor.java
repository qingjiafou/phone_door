package com.example.websocket.interceptor;

import com.example.websocket.constant.WsConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * TODO
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/29 21:30
 * @update none
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * WebSocket 无法使用 header 传递参数,用param
     * 
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            // 解析出设备ID并注入attributes
            String deviceId = servletServerHttpRequest.getServletRequest().getParameter(WsConst.deviceIdParamKey);
            // TODO 标准的做法要校验本设备ID是否属于本系统,由设备相关业务开发人员负责.
            if (StringUtils.isBlank(deviceId)) {
                log.error("确保有设备ID参数,例如: ws://127.0.0.1:8080/ws/dev?{}=123123", WsConst.deviceIdParamKey);
            }
            attributes.put(WsConst.deviceIdParamKey, deviceId);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, Exception exception) {

    }
}
