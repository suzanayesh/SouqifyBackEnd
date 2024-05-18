package com.code.auth.config;

import com.code.auth.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

  private UserInfo userInfo;  // Add a reference to UserInfo
  private String name;
  private String password;
  private List<GrantedAuthority> authorities;

  public UserInfoDetails(UserInfo userInfo) {
    this.userInfo = userInfo;  // Store the entire UserInfo object
    this.name = userInfo.getName();
    this.password = userInfo.getPassword();
    this.authorities = userInfo.getRole().getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
  }

  // Getter for UserInfo
  public UserInfo getUserInfo() {
    return this.userInfo;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
