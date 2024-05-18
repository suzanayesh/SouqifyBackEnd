package com.code.auth.controller;

import com.code.auth.dto.AuthRequest;
import com.code.auth.dto.user.JwtResponseDTO;
import com.code.auth.dto.user.RefreshTokenRequestDTO;
import com.code.auth.dto.user.SimpleUserInfo;
import com.code.auth.dto.user.UserDto;
import com.code.auth.entity.*;
import com.code.auth.exception.EmailExistsException;
import com.code.auth.exception.ErrorResponse;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.exception.UsernameExistsException;
import com.code.auth.lookup.ApiResponse;
import com.code.auth.lookup.Response;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import com.code.auth.service.JwtService;
import com.code.auth.service.RefreshTokenService;
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

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UserController {
  @Autowired
  private UserInfoService service;
  @Autowired
  private UserProfileRepository userProfileRepository;
  @Autowired
  private JwtService jwtService;
  private final UserInfoService userService;

  @Autowired
  public UserController(UserInfoService userService) {
    this.userService = userService;
  }

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private AuthenticationManager authenticationManager;

  @GetMapping("/welcome")
  public String welcome() {
    return "Welcome this endpoint is not secure";
  }
  @PostMapping("/signup")
  public ResponseEntity<?> addNewUser(@RequestBody UserDto userDto) {
    try {
      UserInfo newUser = service.addUser(userDto);
      UserProfile userProfile = userProfileRepository.findByUser(newUser)
              .orElseThrow(() -> new Exception("UserProfile not found for user ID: " + newUser.getId()));

      // Assume you have a method to generate a token for a given username
      String token = jwtService.generateToken(newUser.getEmail());

      Map<String, Object> response = new HashMap<>();
      response.put("message", "Account created successfully! Welcome, " + newUser.getName());
      response.put("userId", newUser.getId());
      response.put("username", newUser.getName());
      response.put("email", newUser.getEmail());
      response.put("roleId", newUser.getRole().getId()); // Assuming Role is a field in UserInfo
      response.put("token", token);

      return ResponseEntity.ok(response);
    } catch (EmailExistsException | UsernameExistsException e) {
      return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to create user account"));
    }
  }


//@PostMapping("/signup")
//public ResponseEntity<?> signupUser(@RequestBody UserDto newUser) {
//  Role userRole = roleService.getRoleById(newUser.getRoleId());
//  newUser.setRole(userRole);
//  UserInfo savedUser = service.addUser(newUser);
//
//  if ("retailer".equalsIgnoreCase(userRole.getName())) {
//    Cart newCart = new Cart();
//    newCart.setUser(savedUser);
//    newCart.setItems(new ArrayList<>());
//    cartService.saveCart(newCart);
//  }
//
//  return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//}


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

  //  @GetMapping("/search/{name}")
//  public ResponseEntity<UserInfo> getUserByUsername(@PathVariable String name) {
//    return userService.getUserByName(name)
//            .map(ResponseEntity::ok)
//            .orElseGet(() -> ResponseEntity.notFound().build());
//  }
  @GetMapping("/search/{name}")
  public ResponseEntity<List<SimpleUserInfo>> searchUsersByName(@PathVariable String name) {
    List<SimpleUserInfo> users = userService.findUsersByNameContaining(name);
    if (users.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(users);
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

  @Autowired
  private RefreshTokenService refreshTokenService;
  @PostMapping("/login")
  public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
      );

      if (!authentication.isAuthenticated()) {
        throw new BadCredentialsException("Invalid username or password");
      }

      String token = jwtService.generateToken(authRequest.getUsername());
      UserInfo userInfo = userInfoRepository.findByName(authRequest.getUsername())
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      UserProfile userProfile = userProfileRepository.findByUser(userInfo)
              .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfo.getName());

      Map<String, Object> response = new HashMap<>();
      response.put("accessToken", token);
      response.put("refreshToken", refreshToken.getToken());
      response.put("message", "Welcome " + userInfo.getName() + ", logged in successfully.");
      response.put("userId", userInfo.getId());
      response.put("username", userInfo.getName());
      response.put("roleId", userInfo.getRole().getId()); // Assuming Role is a direct field in UserInfo
      response.put("email", userInfo.getEmail());  // Assuming the email is not null

      return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
      log.error("Authentication failed: Invalid username or password");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password"));
    } catch (AuthenticationException e) {
      log.error("Authentication failed: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authentication failed"));
    }
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
    return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUserInfo)
            .map(userInfo -> {
              String accessToken = jwtService.generateToken(userInfo.getName());
              Map<String, Object> response = new HashMap<>();
              response.put("accessToken", accessToken);
              response.put("refreshToken", refreshTokenRequestDTO.getToken());
              return ResponseEntity.ok(response);
            }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
  }



//  @PostMapping("/login")
//  public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
//    try {
//      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//      if (authentication.isAuthenticated()) {
//        System.out.println("login is working");
//        String token = jwtService.generateToken(authRequest.getUsername());
//
//        return ResponseEntity.ok(Map.of("token", token));
//
//      } else {
//        throw new UsernameNotFoundException("Invalid user request!");
//      }
//    } catch (BadCredentialsException e) {
//      if (e.getMessage().contains("Bad credentials")) {
//        // Handle case where either username or password is incorrect
//
//        throw new BadCredentialsException("Invalid username or password");
//      } else {
//        // Handle case where username is not found
//        log.error("Username not found");
//        throw new UsernameNotFoundException("Username not found");
//      }
//    } catch (UsernameNotFoundException e) {
//      // Handle case where username is not found
//      log.error("Username not found");
//      throw new UsernameNotFoundException("Username not found");
//    } catch (AuthenticationException e) {
//      // Handle other authentication exceptions
//      log.error("Authentication failed: " + e.getMessage());
//      throw new AuthenticationException("Authentication failed: " + e.getMessage()) {};
//    }
//  }






}
