package com.code.auth.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.code.auth.entity.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketIOService {
    private final SocketIOServer server;
    private final Map<String, SocketIOClient> userSessions = new ConcurrentHashMap<>();

    @Autowired
    public SocketIOService(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("custom-connect", String.class, onCustomConnect());
        server.addEventListener("send-message", Message.class, onSendMessage());
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("Client disconnected: " + client.getSessionId());
        userSessions.values().remove(client);
    }

    public DataListener<String> onCustomConnect() {
        return (client, username, ackSender) -> {
            System.out.println("Custom connect event received for user: " + username);
            userSessions.put(username, client);
        };
    }

    public DataListener<Message> onSendMessage() {
        return (client, data, ackSender) -> {
            System.out.println("Message received from client: " + client.getSessionId() + " with data: " + data);
            // Send message to the specific recipient
            SocketIOClient recipientClient = userSessions.get(data.getToUser());
            if (recipientClient != null) {
                recipientClient.sendEvent("get-response", data);
                System.out.println("Message sent to recipient: " + data);
            }
        };
    }
}