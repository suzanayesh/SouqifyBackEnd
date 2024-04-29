package com.code.auth.repo;

import com.code.auth.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Additional custom query methods can be defined here
}
