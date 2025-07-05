package com.contentorganizer.youtube.config;

import lombok.Data;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Data
@Configuration
@ConfigurationProperties(prefix = "ffmpeg")
public class FFmpegConfig {
    private String tempDirectory = "./temp-videos";
    private String outputDirectory = "./processed-videos";

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg();
    }

    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe();
    }
} 