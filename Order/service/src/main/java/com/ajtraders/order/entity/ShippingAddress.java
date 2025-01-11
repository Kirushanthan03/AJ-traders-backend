package com.ajtraders.order.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_addresses")
@Data
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String country;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;



    // PrePersist method
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}