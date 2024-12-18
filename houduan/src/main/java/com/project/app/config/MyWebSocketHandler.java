package com.project.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    private static final Map<String, UserInfo> USER_INFO = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);
        String type = (String) messageMap.get("type");

        if ("connect".equals(type)) {
            // 处理用户连接
            String username = (String) messageMap.get("username");
            String avatar = (String) messageMap.get("avatar");
            USER_INFO.put(username, new UserInfo(username, avatar));
            SESSIONS.put(username, session);

            // 广播在线用户列表
            broadcastUserList();
        } else if ("message".equals(type)) {
            // 处理普通消息
            String to = (String) messageMap.get("to");
            String from = (String) messageMap.get("from");
            String content = (String) messageMap.get("content");
            String timestamp = (String) messageMap.get("timestamp");

            Map<String, Object> responseMessage = new ConcurrentHashMap<>();
            responseMessage.put("type", "message");
            responseMessage.put("from", from);
            responseMessage.put("content", content);
            responseMessage.put("timestamp", timestamp);

            WebSocketSession targetSession = SESSIONS.get(to);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseMessage)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = null;
        for (Map.Entry<String, WebSocketSession> entry : SESSIONS.entrySet()) {
            if (entry.getValue().getId().equals(session.getId())) {
                username = entry.getKey();
                break;
            }
        }

        if (username != null) {
            SESSIONS.remove(username);
            USER_INFO.remove(username);
            broadcastUserList();
        }
    }

    private void broadcastUserList() throws Exception {
        List<Map<String, String>> userList = new ArrayList<>();
        for (UserInfo userInfo : USER_INFO.values()) {
            Map<String, String> user = new ConcurrentHashMap<>();
            user.put("username", userInfo.username);
            user.put("avatar", userInfo.avatar);
            userList.add(user);
        }

        Map<String, Object> message = new ConcurrentHashMap<>();
        message.put("type", "userList");
        message.put("users", userList);

        String messageJson = objectMapper.writeValueAsString(message);
        for (WebSocketSession session : SESSIONS.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(messageJson));
            }
        }
    }

    private static class UserInfo {
        String username;
        String avatar;

        UserInfo(String username, String avatar) {
            this.username = username;
            this.avatar = avatar;
        }
    }
}