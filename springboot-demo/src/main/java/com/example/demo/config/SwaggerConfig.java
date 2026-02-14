package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for Bearer token authentication.
 * Configures the API documentation to support Bearer token in Authorization header.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("UUID")
            .description("Bearer token authentication using UUID tokens from application.properties");
        
        Components components = new Components()
            .addSecuritySchemes("bearerAuth", securityScheme);
        
        return new OpenAPI()
            .info(new Info()
                .title("Server Status API")
                .description("API for monitoring server status with Bearer token authentication")
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(components);
    }
}

