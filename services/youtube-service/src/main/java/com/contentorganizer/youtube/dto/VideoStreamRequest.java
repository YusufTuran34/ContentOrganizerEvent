package com.contentorganizer.youtube.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoStreamRequest {
    @NotBlank(message = "Input path cannot be empty")
    private String inputPath;
} 