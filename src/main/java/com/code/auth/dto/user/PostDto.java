package com.code.auth.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class PostDto {

    private Long postId;
    private String username;
    private String profileImage;
    private String content;
    private LocalDateTime postedAt;

}
