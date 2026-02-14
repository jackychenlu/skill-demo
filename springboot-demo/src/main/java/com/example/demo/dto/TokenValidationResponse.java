package com.example.demo.dto;

/**
 * Token validation request DTO.
 * Used for health check and token validation.
 */
public record TokenValidationResponse(
    boolean valid,
    String message
) {}
