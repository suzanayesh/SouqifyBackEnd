package com.code.auth.dto.user;

import lombok.Data;

import java.util.List;
@Data
public class SupplierDashboardResponseDTO {
    private List<OrderResponseDTO> lastThreeOrders;
    private List<UserProfileDTO> randomRetailers;

}
