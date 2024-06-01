package com.code.auth.dto.user;



import com.code.auth.entity.Color;
import com.code.auth.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long productId;
    private int quantity;
    private List<String> selectedColors;
    private List<String> selectedSizes;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getSelectedColors() {
        return selectedColors;
    }

    public void setSelectedColors(List<String> selectedColors) {
        this.selectedColors = selectedColors;
    }

    public List<String> getSelectedSizes() {
        return selectedSizes;
    }

    public void setSelectedSizes(List<String> selectedSizes) {
        this.selectedSizes = selectedSizes;
    }
}
