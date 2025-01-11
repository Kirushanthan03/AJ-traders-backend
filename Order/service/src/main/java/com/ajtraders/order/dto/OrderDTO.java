package com.ajtraders.order.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private Long id; // Typically auto-generated, no validation needed here.

    @NotNull(message = "User ID is required")

    private Long userId;

    @NotEmpty(message = "At least one product must be included in the order")
    private List<Long> productIds;



    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    private String country;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City name must not exceed 100 characters")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be a positive value")
    private Double totalPrice;

    // No need to include createdAt and updatedAt as they are system-generated.
}
