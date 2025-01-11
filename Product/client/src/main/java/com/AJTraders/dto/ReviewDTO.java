package com.AJTraders.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDTO {

    private Long id; // Typically auto-generated, no validation needed here.

//    @NotBlank(message = "User ID is required")
//    @Size(max = 255, message = "User ID must not exceed 255 characters")
//    private String userId;

    @NotNull(message = "Product ID is required")
    private Long productId; // Reference to the product entity using its ID.

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "Review text must not exceed 1000 characters")
    private String reviewText; // Optional, allows for a longer review text.
}
