package com.code.auth.service;

import com.code.auth.dto.PartnerDTO;
import com.code.auth.entity.Message;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.repo.MessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    private final UserInfoService userInfoService;

    private final UserProfileService userProfileService;

    public MessageService(MessageRepository messageRepository, UserInfoService userInfoService, UserProfileService userProfileService) {
        this.messageRepository = messageRepository;
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
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

    public List<PartnerDTO> getAllChatPartners(String username) {
        List<String> partners = messageRepository.findAllChatPartners(username);
        List<PartnerDTO> result = new ArrayList<PartnerDTO>();
        for(String partner: partners){
            if(userInfoService.getUserByName(partner).isEmpty()){
                continue;
            }
            UserInfo user = userInfoService.getUserByName(partner).get();
            UserProfile userProfile = userProfileService.getUserProfile(user.getId()).get();
            PartnerDTO dto = new PartnerDTO();
            dto.setName(partner);
            dto.setPicUrl(userProfile.getProfileImage());
            result.add(dto);
        }
        return result;
    }

    public void initiateChat(String fromUser, String toUser) {
        saveMessage(fromUser, toUser, ""); // Save an initial empty message to start the chat
    }
}

