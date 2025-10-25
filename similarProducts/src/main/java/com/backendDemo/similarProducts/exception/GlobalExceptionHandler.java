package com.backendDemo.similarProducts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "code", 404,
                        "message", ex.getMessage(),
                        "type", "Not Found"
                ));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleResponseStatusException(InternalServerErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "code", 500,
                        "message", ex.getMessage(),
                        "type", "Internal Server Error"
                ));
    }
}
