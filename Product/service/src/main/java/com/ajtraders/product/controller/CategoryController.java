package com.ajtraders.product.controller;

import com.ajtraders.product.dto.CategoryDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/product/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/say-hi")
    public String sayHi() {
        return "Hello from Category Service!";
    }

    @PostMapping("/add")
    public ResponseDto addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }

    @GetMapping("/getAll")
    public List<CategoryDTO> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/getById/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseDto deleteById(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

    @PostMapping("/update/{id}")
    public ResponseDto updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }
}
