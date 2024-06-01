package com.code.auth.dto.user;

import java.util.List;

public class AllCartItemsDTO {

    private Long cartItemId;

    private Long productId;
    private int quantity;
    private List<String> colors;
    private List<String> sizes;

    private double total;

    private Long modelNumber;


    public AllCartItemsDTO() {
    }

    public AllCartItemsDTO(Long cartItemId, Long productId, int quantity, List<String> colors, List<String> sizes, double total, Long modelNumber) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.colors = colors;
        this.sizes = sizes;
        this.total = total;
        this.modelNumber = modelNumber;
    }

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

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(Long modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}
