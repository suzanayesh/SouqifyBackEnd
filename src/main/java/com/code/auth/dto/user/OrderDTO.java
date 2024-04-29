package com.code.auth.dto.user;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalPrice;
    private List<OrderItemDTO> orderItems;

}
