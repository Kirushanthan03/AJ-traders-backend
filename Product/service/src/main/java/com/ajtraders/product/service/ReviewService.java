package com.ajtraders.product.service;

import com.ajtraders.product.dto.ReviewDTO;
import com.ajtraders.product.dto.ResponseDto;
import com.ajtraders.product.entity.Review;
import com.ajtraders.product.exception.ServiceException;
import com.ajtraders.product.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ResponseDto addReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        BeanUtils.copyProperties(reviewDTO, review);
        reviewRepository.save(review);
        return new ResponseDto("Review created successfully");
    }

    public List<ReviewDTO> getReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDTO reviewDTO = new ReviewDTO();
            BeanUtils.copyProperties(review, reviewDTO);
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested review is not available", "Not Found", HttpStatus.NOT_FOUND));
        ReviewDTO reviewDTO = new ReviewDTO();
        BeanUtils.copyProperties(review, reviewDTO);
        return reviewDTO;
    }

    public ResponseDto deleteById(Long id) {
        reviewRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested review is not available", "Not Found", HttpStatus.NOT_FOUND));
        reviewRepository.deleteById(id);
        return new ResponseDto("The review has been deleted successfully");
    }

    public ResponseDto updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ServiceException("The requested review is not available", "Not Found", HttpStatus.NOT_FOUND));
        BeanUtils.copyProperties(reviewDTO, review);
        reviewRepository.save(review);
        return new ResponseDto("The review has been updated successfully");
    }
}
