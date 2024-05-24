package com.code.auth.controller;

import com.code.auth.entity.Message;
import com.code.auth.service.MessageService;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.saveMessage(message.getFromUser(), message.getToUser(), message.getContent());
    }

    @GetMapping("/history")
    public List<Message> getMessageHistory(@RequestParam String fromUser, @RequestParam String toUser) {
        return messageService.getMessageHistory(fromUser, toUser);
    }

    @GetMapping("/chats")
    public List<String> getAllChats(@RequestParam String username) {
        return messageService.getAllChatPartners(username);
    }
    @PostMapping("/initiate")
    public void initiateChat(@RequestParam String fromUser, @RequestParam String toUser) {
        messageService.initiateChat(fromUser, toUser);
    }
}
