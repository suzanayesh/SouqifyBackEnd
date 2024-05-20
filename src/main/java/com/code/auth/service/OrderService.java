package com.code.auth.service;

import com.code.auth.dto.user.OrderDTO;
import com.code.auth.entity.*;
import com.code.auth.repo.OrderRepository;
import com.code.auth.repo.ProductRepository;
import com.code.auth.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        UserInfo user = userInfoRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));
        order.setTotalPrice(orderDTO.getTotalPrice());

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPricePerUnit(itemDTO.getPricePerUnit());

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems((Set<OrderItem>) orderItems);

        return orderRepository.save(order);
    }
}
