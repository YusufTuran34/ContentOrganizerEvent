package com.contentorganizer.youtube.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoResponse {
    private String videoId;
    private String videoUrl;
    private String error;
    private boolean success;
    private String videoPath;
    private String tag;
    private long durationSeconds;
    private long fileSizeBytes;
} 