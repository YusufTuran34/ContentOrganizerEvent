package com.contentorganizer.chatgpt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Chat response from OpenAI API")
public class ChatResponse {
    
    @Schema(description = "The response text from ChatGPT", example = "🎵 Lofi Radio - Sakin ve Huzurlu Müzikler...")
    private String response;
    
    @Schema(description = "The number of tokens used in the request", example = "150")
    private Integer tokensUsed;
    
    @Schema(description = "The model used for the request", example = "gpt-3.5-turbo")
    private String model;
    
    @Schema(description = "Whether the request was successful", example = "true")
    private boolean success;
    
    @Schema(description = "Error message if the request failed", example = "null")
    private String error;
    
    public ChatResponse() {
    }
    
    public ChatResponse(String response, Integer tokensUsed, String model, boolean success) {
        this.response = response;
        this.tokensUsed = tokensUsed;
        this.model = model;
        this.success = success;
    }
    
    public ChatResponse(String error) {
        this.error = error;
        this.success = false;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public Integer getTokensUsed() {
        return tokensUsed;
    }
    
    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public String toString() {
        return "ChatResponse{" +
                "response='" + response + '\'' +
                ", tokensUsed=" + tokensUsed +
                ", model='" + model + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
} 