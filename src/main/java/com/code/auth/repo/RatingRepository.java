package com.code.auth.repo;

import com.code.auth.entity.Rating;
import com.code.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserProfile(UserProfile userProfile);
}
