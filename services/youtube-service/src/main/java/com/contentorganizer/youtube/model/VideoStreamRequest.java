package com.contentorganizer.youtube.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoStreamRequest {
    @NotBlank(message = "Input path cannot be empty")
    private String inputPath;
} 