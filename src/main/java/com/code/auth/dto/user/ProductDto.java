package com.code.auth.dto.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private Long modelNumber;
    private String productName;
    private String description;
    private Double price;
    private String brand;
    private Long categoryId;
    private int stockQuantity;
    private List<String> availableSizes;
    private List<String> availableColors;
}