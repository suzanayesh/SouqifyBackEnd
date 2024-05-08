package com.code.auth.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Entity
@Table(name = "products1")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1024)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private String brand;

    @Transient
    private List<String> availableSizes;

    @Transient
    private List<String> availableColors;

    @Column(name = "sizes", length = 1024) // JSON string
    private String sizesJson;

    @Column(name = "colors", length = 1024) // JSON string
    private String colorsJson;

    public void setAvailableSizes(List<String> sizes) {
        this.availableSizes = sizes;
        this.sizesJson = toJson(sizes);
    }

    public void setAvailableColors(List<String> colors) {
        this.availableColors = colors;
        this.colorsJson = toJson(colors);
    }

    public List<String> getAvailableSizes() {
        return fromJson(this.sizesJson, List.class);
    }

    public List<String> getAvailableColors() {
        return fromJson(this.colorsJson, List.class);
    }

    private static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in JSON writing", e);
        }
    }

    private static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in JSON reading", e);
        }
    }
}
