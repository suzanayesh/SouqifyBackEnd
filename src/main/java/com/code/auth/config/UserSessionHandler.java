package com.code.auth.config;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class UserSessionHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String username, WebSocketSession session) {
        sessions.put(username, session);
    }

    public void removeSession(String username) {
        sessions.remove(username);
    }

    public boolean hasSession(String username) {
        return sessions.containsKey(username);
    }

    public WebSocketSession getSession(String username) {
        return sessions.get(username);
    }
}
