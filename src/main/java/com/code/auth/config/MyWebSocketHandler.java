package com.code.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final UserSessionHandler sessionHandler = new UserSessionHandler();
    private final Map<String, String> messageHistory = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            sessionHandler.addSession(username, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            sessionHandler.removeSession(username);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, String> messageData = objectMapper.readValue(payload, Map.class);

        String fromUser = messageData.get("from");
        String toUser = messageData.get("to");
        String content = messageData.get("content");

        if (sessionHandler.hasSession(toUser)) {
            WebSocketSession toSession = sessionHandler.getSession(toUser);
            toSession.sendMessage(new TextMessage(payload));
        }

        saveMessage(fromUser, toUser, content);
    }

    private void saveMessage(String fromUser, String toUser, String content) {
        String messageKey = fromUser + ":" + toUser;
        messageHistory.put(messageKey, content);
    }

    public Map<String, String> getMessageHistory(String fromUser, String toUser) {
        String messageKey = fromUser + ":" + toUser;
        return messageHistory.entrySet().stream()
                .filter(entry -> entry.getKey().equals(messageKey))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
