package com.code.auth.repo;

import com.code.auth.entity.Category;
import com.code.auth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteByIdAndUserId(Long productId, Long userId);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findAllByUserId(Long userId);
    Optional<Product> findByIdAndUserId(Long productId, Long userId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);
}