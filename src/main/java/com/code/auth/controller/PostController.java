package com.code.auth.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.code.auth.dto.user.PostDto;
import com.code.auth.entity.Post;
import com.code.auth.entity.UserProfile;
import com.code.auth.repo.UserProfileRepository;
import com.code.auth.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private  UserProfileRepository userProfileRepository;
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Retrieve the username from the Authentication object

        UserProfile userProfile = userProfileRepository.findByUsername(username); // Retrieve user profile by username
        if (userProfile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User profile not found");
        }

        Post post = postService.createPost(userProfile.getProfileId(), postDto.getContent());
        PostDto responseDto = convertToDto(post);

        // Create a response map to include both the message and the post details
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Your post is published successfully");
        response.put("post", responseDto);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(postDtos);
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setUsername(post.getUserProfile().getUsername());
        postDto.setProfileImage(post.getUserProfile().getProfileImage());
        postDto.setContent(post.getContent());
        postDto.setPostedAt(post.getPostedAt());
        return postDto;
    }
}
