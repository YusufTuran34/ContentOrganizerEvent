package com.contentorganizer.youtube.service;

import com.contentorganizer.youtube.config.ContentGenerationConfig;
import com.contentorganizer.youtube.dto.VideoResponse;
import com.contentorganizer.youtube.dto.VideoStreamRequest;
import com.contentorganizer.youtube.dto.VideoUploadRequest;
import com.contentorganizer.youtube.model.UploadResult;
import com.contentorganizer.youtube.model.VideoMetadata;
import com.contentorganizer.youtube.service.ContentGenerationService.GeneratedMetadata;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeService {
    
    private final FFmpegService ffmpegService;
    private final ContentGenerationService contentGenerationService;
    private final YouTubeApiService youTubeApiService;
    private final ContentGenerationConfig contentGenerationConfig;
    private final YouTube youTube;
    
    public Mono<VideoResponse> streamVideo(VideoStreamRequest request) {
        try {
            VideoMetadata metadata = ffmpegService.getVideoMetadata(request.getInputPath());
            
            // Start streaming in a separate thread
            new Thread(() -> {
                try {
                    ffmpegService.startStreaming(request);
                } catch (Exception e) {
                    log.error("Streaming thread failed", e);
                }
            }).start();

            return Mono.just(VideoResponse.builder()
                .status("STREAMING")
                .message("Stream started successfully")
                .videoPath(request.getInputPath())
                .durationSeconds(metadata.getDurationSeconds())
                .fileSizeBytes(metadata.getFileSizeBytes())
                .build());
        } catch (Exception e) {
            log.error("Failed to start streaming", e);
            return Mono.just(VideoResponse.builder()
                .status("ERROR")
                .message("Failed to start streaming: " + e.getMessage())
                .build());
        }
    }
    
    public Mono<VideoResponse> uploadVideo(VideoUploadRequest request) {
        try {
            validateVideoFile(request.getVideoPath());
            VideoMetadata metadata = ffmpegService.getVideoMetadata(request.getVideoPath());

            if (request.isUseAiGeneration()) {
                return contentGenerationService.generateMetadata(metadata)
                    .flatMap(generatedMetadata -> uploadVideoWithMetadata(
                        request.getVideoPath(),
                        generatedMetadata.getTitle(),
                        generatedMetadata.getDescription(),
                        generatedMetadata.getTags(),
                        request.getPrivacyStatus()
                    ));
            } else {
                return uploadVideoWithMetadata(
                    request.getVideoPath(),
                    request.getCustomTitle(),
                    request.getCustomDescription(),
                    request.getCustomTags(),
                    request.getPrivacyStatus()
                );
            }
        } catch (Exception e) {
            log.error("Failed to upload video", e);
            return Mono.just(VideoResponse.builder()
                .status("ERROR")
                .message("Failed to upload video: " + e.getMessage())
                .build());
        }
    }
    
    public void startLiveStream(VideoStreamRequest request) throws IOException {
        ffmpegService.startStreaming(request);
    }
    
    public Mono<Boolean> validateConfiguration() {
        return youTubeApiService.validateCredentials()
            .map(valid -> {
                if (valid) {
                    log.info("YouTube service configuration validated successfully");
                } else {
                    log.error("YouTube service configuration validation failed");
                }
                return valid;
            });
    }
    
    private void validateVideoFile(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Video file does not exist: " + path);
        }
    }

    private Mono<VideoResponse> uploadVideoWithMetadata(
        String videoPath,
        String title,
        String description,
        List<String> tags,
        String privacyStatus
    ) {
        return youTubeApiService.uploadVideo(videoPath, title, description, tags, privacyStatus)
            .map(result -> VideoResponse.builder()
                .status(result.isSuccess() ? "UPLOADED" : "ERROR")
                .message(result.getMessage())
                .videoId(result.getVideoId())
                .build());
    }
} 