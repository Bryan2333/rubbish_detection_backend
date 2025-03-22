package com.bryan.rubbish_detection_backend.websocket;

import com.bryan.rubbish_detection_backend.utils.CookieUtil;
import com.bryan.rubbish_detection_backend.utils.StpKit;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) throws Exception {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();

        Cookie tokenCookie = CookieUtil.getCookie(httpRequest, "satoken");

        if (tokenCookie == null) {
            return false;
        }

        String tokenValue = tokenCookie.getValue();

        Object adminId = StpKit.ADMIN.getLoginIdByToken(tokenValue);
        Object userId = StpKit.USER.getLoginIdByToken(tokenValue);

        if (adminId != null) {
            httpRequest.setAttribute("role", "ADMIN");
            httpRequest.setAttribute("id", adminId);
            return true;
        } else if (userId != null) {
            httpRequest.setAttribute("role", "USER");
            httpRequest.setAttribute("id", userId);
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();

        Object id = httpRequest.getAttribute("id");
        Object role = httpRequest.getAttribute("role");

        log.info("{} with ID {} connected to WebSocket", role, id);
    }
}
