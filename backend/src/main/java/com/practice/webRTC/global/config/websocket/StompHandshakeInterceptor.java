package com.practice.webRTC.global.config.websocket;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class StompHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        System.out.println("🟢 HandshakeInterceptor: 실행됨");

        if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {
            String accessToken = servletServerHttpRequest.getServletRequest()
                    .getParameter("accessToken");
            System.out.println("accessToken=" + accessToken);
            if (accessToken != null && !accessToken.isEmpty()) {
                attributes.put("accessToken", accessToken);
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

    }
}
