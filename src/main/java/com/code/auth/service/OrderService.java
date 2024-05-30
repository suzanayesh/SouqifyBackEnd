package com.code.auth.service;

import com.code.auth.dto.user.OrderDTO;
import com.code.auth.dto.user.OrderItemDTO;
import com.code.auth.dto.user.OrderResponseDTO;
import com.code.auth.entity.Order;
import com.code.auth.entity.OrderItem;
import com.code.auth.exception.OrderNotFoundException;
import com.code.auth.repo.OrderItemRepository;
import com.code.auth.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    public OrderDTO updateOrderStatus(Long orderId, String orderStatus, UserDetails userDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        order.setStatus(orderStatus);
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return convertToDTO(order);
    }

    public List<OrderResponseDTO> getAllOrdersForUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderName(order.getOrderName());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setProductId(orderItem.getProductId());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setPricePerUnit(orderItem.getPricePerUnit());
            return orderItemDTO;
        }).collect(Collectors.toList()));
        return orderDTO;
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate().toString());
        dto.setOrderName(order.getOrderName());
        return dto;
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(orderDTO.getStatus());
        order.setOrderName(orderDTO.getOrderName());
        order.setUserId(orderDTO.getUserId());
        order.setOrderItems(orderDTO.getOrderItems().stream().map(orderItemDTO -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemDTO.getProductId());
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setPricePerUnit(orderItemDTO.getPricePerUnit());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList()));
        return order;
    }
}
