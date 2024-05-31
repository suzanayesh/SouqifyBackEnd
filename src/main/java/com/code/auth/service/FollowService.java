package com.code.auth.service;

import com.code.auth.dto.user.FollowDto;
import com.code.auth.entity.Follow;
import com.code.auth.entity.Role;
import com.code.auth.entity.UserProfile;
import com.code.auth.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.code.auth.repo.FollowRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserInfoService userInfoService;

    public FollowDto follow(Long retailerId, Long supplierId) {
        UserProfile retailer = userProfileRepository.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found"));
        UserProfile supplier = userProfileRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Follow follow = new Follow();
        follow.setFollower(retailer);  // Retailer is the follower
        follow.setFollowee(supplier);  // Supplier is the followee
        follow.setFollowedAt(LocalDateTime.now());
        Follow savedFollow = followRepository.save(follow);
        return convertToDto(savedFollow);
    }

    public List<FollowDto> getFollowers(Long supplierId) {
        UserProfile supplier = userProfileRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return followRepository.findByFollowee(supplier).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FollowDto> getFollowing(Long retailerId) {
        UserProfile retailer = userProfileRepository.findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Retailer not found"));
        return followRepository.findByFollower(retailer).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FollowDto convertToDto(Follow follow) {
        FollowDto followDto = new FollowDto();
        followDto.setId(follow.getId());
        followDto.setFollowerId(follow.getFollower().getProfileId());
        followDto.setFolloweeId(follow.getFollowee().getProfileId());
        followDto.setFollowedAt(follow.getFollowedAt());
        Role role = userInfoService.getCurrentUserInfo().getRole();
        if(role.getId() == 3 ){
            followDto.setUsername(follow.getFollower().getUsername());
            followDto.setProfilePic(follow.getFollower().getProfileImage());
        }else{
            followDto.setUsername(follow.getFollowee().getUsername());
            followDto.setProfilePic(follow.getFollowee().getProfileImage());
        }
        return followDto;
    }
}