package com.code.auth.dto.user;

import lombok.Data;

@Data
public class UserProfileDTO {
    private Long userId;
    private String userName;
    private String profileImage;
}
