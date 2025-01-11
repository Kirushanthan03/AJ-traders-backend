package com.ajtraders.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;



    // PrePersist method
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}