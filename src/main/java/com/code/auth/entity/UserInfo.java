package com.code.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity

public class UserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private String password;
  private String storename;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  @JsonIgnore
  private Role role;


  @Column(name = "profile_image")
  private String profileImage;
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private UserProfile userProfile;



}
