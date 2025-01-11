package com.ajtraders.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs")
@Data
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "change_quantity", nullable = false)
    private Integer changeQuantity;

    @Column(nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;




    // PrePersist method
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}