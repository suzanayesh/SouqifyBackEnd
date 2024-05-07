package com.code.auth.controller;

import com.code.auth.dto.user.UserProfileUpdateDto;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
}
