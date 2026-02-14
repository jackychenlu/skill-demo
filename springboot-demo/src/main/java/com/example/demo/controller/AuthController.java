package com.example.demo.controller;

import com.example.demo.dto.TokenValidationResponse;
import com.example.demo.security.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller.
 * Provides endpoints for token validation and authentication checks.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TokenProvider tokenProvider;

    public AuthController(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Check if the current request is authenticated.
     * 
     * @return 200 OK if authenticated, 401 Unauthorized otherwise
     */
    @GetMapping("/check")
    public ResponseEntity<TokenValidationResponse> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {
            return ResponseEntity.ok(new TokenValidationResponse(
                true,
                "Authentication token is valid"
            ));
        }
        
        return ResponseEntity.status(401).body(new TokenValidationResponse(
            false,
            "No valid authentication token provided"
        ));
    }

    /**
     * Validate a token without setting it in the security context.
     * Used for health checks and token validation testing.
     * 
     * @param authHeader the Authorization header value
     * @return response indicating if token is valid
     */
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.status(400).body(new TokenValidationResponse(
                false,
                "Authorization header is missing"
            ));
        }

        String token = tokenProvider.extractToken(authHeader);
        
        if (token == null) {
            return ResponseEntity.status(400).body(new TokenValidationResponse(
                false,
                "Invalid Authorization header format. Use: Bearer <token>"
            ));
        }

        if (tokenProvider.validateToken(token)) {
            return ResponseEntity.ok(new TokenValidationResponse(
                true,
                "Token is valid"
            ));
        }

        return ResponseEntity.status(401).body(new TokenValidationResponse(
            false,
            "Token is invalid or expired"
        ));
    }
}
