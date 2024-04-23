package com.code.auth.repo;

import com.code.auth.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Here, Long is the type of the primary key of Order

    // You can define custom query methods here as needed

}

