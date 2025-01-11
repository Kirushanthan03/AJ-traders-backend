package com.AJTraders;


import com.AJTraders.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-client", url = "http://localhost:8092")
public interface ProductClient {
    @GetMapping("api/v1/product/getAll")
    public List<ProductDTO> getProducts();
}