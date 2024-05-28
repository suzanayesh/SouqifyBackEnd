package com.code.auth.config;
import com.code.auth.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

  private String name;
  private String password;

  private int id;
  private List<GrantedAuthority> authorities;

  public UserInfoDetails(UserInfo userInfo) {
    name = userInfo.getName();
    password = userInfo.getPassword();
    id = userInfo.getId();
//    authorities = Arrays.stream(userInfo.getRoles().split(","))
//      .map(SimpleGrantedAuthority::new)
//      .collect(Collectors.toList());

    authorities = userInfo.getRole().getPermissions().stream()
      .map(permission -> new SimpleGrantedAuthority(permission.getName()))
      .collect(Collectors.toList());
    //functional programming
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

  public int getId() {
    return this.id;
  }
}
