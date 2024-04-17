package com.code.auth.controller;

import com.code.auth.dto.AuthRequest;
import com.code.auth.dto.user.UserDto;
import com.code.auth.entity.UserInfo;
import com.code.auth.lookup.ApiResponse;
import com.code.auth.lookup.Response;
import com.code.auth.service.JwtService;
import com.code.auth.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
  @Autowired
  private UserInfoService service;

  @Autowired
  private JwtService jwtService;
  private final UserInfoService userService;

  @Autowired
  public UserController(UserInfoService userService) {
    this.userService = userService;
  }

  @Autowired
  private AuthenticationManager authenticationManager;

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome this endpoint is not secure";
  }

  @PostMapping("/signup")
  public String addNewUser(@RequestBody UserDto userDto) {
    return service.addUser(userDto);
  }


  @GetMapping("/user/RetailerProfile")
  @PreAuthorize("hasAuthority('RETAILER_PER')")
  public ResponseEntity<ApiResponse<UserInfo>> retailerProfile(@AuthenticationPrincipal UserDetails userDetails) {
    Optional<UserInfo> optionalUserInfo = userService.getUserInfoByName(userDetails.getUsername());

    if (optionalUserInfo.isPresent()) {
      UserInfo userInfo = optionalUserInfo.get();
      System.out.println(userInfo); // Or use a proper logger

      return ResponseEntity.ok(Response.createSuccessResponse(userInfo).getBody());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @GetMapping("/user/SupplierProfile")
  @PreAuthorize("hasAuthority('SUPPLIER_PER')")
  public ResponseEntity<ApiResponse<UserInfo>> supplierProfile(@AuthenticationPrincipal UserDetails userDetails) {
    Optional<UserInfo> optionalUserInfo = userService.getUserInfoByName(userDetails.getUsername());

    if (optionalUserInfo.isPresent()) {
      UserInfo userInfo = optionalUserInfo.get();
      System.out.println(userInfo); // Or use a proper logger

      return ResponseEntity.ok(Response.createSuccessResponse(userInfo).getBody());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @PutMapping("user/Supplier/{id}")
  @PreAuthorize("hasAuthority('SUPPLIER_PER')")
  public ResponseEntity<UserInfo> updateUser(@PathVariable Long id, @RequestBody UserInfo user) {
    UserInfo updatedUser = userService.updateUser(id, user);
    if (updatedUser != null) {
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("admin/{id}")
  @PreAuthorize("hasAuthority('ADMIN_PER')")
  public ResponseEntity<UserInfo> updateAdmin(@PathVariable Long id, @RequestBody UserInfo user) {
    UserInfo updatedUser = userService.updateUser(id, user);
    if (updatedUser != null) {
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.notFound().build();
  }
  @PutMapping("user/retailer/{id}")
  @PreAuthorize("hasAuthority('RETAILER_PER')")
  public ResponseEntity<UserInfo> updateUser2(@PathVariable Long id, @RequestBody UserInfo user) {
    UserInfo updatedUser = userService.updateUser(id, user);
    if (updatedUser != null) {
      return ResponseEntity.ok(updatedUser);
    }
    return ResponseEntity.notFound().build();
  }
  @GetMapping("/search/{name}")
  public ResponseEntity<UserInfo> getUserByUsername(@PathVariable String name) {
    return userService.getUserByName(name)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/admin/users")
  @PreAuthorize("hasAuthority('ADMIN_PER')")
  public ResponseEntity<List<UserInfo>> getAllUsers() {
    List<UserInfo> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
    String message = "A user with the given details already exists.";
    return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
  }
  @GetMapping("/admin/AdminProfile")
  @PreAuthorize("hasAuthority('ADMIN_PER')")
  public String adminProfile() {
    return "Welcome to Admin Profile";
  }

  @DeleteMapping("/admin/{id}")
  @PreAuthorize("hasAuthority('ADMIN_PER')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("Attempting to delete user with ID: {}", id);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Logged in user details: {}", authentication);

    if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN_PER"))) {
      log.warn("User does not have the required authority to delete users.");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    boolean deleted = userService.deleteUser(id);
    if (deleted) {
      log.info("User deleted successfully.");
      return ResponseEntity.ok().build();
    } else {
      log.warn("User with ID {} not found.", id);
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/login")
  public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
      if (authentication.isAuthenticated()) {
        return jwtService.generateToken(authRequest.getUsername());
      } else {
        throw new UsernameNotFoundException("Invalid user request!");
      }
    } catch (BadCredentialsException e) {
      if (e.getMessage().contains("Bad credentials")) {
        // Handle case where either username or password is incorrect

        throw new BadCredentialsException("Invalid username or password");
      } else {
        // Handle case where username is not found
        log.error("Username not found");
        throw new UsernameNotFoundException("Username not found");
      }
    } catch (UsernameNotFoundException e) {
      // Handle case where username is not found
      log.error("Username not found");
      throw new UsernameNotFoundException("Username not found");
    } catch (AuthenticationException e) {
      // Handle other authentication exceptions
      log.error("Authentication failed: " + e.getMessage());
      throw new AuthenticationException("Authentication failed: " + e.getMessage()) {};
    }
  }






}
