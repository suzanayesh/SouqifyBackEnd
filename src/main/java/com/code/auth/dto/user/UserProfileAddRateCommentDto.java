package com.code.auth.dto.user;

import lombok.Data;

@Data
public class UserProfileAddRateCommentDto {
    private Long userId;  // ID of the user profile to rate/comment on
    private int rating;  // Rating given by the visitor
    private String comment;  // Comment made by the visitor
}
