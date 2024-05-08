package com.code.auth.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
public class UserProfile {

    @Id
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserInfo user;

    @Column(name = "location")
    private String location;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "username", nullable = false) // Ensures the username is not nullable and reflects the username from UserInfo
    private String username;
    @Column(name = "store_weblink")
    private String storeWebLink;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "social_media_facebook")
    private String socialMediaFacebook;

    @Column(name = "social_media_telegram")
    private String socialMediaTelegram;

    @Column(name = "social_media_instagram")
    private String socialMediaInstagram;

    @Column(name = "comment")  // New field for additional comments
    private String comment;
    @Column(name = "rating", nullable = true)
    @Min(1) @Max(5)
    private Integer rating;
    @Column(name = "profile_image", nullable = true)
    private String profileImage;  // URL to the profile image

    @Column(name = "cover_image", nullable = true)
    private String coverImage;  // URL to the cover image
    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @PrePersist
    @PreUpdate
    private void loadUsernameFromUser() {
        if (user != null) {
            this.username = user.getName(); // Ensures username is synchronized with UserInfo before save or update
        }
    }
}

