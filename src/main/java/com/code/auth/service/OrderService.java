package com.code.auth.service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.code.auth.dto.user.OrderDTO;
import com.code.auth.dto.user.OrderItemDTO;
import com.code.auth.entity.*;
import com.code.auth.repo.OrderRepository;
import com.code.auth.repo.ProductRepository;
import com.code.auth.repo.UserInfoRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserInfoRepository userRepository;
    private final ProductRepository productRepository;
    public OrderService(OrderRepository orderRepository, UserInfoRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }


    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setStatus(OrderStatus.valueOf(orderDTO.getStatus())); // Assuming status is passed as a string
        existingOrder.setOrderDate(orderDTO.getOrderDate());
        existingOrder.setTotalPrice(orderDTO.getTotalPrice());

        Set<OrderItem> updatedItems = orderDTO.getOrderItems().stream()
                .map(dto -> convertToOrderItemEntity(dto, existingOrder)) // pass existingOrder as the order
                .collect(Collectors.toSet());

        existingOrder.setOrderItems(updatedItems); // Set the updated items

        return orderRepository.save(existingOrder);
    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO dto, Order order) {
        OrderItem item = new OrderItem();
        item.setOrder(order); // Ensure the order is set
        item.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID")));
        item.setQuantity(dto.getQuantity());
        item.setPricePerUnit(dto.getPricePerUnit());
        return item;
    }



//    public Order createOrder(OrderDTO orderDTO) {
//        Order order = new Order();
//        order.setUser(userRepository.findById(orderDTO.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")));
//        order.setOrderDate(orderDTO.getOrderDate());
//        order.setTotalPrice(orderDTO.getTotalPrice());
//        Set<OrderItem> items = orderDTO.getOrderItems().stream()
//                .map(dto -> convertToOrderItemEntity(dto, order))
//                .collect(Collectors.toSet());
//
//        order.setOrderItems(items); // Set the items with back-reference to order
//
//        return orderRepository.save(order);
//    }
//public OrderDTO createOrder(int userId, OrderDTO orderDTO) {
//    Order order = new Order();
//    order.setUser(userId);
//    order.setOrderDate(LocalDateTime.now());
//    order.setStatus(OrderStatus.valueOf("PENDING"));
//
//    double total = 0;
//    Set<OrderItem> orderItems = new HashSet<>();
//    for (OrderItemDTO item : orderDTO.getOrderItems()) {
//        Product product = productRepository.findById(item.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
//        OrderItem orderItem = new OrderItem();
//        orderItem.setProduct(product);
//        orderItem.setQuantity(item.getQuantity());
//        orderItem.setPricePerUnit(item.getPricePerUnit());
//        orderItem.setTotalPrice(item.getPricePerUnit() * item.getQuantity());
//        orderItems.add(orderItem);
//        total += orderItem.getTotalPrice();
//    }
//    order.setOrderItems(orderItems);
//    order.setTotalPrice(total);
//
//    orderRepository.save(order);
//
//    OrderDTO responseDTO = new OrderDTO();
//    responseDTO.setOrderDate(order.getOrderDate());
//    responseDTO.setStatus(order.getStatus().toString());
//    responseDTO.setTotalPrice(order.getTotalPrice());
//    responseDTO.setOrderItems(orderDTO.getOrderItems()); // Assuming you want to return the items as they were sent
//    return responseDTO;
//}
public OrderDTO createOrder(OrderDTO orderDTO, Long userId) {
    UserInfo user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    Order order = new Order();
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.valueOf("PENDING"));
    order.setUser(user); // Set the user if there's a relationship

    double total = 0;
    Set<OrderItem> orderItems = new HashSet<>();
    for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(itemDTO.getQuantity());
        item.setPricePerUnit(itemDTO.getPricePerUnit());
        double itemTotal = itemDTO.getQuantity() * itemDTO.getPricePerUnit();
        total += itemTotal;
        orderItems.add(item);
    }
    order.setOrderItems(orderItems);
    order.setTotalPrice(total); // Set the total price of the order
    orderRepository.save(order);

    orderDTO.setUserId(order.getOrderId());
    orderDTO.setTotalPrice(total);
    orderDTO.setOrderDate(order.getOrderDate());
    orderDTO.setStatus(String.valueOf(order.getStatus()));
    return orderDTO;  // Return the completed OrderDTO
}



    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}

