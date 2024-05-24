package com.code.auth.service;

import com.code.auth.entity.Message;
import com.code.auth.repo.MessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(String fromUser, String toUser, String content) {
        Message message = new Message();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessageHistory(String fromUser, String toUser) {
        List<Message> sentMessages = messageRepository.findByFromUserAndToUserOrderByTimestampAsc(fromUser, toUser);
        List<Message> receivedMessages = messageRepository.findByToUserAndFromUserOrderByTimestampAsc(fromUser, toUser);
        sentMessages.addAll(receivedMessages);
        sentMessages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        return sentMessages;
    }

    public List<String> getAllChatPartners(String username) {
        return messageRepository.findAllChatPartners(username);
    }

    public void initiateChat(String fromUser, String toUser) {
        // Optional: Add logic to check if the chat already exists or any other business logic
        saveMessage(fromUser, toUser, ""); // Save an initial empty message to start the chat
    }
}

