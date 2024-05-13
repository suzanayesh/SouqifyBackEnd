package com.code.auth.repo;

import com.code.auth.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    //postrepo
}
