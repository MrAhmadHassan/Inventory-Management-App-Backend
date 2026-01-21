package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Wireless Keyboard
    @Column(nullable = false)
    private String name;

    // KB-001, HUB-002
    @Column(nullable = false, unique = true)
    private String sku;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cid", nullable = false)
    private Category category;

    // Quantity in stock
    @Column(nullable = false)
    private Integer quantity;

    // 79.99
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String status;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp lastUpdatedAt;

}
