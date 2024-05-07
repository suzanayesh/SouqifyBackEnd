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
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfileUpdateDto updateDto) {
        try {
            UserProfile updatedProfile = userProfileService.updateProfile(updateDto);
            return ResponseEntity.ok(updatedProfile);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // Or customize the response to convey the issue
        }
    }
}

