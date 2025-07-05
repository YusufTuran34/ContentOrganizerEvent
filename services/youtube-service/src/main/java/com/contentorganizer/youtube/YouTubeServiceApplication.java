package com.contentorganizer.youtube;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition(
    info = @Info(
        title = "YouTube Service API",
        version = "1.0",
        description = "API for uploading and streaming videos to YouTube"
    )
)
public class YouTubeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouTubeServiceApplication.class, args);
    }
} 