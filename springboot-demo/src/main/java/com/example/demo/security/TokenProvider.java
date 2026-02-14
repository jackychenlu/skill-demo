package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for validating API tokens.
 * Supports both simple token matching and Bearer token validation.
 */
@Service
public class TokenProvider {

    @Value("${api.secret.key:}")
    private String secretKey;

    @Value("${api.allowed.tokens:}")
    private String allowedTokensConfig;

    /**
     * Validate a Bearer token.
     * The token should be in the format: "Bearer <token>"
     * 
     * @param token the authorization header value
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        // Check if token matches the secret key
        if (!secretKey.isBlank() && token.equals(secretKey)) {
            return true;
        }

        // Check if token is in allowed list
        if (!allowedTokensConfig.isBlank()) {
            String[] allowedTokens = allowedTokensConfig.split(",");
            for (String allowed : allowedTokens) {
                if (token.equals(allowed.strip())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Extract token from Authorization header.
     * Expected format: "Bearer <token>"
     * 
     * @param authHeader the Authorization header value
     * @return the token, or null if header is invalid
     */
    public String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
