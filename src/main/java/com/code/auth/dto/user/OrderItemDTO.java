package com.code.auth.dto.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
    private Double pricePerUnit;
    private List<String> colors;
    private List<String> sizes;




}

