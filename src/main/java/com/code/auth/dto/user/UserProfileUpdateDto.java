package com.code.auth.dto.user;

import lombok.Data;

@Data
public class UserProfileUpdateDto {
    private String username;          // Username can be included if it needs to be updated or just passed through

    private String email;
    private String location;
    private String storeWebLink;
    private String phoneNumber;
    private String description;
    private String socialMediaFacebook;
    private String socialMediaTelegram;
    private String socialMediaInstagram;
//    private Integer rating;           // Assuming the rating is an integer from 1 to 5
    private String profileImage;      // URL for the profile image
    private String coverImage;        // URL for the cover image
    //    private String comment;
}
