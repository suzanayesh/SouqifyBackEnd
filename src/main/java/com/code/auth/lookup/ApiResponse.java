package com.code.auth.lookup;

import com.code.auth.entity.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse <T>{
  private boolean status;
  private String username;
  private String storename;
  private String email;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "name")
  private Role role;
  private String data;
  private int statusCode;


  // Constructor, getters, and setters
}
