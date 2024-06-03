package com.code.auth.repo;

import com.code.auth.entity.ProductPic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductPicRepositpry extends JpaRepository<ProductPic, Long> {
    Optional<ProductPic> findByProductId(Long productId);
}
