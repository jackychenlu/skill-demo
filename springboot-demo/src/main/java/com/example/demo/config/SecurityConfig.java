package com.example.demo.config;

import com.example.demo.security.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for API token-based authentication.
 * Configures stateless security with Bearer token validation.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for API (we use stateless tokens)
            .csrf(csrf -> csrf.disable())

            // Use stateless sessions (no session cookies)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints - allow Swagger UI and API docs
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Public API endpoints - token validation and auth check endpoints
                // Allow /api/auth/validate and /api/auth/check without authentication
                // This allows users to check their auth status or validate a token
                .requestMatchers("/api/auth/validate", "/api/auth/check").permitAll()
                
                // Protected endpoints - require authentication
                .requestMatchers("/api/**").authenticated()
                
                // Allow everything else by default but could deny if needed
                .anyRequest().permitAll()
            )

            // Add token authentication filter before the UsernamePasswordAuthenticationFilter
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // Disable form login (API only)
            .formLogin(form -> form.disable())

            // Disable HTTP basic auth
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
