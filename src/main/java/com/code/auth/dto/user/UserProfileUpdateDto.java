package com.code.auth.dto.user;

import lombok.Data;

@Data
public class UserProfileUpdateDto {
    private String email;
    private String location;
    private String storeWebLink;
    private String phoneNumber;
    private String description;
    private String socialMediaFacebook;
    private String socialMediaTelegram;
    private String socialMediaInstagram;
    // Getters and Setters
}
