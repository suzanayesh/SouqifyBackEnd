package com.code.auth.repo;


import com.code.auth.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Custom queries can be added here if needed
}

