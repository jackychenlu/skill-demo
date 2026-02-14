package com.example.demo.controller;

import com.example.demo.dto.TokenValidationResponse;
import com.example.demo.security.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Authentication", description = "Endpoints for token validation and authentication")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final TokenProvider tokenProvider;

    public AuthController(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Check if the current request is authenticated.
     * Can be called with or without a token.
     * If token is provided, it will be validated.
     * 
     * @param authHeader the Authorization header (optional)
     * @return 200 OK with authentication status
     */
    @GetMapping("/check")
    @Operation(
        summary = "Check authentication status",
        description = "Verifies if the provided token is valid. Can be called with or without authentication.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TokenValidationResponse> checkAuth(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.debug("Authentication check requested");
        
        // If Authorization header is provided, validate the token
        if (authHeader != null && !authHeader.isBlank()) {
            String token = tokenProvider.extractToken(authHeader);
            if (token != null && tokenProvider.validateToken(token)) {
                log.debug("Authentication check: token is valid");
                return ResponseEntity.ok(new TokenValidationResponse(
                    true,
                    "Authentication token is valid"
                ));
            } else {
                log.debug("Authentication check: token is invalid");
                return ResponseEntity.ok(new TokenValidationResponse(
                    false,
                    "Authentication header is invalid or token is expired"
                ));
            }
        }
        
        // No Authorization header provided
        log.debug("Authentication check: no token provided");
        return ResponseEntity.ok(new TokenValidationResponse(
            false,
            "No authentication token provided"
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
    @Operation(
        summary = "Validate token",
        description = "Validates a token by extracting it from Authorization header. Useful for testing token validity.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<TokenValidationResponse> validateToken(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.debug("Token validation requested");
        
        if (authHeader == null || authHeader.isBlank()) {
            log.debug("Token validation failed: no Authorization header");
            return ResponseEntity.status(400).body(new TokenValidationResponse(
                false,
                "Authorization header is missing"
            ));
        }

        String token = tokenProvider.extractToken(authHeader);
        
        if (token == null) {
            log.debug("Token validation failed: invalid header format");
            return ResponseEntity.status(400).body(new TokenValidationResponse(
                false,
                "Invalid Authorization header format. Use: Bearer <token>"
            ));
        }

        if (tokenProvider.validateToken(token)) {
            log.debug("Token validation successful");
            return ResponseEntity.ok(new TokenValidationResponse(
                true,
                "Token is valid"
            ));
        }

        log.debug("Token validation failed: token is invalid");
        return ResponseEntity.status(401).body(new TokenValidationResponse(
            false,
            "Token is invalid or expired"
        ));
    }
}
