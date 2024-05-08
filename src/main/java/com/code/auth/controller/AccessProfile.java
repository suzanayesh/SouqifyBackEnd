package com.code.auth.controller;

import com.code.auth.dto.user.UserProfileAddRateCommentDto;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import com.code.auth.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
public class AccessProfile {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserProfileService userProfileService;
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam Optional<Long> id) {
        try {
            UserProfile userProfile;
            UserInfo userInfo;

            if (id.isPresent()) {
                userProfile = userProfileService.getUserProfile(id.get())
                        .orElseThrow(() -> new ResourceNotFoundException("User profile not found for user ID: " + id.get()));
                userInfo = userProfile.getUser();  // Assuming there's a getUser method to fetch UserInfo from UserProfile
            } else {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                userInfo = userInfoRepository.findByName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
                userProfile = userProfileService.getUserProfile((long) userInfo.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("UserProfile not found for user: " + username));
            }

            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("username", userInfo.getName());  // Add username
            profileMap.put("profileId", userProfile.getProfileId());
            profileMap.put("email", userProfile.getEmail() != null ? userProfile.getEmail() : "");
            profileMap.put("location", userProfile.getLocation() != null ? userProfile.getLocation() : "");
            profileMap.put("storeWebLink", userProfile.getStoreWebLink() != null ? userProfile.getStoreWebLink() : "");
            profileMap.put("phoneNumber", userProfile.getPhoneNumber() != null ? userProfile.getPhoneNumber() : "");
            profileMap.put("description", userProfile.getDescription() != null ? userProfile.getDescription() : "");
            profileMap.put("socialMediaFacebook", userProfile.getSocialMediaFacebook() != null ? userProfile.getSocialMediaFacebook() : "");
            profileMap.put("socialMediaTelegram", userProfile.getSocialMediaTelegram() != null ? userProfile.getSocialMediaTelegram() : "");
            profileMap.put("socialMediaInstagram", userProfile.getSocialMediaInstagram() != null ? userProfile.getSocialMediaInstagram() : "");
            profileMap.put("profileImage", userProfile.getProfileImage());  // Assuming these fields are stored in UserProfile
            profileMap.put("coverImage", userProfile.getCoverImage());

            // Add ratings to the profile response
            List<Map<String, Object>> ratings = userProfile.getRatings().stream().map(rating -> {
                Map<String, Object> ratingDetails = new HashMap<>();
                ratingDetails.put("rater", rating.getRater().getName());
                ratingDetails.put("rating", rating.getRating());
                ratingDetails.put("comment", rating.getComment());
                return ratingDetails;
            }).collect(Collectors.toList());
            profileMap.put("ratings", ratings);

            return ResponseEntity.ok(profileMap);
        } catch (UsernameNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage(), "suggestion", "User not found, please try again with a valid user ID."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred while retrieving the profile"));
        }
    }


    //    @PostMapping("/addrateComment")
//    public ResponseEntity<?> addRateAndComment(@RequestBody UserProfileAddRateCommentDto rateCommentDto) {
//        try {
//            UserProfile updatedProfile = userProfileService.addRatingAndComment(rateCommentDto);
//            return ResponseEntity.ok().body(new UserProfileController.ApiResponse(true, "Rating and comment added successfully", updatedProfile));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(new UserProfileController.ApiResponse(false, "Failed to add rating and comment", null));
//        }
//    }
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
