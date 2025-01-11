package com.AJTraders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long id; // Typically auto-generated, no validation needed here.

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;

    private Long parentCategoryId; // Reference to the parent category using its ID.

    // No need for validation on `createdAt` and `updatedAt` as they are generated server-side.
}
