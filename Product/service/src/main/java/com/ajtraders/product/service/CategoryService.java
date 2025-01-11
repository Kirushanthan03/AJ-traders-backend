package com.ajtraders.product.service;

import com.ajtraders.product.dto.CategoryDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.entity.Category;
import com.ajtraders.product.exception.ServiceException;
import com.ajtraders.product.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public ResponseDto addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryRepository.save(category);
        return new ResponseDto("Category created successfully");
    }

    public List<CategoryDTO> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(category, categoryDTO);
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested category is not available", "Not Found", HttpStatus.NOT_FOUND));
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }

    public ResponseDto deleteById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested category is not available", "Not Found", HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
        return new ResponseDto("The category has been deleted successfully");
    }

    public ResponseDto updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested category is not available", "Not Found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties(categoryDTO, category);
        categoryRepository.save(category);
        return new ResponseDto("The category has been updated successfully");
    }
}
