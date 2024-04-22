package com.code.auth.repo;
import java.util.Optional;

import com.code.auth.entity.Cart;
import com.code.auth.entity.CartItem;
import com.code.auth.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

