package com.example.demo.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized API error response.
 * Provides consistent structure for error messages across the API.
 */
public record ApiError(
    int status,
    String message,
    String path,
    Instant timestamp,
    Map<String, String> errors
) {}
