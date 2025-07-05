package com.contentorganizer.youtube.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResponse {
    private String status;
    private String message;
    private String videoId;
    private String videoPath;
    private Long durationSeconds;
    private Long fileSizeBytes;
} 