package com.code.auth.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponseDto {
    private Long userId;
    private String profileImage;
    private int roleId;
    private String username;
    private String location;
}
