package com.code.auth.service;


import java.util.List;

import com.code.auth.dto.user.OrderItemDTO;
import com.code.auth.entity.OrderItem;
import com.code.auth.repo.OrderItemRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;


@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> findAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItemDTO findOrderItemDTOById(Long id) {
        return orderItemRepository.findById(id)
                .map(this::convertToOrderItemDTO)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
    }

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(orderItem.getProductId());
// Assuming Product is eagerly loaded or handled to prevent LazyInitializationException
        dto.setQuantity(orderItem.getQuantity());
        dto.setPricePerUnit(orderItem.getPricePerUnit());
        return dto;
    }

    // public Optional<OrderItem> findOrderItemById(Long id) {
    //     return orderItemRepository.findById(id);
    //
    public OrderItem getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        Hibernate.initialize(orderItem.getOrder()); // Force initialization of the 'order' proxy
        return orderItem;
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem updateOrderItem(Long id, OrderItem orderItemDetails) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
        existingOrderItem.setQuantity(orderItemDetails.getQuantity());
        existingOrderItem.setPricePerUnit(orderItemDetails.getPricePerUnit());
        // Add any other fields that you need to update
        return orderItemRepository.save(existingOrderItem);
    }

    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
        orderItemRepository.delete(orderItem);
    }
}
