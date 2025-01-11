package com.ajtraders.product.controller;

import com.ajtraders.product.dto.ProductDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;
    @GetMapping("/say-hi")
    public String sayHi() {

        return "Hello from product Service!";
    }

    @PostMapping("/add")
    public ResponseDto addProduct(@RequestBody @Valid ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }
    @GetMapping("/getAll")
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }
    @GetMapping("/getById/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @DeleteMapping ("/deleteById/{id}")
    public ResponseDto deleteById(@PathVariable Long id) {
        return productService.deleteById(id);
    }
    @PutMapping("/update/{id}")
    public ResponseDto updateProduct(@PathVariable Long id,@RequestBody @Valid ProductDTO productDTO) {
        return productService.updateProduct(id,productDTO);
    }

    @GetMapping("/featured")
    public List<ProductDTO> featuredProducts() {
        return productService.featuredProducts();
    }
}
