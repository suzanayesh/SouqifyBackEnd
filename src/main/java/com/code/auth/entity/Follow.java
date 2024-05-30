package com.code.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserProfile follower;

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private UserProfile followee;

    @Column(name = "followed_at", nullable = false)
    private LocalDateTime followedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getFollower() {
        return follower;
    }

    public void setFollower(UserProfile follower) {
        this.follower = follower;
    }

    public UserProfile getFollowee() {
        return followee;
    }

    public void setFollowee(UserProfile followee) {
        this.followee = followee;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + follower +
                ", followee=" + followee +
                ", followedAt=" + followedAt +
                '}';
    }
}
