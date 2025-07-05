package com.contentorganizer.youtube.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoMetadata {
    private Long durationSeconds;
    private Long fileSizeBytes;
    private Integer width;
    private Integer height;
    private double frameRate;
    private long bitRate;
} 