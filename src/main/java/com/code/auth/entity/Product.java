package com.code.auth.entity;



import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products1")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
    @Column(nullable = false, unique = true)
    private String productName;

    @Column(length = 1024) // Assuming a lengthy description might be needed
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Adjust 'user_id' to match your database column name
    private UserInfo user;

    // Constructors, Getters, and Setters


    @Column(nullable = false)
    private int stockQuantity;
    @Column(nullable = false)
    private String brand;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    @Enumerated(EnumType.STRING)
    private Set<Size> availableSizes;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Set<Color> availableColors;






}