package com.ajtraders.product.service;

import com.ajtraders.product.dto.ProductDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.entity.Product;
import com.ajtraders.product.enums.Category;
import com.ajtraders.product.exception.ServiceException;
import com.ajtraders.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseDto addProduct(ProductDTO productDTO) {
        Category.fromMappedValue(productDTO.getCategory());
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        productRepository.save(product);
        return new ResponseDto("product created successfully");
    }

    public List<ProductDTO> getProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(product, productDTO);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    public ProductDTO getProductById(Long id) {
       Product product = productRepository.findById(id).orElseThrow(()->new ServiceException("The requested product is not available","Not Found", HttpStatus.NOT_FOUND));
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    public ResponseDto deleteById(Long id) {
        productRepository.findById(id).orElseThrow(()->new ServiceException("The requested product is not available","Not Found", HttpStatus.NOT_FOUND));
        productRepository.deleteById(id);
        return  new ResponseDto("The product has been deleted successfully");
    }

    public ResponseDto updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(()->new ServiceException("The requested product is not available","Not Found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties( productDTO,product);
        product.setId(id);
        productRepository.save(product);
        return new ResponseDto("The product has been updated successfully");
    }

    public List<ProductDTO> featuredProducts() {
        List<Product> products = productRepository.findMostRecentProducts();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(product, productDTO);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
