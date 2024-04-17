//package com.code.auth.dto.user;
//
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class CartItemDTO {
//    private Long productId;
//    private int quantity;
//    private double price;
//    private Color color;
//    private String productName;
//    private double total;
//
//    public void setColorFromString(String colorStr) {
//        if (colorStr != null && !colorStr.isEmpty()) {
//            try {
//                this.color = Color.valueOf(colorStr.toUpperCase()); // Convert string to enum safely
//            } catch (IllegalArgumentException e) {
//                throw new IllegalArgumentException("Invalid color: " + colorStr);
//            }
//        }
//    }
//
//}
