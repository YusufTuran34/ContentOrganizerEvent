package com.contentorganizer.youtube.config;

import com.contentorganizer.youtube.model.VideoQuality;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "youtube.stream")
public class StreamConfig {
    private String key;
    private VideoQuality quality = VideoQuality.HIGH;
    private int bitrate = 4500000;
    private int frameRate = 30;
    private String outputDir = "output";
} 