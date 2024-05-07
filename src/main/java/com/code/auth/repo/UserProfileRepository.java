package com.code.auth.repo;
import com.code.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Here, Long is the data type of the primary key of UserProfile which is userId

    // You can define custom methods here, for example:
//    UserProfile findByUserId(Long userId);

    // Add other custom methods if needed
}
