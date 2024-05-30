package com.code.auth.repo;

import com.code.auth.entity.Follow;
import com.code.auth.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(UserProfile follower);
    List<Follow> findByFollowee(UserProfile followee);
}