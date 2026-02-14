package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST API endpoints.
 * Provides consistent error responses across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation errors from @Valid annotations.
     * 
     * @param ex the validation exception
     * @param request the web request
     * @return 400 Bad Request with field-level errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex, 
            WebRequest request) {
        
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing
                ));
        
        log.warn("Validation failed for request: {} - errors: {}", request.getDescription(false), fieldErrors);
        
        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            request.getDescription(false),
            Instant.now(),
            fieldErrors
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle generic runtime exceptions.
     * 
     * @param ex the exception
     * @param request the web request
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {
        
        log.error("Unexpected runtime exception for request: {}", request.getDescription(false), ex);
        
        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            request.getDescription(false),
            Instant.now(),
            new HashMap<>()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle illegal argument exceptions.
     * 
     * @param ex the exception
     * @param request the web request
     * @return 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        log.warn("Illegal argument exception for request: {} - message: {}", request.getDescription(false), ex.getMessage());
        
        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getDescription(false),
            Instant.now(),
            new HashMap<>()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
