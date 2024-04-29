  package com.code.auth.dto.user;

  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.NoArgsConstructor;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class UserDto {
    private String name;
    private String email;
    private String password;
    private int roleId;
    private String storename;
  }
