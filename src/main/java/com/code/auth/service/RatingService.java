package com.code.auth.service;

import com.code.auth.dto.user.UserProfileAddRateCommentDto;
import com.code.auth.entity.Rating;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.repo.RatingRepository;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Transactional
    public Rating addRating(UserProfileAddRateCommentDto dto, String raterUsername) {
        UserInfo rater = userInfoRepository.findByName(raterUsername)
                .orElseThrow(() -> new RuntimeException("Rater not found"));
        UserProfile userProfile = userProfileRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        Rating rating = new Rating();
        rating.setUserProfile(userProfile);
        rating.setRater(rater);
        rating.setRating(dto.getRating());
        rating.setComment(dto.getComment());

        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        return ratingRepository.findByUserProfile(userProfile);
    }
}
