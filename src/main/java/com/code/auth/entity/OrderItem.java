package com.code.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_per_unit", nullable = false)
    private double pricePerUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_item_colors", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "color")
    private List<String> colors;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_item_sizes", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "size")
    private List<String> sizes;
}
