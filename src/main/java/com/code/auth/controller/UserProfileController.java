package com.code.auth.controller;

import com.code.auth.dto.user.UserProfileUpdateDto;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ErrorResponse;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userProfile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileUpdateDto updateDto) {
        try {
            UserProfile updatedProfile = userProfileService.updateProfile(updateDto);
            return ResponseEntity.ok().body(new ApiResponse(true, "Profile updated successfully", updatedProfile));
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(false, "User profile not found, failed to update. Try again.", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already in use, failed to update. Try again.", null));
        } catch (Exception e) {
            // General exception handler for any other unexpected errors
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to update profile. Try again.", null));
        }
    }

    // Inner class to handle response objects
    private static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserInfo userInfo = userInfoRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            UserProfile userProfile = userProfileService.getUserProfile((long) userInfo.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserProfile not found for user: " + username));

            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("profileId", userProfile.getProfileId());
            profileMap.put("email", userProfile.getEmail() != null ? userProfile.getEmail() : "");
            profileMap.put("socialMediaTelegram", userProfile.getSocialMediaTelegram() != null ? userProfile.getSocialMediaTelegram() : "");
            profileMap.put("phoneNumber", userProfile.getPhoneNumber() != null ? userProfile.getPhoneNumber() : "");
            profileMap.put("socialMediaInstagram", userProfile.getSocialMediaInstagram() != null ? userProfile.getSocialMediaInstagram() : "");
            profileMap.put("description", userProfile.getDescription() != null ? userProfile.getDescription() : "");
            profileMap.put("location", userProfile.getLocation() != null ? userProfile.getLocation() : "");
            profileMap.put("storeWebLink", userProfile.getStoreWebLink() != null ? userProfile.getStoreWebLink() : "");
            profileMap.put("socialMediaFacebook", userProfile.getSocialMediaFacebook() != null ? userProfile.getSocialMediaFacebook() : "");

            return ResponseEntity.ok(Map.of("profile", profileMap));
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred while retrieving the profile"));
        }
    }
}
