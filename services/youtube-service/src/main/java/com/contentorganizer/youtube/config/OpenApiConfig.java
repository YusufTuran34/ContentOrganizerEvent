package com.contentorganizer.youtube.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("YouTube Service API")
                .version("1.0")
                .description("API for uploading and streaming videos to YouTube")
                .license(new License()
                    .name("Content Organizer Event Driven System")
                    .url("https://github.com/yourusername/ContentOrganizerEventDrivenSystem")));
    }
} 