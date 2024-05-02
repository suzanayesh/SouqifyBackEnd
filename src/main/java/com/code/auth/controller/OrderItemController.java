package com.code.auth.controller;

import java.util.List;

import com.code.auth.dto.user.OrderItemDTO;
import com.code.auth.entity.OrderItem;
import com.code.auth.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
//        try {
//            OrderItemDTO orderItemDTO = orderItemService.findOrderItemDTOById(id);
//            return ResponseEntity.ok(orderItemDTO);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

//    @GetMapping
//    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
//        List<OrderItem> orderItems = orderItemService.findAllOrderItems();
//        return ResponseEntity.ok(orderItems);
//    }
    // @GetMapping("/{id}")
    // public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
    //     try {
    //         OrderItemDTO orderItemDTO = orderItemService.findOrderItemDTOById(id);
    //         return ResponseEntity.ok(orderItemDTO);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
    // @GetMapping("/{id}")
    // public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
    //     return orderItemService.findOrderItemById(id)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        OrderItem newOrderItem = orderItemService.saveOrderItem(orderItem);
        return ResponseEntity.ok(newOrderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem orderItemDetails) {
        OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItemDetails);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok().build();
    }
}
