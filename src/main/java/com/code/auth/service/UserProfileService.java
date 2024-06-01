package com.code.auth.service;

import com.code.auth.dto.user.UserProfileAddRateCommentDto;
import com.code.auth.dto.user.UserProfileDTO;
import com.code.auth.dto.user.UserProfileUpdateDto;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public UserProfile addRatingAndComment(UserProfileAddRateCommentDto dto) {
        UserProfile userProfile = userProfileRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile not found for user ID: " + dto.getUserId()));

        userProfile.setRating(dto.getRating());
        userProfile.setComment(dto.getComment());
        return userProfileRepository.save(userProfile);
    }

    @Transactional(readOnly = true)
    public UserProfile getUserProfileWithRatings() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserInfo userInfo = userInfoRepository.findByName(username)
                .orElseThrow(() -> new Exception("User not found with username: " + username));

        UserProfile userProfile = userProfileRepository.findById((long) userInfo.getId())
                .orElseThrow(() -> new Exception("Profile not found for user: " + username));

        // Ensure ratings are loaded
        userProfile.getRatings().size(); // Access the ratings to ensure they are loaded if needed
        return userProfile;
    }

    @Transactional
    public UserProfile updateProfile(UserProfileUpdateDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Current logged in user's username

        UserInfo userInfo = userInfoRepository.findByName(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUsername));

        // Check if the username needs to be updated and is different from the current one
        if (updateDto.getUsername() != null && !updateDto.getUsername().equals(currentUsername)) {
            // Ensure the new username is not already in use
            boolean usernameExists = userInfoRepository.findByName(updateDto.getUsername()).isPresent();
            if (usernameExists) {
                throw new IllegalStateException("Username already in use, please choose another username.");
            }
            userInfo.setName(updateDto.getUsername()); // Update the username in UserInfo
            userInfoRepository.save(userInfo);
        }

        UserProfile profile = userProfileRepository.findById((long) userInfo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile not found for user: " + currentUsername));

        profile.setLocation(updateDto.getLocation());
        profile.setStoreWebLink(updateDto.getStoreWebLink());
        profile.setPhoneNumber(updateDto.getPhoneNumber());
        profile.setDescription(updateDto.getDescription());
        profile.setSocialMediaFacebook(updateDto.getSocialMediaFacebook());
        profile.setSocialMediaTelegram(updateDto.getSocialMediaTelegram());
        profile.setSocialMediaInstagram(updateDto.getSocialMediaInstagram());
        profile.setProfileImage(updateDto.getProfileImage()); // Handling profile image URL update


        return userProfileRepository.save(profile);
    }

    public Optional<UserProfile> getUserProfile(Long userId) {
        return userProfileRepository.findById(userId);
    }


}
