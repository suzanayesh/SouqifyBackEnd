package com.code.auth.controller;

import com.code.auth.dto.user.OrderDTO;
import com.code.auth.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Map<String, Object>> createOrder(@PathVariable Long userId, @RequestBody OrderDTO orderDTO) {
        // Set the userId from the path variable
        orderDTO.setUserId(userId);

        // Set the order date manually
        orderDTO.setOrderDate(LocalDateTime.now());
        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        // Calculate subtotal, tax, and total
        double subtotal = createdOrder.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
                .sum();
        double tax = subtotal * 0.1; // Assuming a 10% tax rate
        double total = subtotal + tax;

        // Create the response map
        Map<String, Object> response = new HashMap<>();
        response.put("orderDateTime", createdOrder.getOrderDate());
        response.put("orderStatus", createdOrder.getStatus());
        response.put("orderId", createdOrder.getId());
        response.put("userId", createdOrder.getUserId());
        response.put("orderName", createdOrder.getOrderName());
        response.put("subtotal", subtotal);
        response.put("tax", tax);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }
}
