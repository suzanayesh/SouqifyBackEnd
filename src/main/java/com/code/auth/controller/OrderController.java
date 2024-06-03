package com.code.auth.controller;

import com.code.auth.dto.user.*;
import com.code.auth.entity.Order;
import com.code.auth.service.OrderService;
import com.code.auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserInfoService userInfoService;

    @DeleteMapping("/create")
    public ResponseEntity<?> createOrder() {
        Order result = orderService.createOrder();
        return ResponseEntity.ok(result);
    }
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
//        OrderDTO orderDTO = orderService.getOrderById(orderId);
//        return ResponseEntity.ok(orderDTO);
//    }
@GetMapping("/user")
public ResponseEntity<List<OrderResponseDTO>> getAllOrdersForUser() {
    List<OrderResponseDTO> orders = orderService.getAllOrdersForUser();
    return ResponseEntity.ok(orders);
}
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateDTO statusUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, statusUpdateDTO.getOrderStatus(), userDetails);
        return ResponseEntity.ok(updatedOrder);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDTO);
    }
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<List<OrderResponseDTO>> getLastThreeOrders() {
        List<OrderResponseDTO> orders = orderService.getLastThreeOrdersForUser();
        return ResponseEntity.ok(orders);
    }
}
