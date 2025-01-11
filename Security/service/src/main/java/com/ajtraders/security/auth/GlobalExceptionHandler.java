package com.ajtraders.security.auth;

import com.ajtraders.security.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    // Handle generic exceptions
    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleServiceException(ServiceException ex) {
        log.error("Internal service error: ", ex);
        ApiError apiError = new ApiError(ex.getHttpStatus().value(), ex.getHeaderMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError,ex.getHttpStatus());
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ApiError {
        private int status;
        private String message;
        private List<String> errors;
    }
}
