package com.code.auth.controller;

import com.code.auth.dto.user.UserProfileAddRateCommentDto;
import com.code.auth.dto.user.UserProfileUpdateDto;
import com.code.auth.entity.Rating;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ErrorResponse;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import com.code.auth.service.RatingService;
import com.code.auth.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private RatingService ratingService;
    @PostMapping("/rate")
    public ResponseEntity<?> addRating(@RequestBody UserProfileAddRateCommentDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        Rating rating = ratingService.addRating(dto, userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "userProfile", rating.getUserProfile().getUsername(),
                "rating", rating.getRating(),
                "comment", rating.getComment()
        ));
    }

//    @GetMapping("/ratings")
//    public ResponseEntity<?> getRatingsForUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
//        Long userId = (long) ((UserInfo) userDetails).getId();
//        List<Rating> ratings = ratingService.getRatingsForUserProfile(userId);
//        List<Map<String, ? extends Serializable>> response = ratings.stream().map(rating -> Map.of(
//                "username", rating.getRater().getName(),
//                "rating", rating.getRating(),
//                "comment", rating.getComment()
//        )).collect(Collectors.toList());
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping("/addrateComment")
//    public ResponseEntity<?> addRateAndComment(@RequestBody UserProfileAddRateCommentDto rateCommentDto) {
//        try {
//            UserProfile updatedProfile = userProfileService.addRatingAndComment(rateCommentDto);
//            return ResponseEntity.ok().body(new ApiResponse(true, "Rating and comment added successfully", updatedProfile));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to add rating and comment", null));
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
