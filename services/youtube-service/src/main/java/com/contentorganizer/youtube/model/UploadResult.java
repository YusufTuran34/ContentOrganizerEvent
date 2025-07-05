package com.contentorganizer.youtube.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResult {
    private boolean success;
    private String message;
    private String videoId;
} 