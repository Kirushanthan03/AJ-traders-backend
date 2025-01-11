package com.ajtraders.product.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class InventoryLogDTO {

    private Long id; // Typically auto-generated, so no validation is applied.

    @NotNull(message = "Product ID is required")
    private Long productId; // Reference to the product entity using its ID.

    @NotNull(message = "Change quantity is required")
    @Min(value = 1, message = "Change quantity must be at least 1")
    private Integer changeQuantity;

    @NotBlank(message = "Reason is required")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;
}
