package com.code.auth.dto.user;


import java.util.Set;

import com.code.auth.entity.Color;
import com.code.auth.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private Long categoryId; // Assuming each product is associated with a category
    private int stockQuantity;  // Add the stockQuantity field
    private Set<Size> availableSizes;
    private Set<Color> availableColors;








}
