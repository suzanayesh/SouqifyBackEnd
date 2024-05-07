package com.code.auth.repo;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Here, Long is the data type of the primary key of UserProfile which is userId
    Optional<UserProfile> findByUser(UserInfo user);

    // You can define custom methods here, for example:
//    UserProfile findByUserId(Long userId);

    // Add other custom methods if needed
}
