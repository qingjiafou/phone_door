package com.example.websocket.config;

import com.example.websocket.constant.WsConst;
import com.example.websocket.ws.DefaultWebSocketHandler;
import com.example.websocket.ws.DefaultWebSocket;
import com.example.websocket.ws.WebSocket;
import com.example.websocket.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * TODO
 *
 * @author mmciel 761998179@qq.com
 * @version 1.0.0
 * @date 2024/3/29 21:09
 * @update none
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public DefaultWebSocketHandler defaultWebSocketHandler() {
        return new DefaultWebSocketHandler();
    }

    // @Bean
    // public WebSocket webSocket() {
    // return new DefaultWebSocket();
    // }

    @Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(defaultWebSocketHandler(), WsConst.urlPath)
                .addInterceptors(webSocketInterceptor())
                .setAllowedOrigins("*");
    }
}
