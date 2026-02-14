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
                // Public endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Protected endpoints - require authentication
                .requestMatchers("/api/**").authenticated()
                
                // Deny everything else
                .anyRequest().denyAll()
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
