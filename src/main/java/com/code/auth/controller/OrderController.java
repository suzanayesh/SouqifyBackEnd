package com.code.auth.controller;

import com.code.auth.dto.user.OrderDTO;
import com.code.auth.entity.Order;
import com.code.auth.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.findAllOrders();
//        return ResponseEntity.ok(orders);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        return orderService.findOrderById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
        @PreAuthorize("hasAuthority('RETAILER_PER')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

//    @PostMapping("/{userId}")
//    @PreAuthorize("hasAuthority('RETAILER_PER')")
//    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO, @PathVariable Long userId) {
//        OrderDTO createdOrder = orderService.createOrder(orderDTO, userId);
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "Your order received successfully");
//        response.put("status", createdOrder.getStatus());
//        response.put("orderDate", createdOrder.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        response.put("total", createdOrder.getTotalPrice());
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
//        try {
//            Order updatedOrder = orderService.updateOrder(id, orderDTO);
//            return ResponseEntity.ok(updatedOrder);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
//        try {
//            orderService.deleteOrder(id);
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
