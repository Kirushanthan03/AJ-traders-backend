package com.ajtraders.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private long productId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters and Setters

    // PrePersist method
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}