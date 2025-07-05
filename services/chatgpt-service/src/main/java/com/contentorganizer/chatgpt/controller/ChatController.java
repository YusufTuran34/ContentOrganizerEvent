package com.contentorganizer.chatgpt.controller;

import com.contentorganizer.chatgpt.dto.ChatRequest;
import com.contentorganizer.chatgpt.dto.ChatResponse;
import com.contentorganizer.chatgpt.service.ChatGptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Tag(name = "ChatGPT API", description = "OpenAI ChatGPT integration endpoints")
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    private final ChatGptService chatGptService;
    
    @Autowired
    public ChatController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }
    
    @PostMapping("/chat")
    @Operation(
        summary = "Send prompt to ChatGPT",
        description = "Sends a prompt to OpenAI ChatGPT API and returns the response"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed request"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<ResponseEntity<ChatResponse>> chat(
            @Parameter(description = "Chat request containing the prompt", required = true)
            @Valid @RequestBody ChatRequest request) {
        
        logger.info("Received chat request: {}", request);
        
        return chatGptService.getChatResponse(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        logger.info("Chat request processed successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        logger.error("Chat request failed: {}", response.getError());
                        return ResponseEntity.status(500).body(response);
                    }
                })
                .onErrorResume(Exception.class, ex -> {
                    logger.error("Unexpected error in chat endpoint", ex);
                    ChatResponse errorResponse = new ChatResponse("Internal server error: " + ex.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
    }
    
    @GetMapping("/health")
    @Operation(
        summary = "Health check endpoint",
        description = "Returns the health status of the ChatGPT service"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<String> health() {
        logger.debug("Health check requested");
        return ResponseEntity.ok("ChatGPT Service is running");
    }
    
    @GetMapping("/status")
    @Operation(
        summary = "Service status endpoint",
        description = "Returns detailed status information about the ChatGPT service"
    )
    @ApiResponse(responseCode = "200", description = "Status information retrieved successfully")
    public ResponseEntity<Object> status() {
        logger.debug("Status check requested");
        
        return ResponseEntity.ok(new Object() {
            public String service = "ChatGPT Service";
            public String version = "1.0.0";
            public String status = "RUNNING";
            public String description = "OpenAI ChatGPT integration service for content generation";
        });
    }
} 