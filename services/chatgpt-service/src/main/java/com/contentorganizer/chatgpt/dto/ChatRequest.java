package com.contentorganizer.chatgpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Chat request to OpenAI API")
public class ChatRequest {
    
    @Schema(description = "The prompt to send to ChatGPT", example = "Lofi radio için tanıtım mesajı")
    @NotBlank(message = "Prompt cannot be blank")
    @Size(min = 1, max = 4000, message = "Prompt must be between 1 and 4000 characters")
    private String prompt;
    
    public ChatRequest() {
    }
    
    public ChatRequest(String prompt) {
        this.prompt = prompt;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    @Override
    public String toString() {
        return "ChatRequest{" +
                "prompt='" + prompt + '\'' +
                '}';
    }
} 