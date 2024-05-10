package com.code.auth.repo;

import com.code.auth.entity.Category;
import com.code.auth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteByIdAndUserId(Long productId, Long userId);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findAllByUserId(Long userId);
    Optional<Product> findByIdAndUserId(Long productId, Long userId);

    List<Product> findAllByCategory(Category category);
}