package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT/Token authentication filter.
 * Validates Bearer tokens in the Authorization header.
 * This filter runs once per request to extract and validate tokens.
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenProvider tokenProvider;

    public TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // Extract Authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = tokenProvider.extractToken(authHeader);

            if (token != null && tokenProvider.validateToken(token)) {
                // Token is valid, create authentication
                UsernamePasswordAuthenticationToken auth = 
                    new UsernamePasswordAuthenticationToken(
                        "api-client",
                        null,
                        new ArrayList<>() // No specific roles for API token auth
                    );
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("Authentication successful for request: {} {}", request.getMethod(), request.getRequestURI());
            } else {
                log.debug("Authentication failed for request: {} {}", request.getMethod(), request.getRequestURI());
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
