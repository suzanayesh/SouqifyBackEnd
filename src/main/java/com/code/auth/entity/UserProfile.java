package com.code.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}