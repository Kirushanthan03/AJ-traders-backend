package com.ajtraders.product.controller;

import com.ajtraders.product.dto.ReviewDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/product/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/say-hi")
    public String sayHi() {
        return "Hello from Review Service!";
    }

    @PostMapping("/add")
    public ResponseDto addReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewService.addReview(reviewDTO);
    }

    @GetMapping("/getAll")
    public List<ReviewDTO> getReviews() {
        return reviewService.getReviews();
    }

    @GetMapping("/getById/{id}")
    public ReviewDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseDto deleteById(@PathVariable Long id) {
        return reviewService.deleteById(id);
    }

    @PostMapping("/update/{id}")
    public ResponseDto updateReview(@PathVariable Long id, @RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewService.updateReview(id, reviewDTO);
    }
}
