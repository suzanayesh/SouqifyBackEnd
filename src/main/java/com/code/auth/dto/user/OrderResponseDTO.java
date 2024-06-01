package com.code.auth.dto.user;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String orderName;
    private String orderStatus;
    private String orderDate;



}
