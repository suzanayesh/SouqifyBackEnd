package com.code.auth.service;
import com.code.auth.dto.user.UserProfileUpdateDto;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.ResourceNotFoundException;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;  // Repository to manage UserInfo

    @Transactional
    public UserProfile updateProfile(UserProfileUpdateDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Assuming username is unique and used as identifier in UserInfo

        UserInfo userInfo = userInfoRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        UserProfile profile = userProfileRepository.findById((long) userInfo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile not found for user: " + username));

        // Update the UserProfile
        profile.setLocation(updateDto.getLocation());
        profile.setStoreWebLink(updateDto.getStoreWebLink());
        profile.setPhoneNumber(updateDto.getPhoneNumber());
        profile.setDescription(updateDto.getDescription());
        profile.setSocialMediaFacebook(updateDto.getSocialMediaFacebook());
        profile.setSocialMediaTelegram(updateDto.getSocialMediaTelegram());
        profile.setSocialMediaInstagram(updateDto.getSocialMediaInstagram());

//        // Update email if it's changed and not in use
//        if (!userInfo.getEmail().equals(updateDto.getEmail())) {
//            boolean emailExists = userInfoRepository.findByEmail(updateDto.getEmail()).isPresent();
//            if (emailExists) {
//                throw new IllegalStateException("Email already in use.");
//            }
//            userInfo.setEmail(updateDto.getEmail());
            userInfoRepository.save(userInfo);
//        }
//
        return userProfileRepository.save(profile);
    }
}
