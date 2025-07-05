package com.contentorganizer.youtube.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Request object for uploading a video to YouTube")
public class VideoUploadRequest {
    @NotBlank(message = "Video path is required")
    @Schema(description = "Path to the video file to upload", example = "/path/to/video.mp4")
    private String videoPath;

    @Schema(description = "Whether to use AI to generate title, description and tags", example = "true", defaultValue = "true")
    private boolean useAiGeneration = true;

    @Schema(description = "Custom title for the video (optional - will be generated if not provided)", example = "My Awesome Video")
    private String customTitle;

    @Schema(description = "Custom description for the video (optional - will be generated if not provided)", example = "This is my awesome video description")
    private String customDescription;

    @Schema(description = "Custom tags for the video (optional - will be generated if not provided)", example = "awesome,video,content")
    private List<String> customTags;

    @Schema(description = "Privacy status of the video", example = "private", defaultValue = "private")
    private String privacyStatus = "private";

    @Schema(description = "Video category ID", example = "10", defaultValue = "10")
    private String categoryId = "10";
} 