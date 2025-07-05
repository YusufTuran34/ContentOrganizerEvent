package com.contentorganizer.youtube.controller;

import com.contentorganizer.youtube.dto.VideoResponse;
import com.contentorganizer.youtube.dto.VideoStreamRequest;
import com.contentorganizer.youtube.dto.VideoUploadRequest;
import com.contentorganizer.youtube.model.UploadResult;
import com.contentorganizer.youtube.service.YouTubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
@Tag(name = "YouTube Controller", description = "API endpoints for YouTube video operations")
@Slf4j
public class YouTubeController {

    private final YouTubeService youTubeService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a video to YouTube", description = "Upload a video file to YouTube with optional AI-generated metadata")
    public Mono<ResponseEntity<VideoResponse>> uploadVideo(@RequestBody VideoUploadRequest request) {
        return youTubeService.uploadVideo(request)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("Error uploading video: {}", e.getMessage()));
    }

    @PostMapping("/stream")
    @Operation(summary = "Stream a video to YouTube", description = "Start streaming a video to YouTube Live")
    public Mono<ResponseEntity<VideoResponse>> streamVideo(@RequestBody VideoStreamRequest request) {
        return youTubeService.streamVideo(request)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("Error streaming video: {}", e.getMessage()));
    }

    @GetMapping("/health")
    @Operation(summary = "Check service health", description = "Check if the YouTube service is running and properly configured")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("YouTube Service is running");
    }
}