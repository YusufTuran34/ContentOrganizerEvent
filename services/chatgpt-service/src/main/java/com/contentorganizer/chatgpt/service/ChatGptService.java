package com.contentorganizer.chatgpt.service;

import com.contentorganizer.chatgpt.dto.ChatRequest;
import com.contentorganizer.chatgpt.dto.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatGptService {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatGptService.class);
    
    @Value("${openai.api.key}")
    private String apiKey;
    
    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String model;
    
    @Value("${openai.api.max-tokens:150}")
    private Integer maxTokens;
    
    @Value("${openai.api.temperature:0.7}")
    private Double temperature;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public ChatGptService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public Mono<ChatResponse> getChatResponse(ChatRequest request) {
        logger.info("Processing chat request: {}", request.getPrompt());
        
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", new Object[]{
                Map.of("role", "user", "content", request.getPrompt())
            },
            "max_tokens", maxTokens,
            "temperature", temperature
        );
        
        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseOpenAIResponse)
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .onErrorResume(Exception.class, this::handleGenericError);
    }
    
    private ChatResponse parseOpenAIResponse(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            String content = jsonNode
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();
            
            Integer tokensUsed = jsonNode
                .path("usage")
                .path("total_tokens")
                .asInt();
            
            String usedModel = jsonNode
                .path("model")
                .asText();
            
            logger.info("OpenAI API response received successfully. Tokens used: {}", tokensUsed);
            
            return new ChatResponse(content, tokensUsed, usedModel, true);
            
        } catch (Exception e) {
            logger.error("Failed to parse OpenAI response: {}", responseBody, e);
            return new ChatResponse("Failed to parse OpenAI response: " + e.getMessage());
        }
    }
    
    private Mono<ChatResponse> handleWebClientError(WebClientResponseException ex) {
        logger.error("OpenAI API error - Status: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        
        String errorMessage = "OpenAI API error";
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            errorMessage = "Invalid API key or unauthorized access";
        } else if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            errorMessage = "Rate limit exceeded";
        } else if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            errorMessage = "Invalid request parameters";
        }
        
        return Mono.just(new ChatResponse(errorMessage + ": " + ex.getMessage()));
    }
    
    private Mono<ChatResponse> handleGenericError(Exception ex) {
        logger.error("Unexpected error during OpenAI API call", ex);
        return Mono.just(new ChatResponse("Service temporarily unavailable: " + ex.getMessage()));
    }
} 