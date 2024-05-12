package com.code.auth.service;

import com.code.auth.entity.Post;
import com.code.auth.entity.UserProfile;
import com.code.auth.repo.PostRepository;
import com.code.auth.repo.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public Post createPost(Long userProfileId, String content) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userProfileId);
        if (!userProfileOptional.isPresent()) {
            throw new RuntimeException("User profile not found");
        }
        UserProfile userProfile = userProfileOptional.get();
        Post post = new Post(content, userProfile);
        return postRepository.save(post);
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
