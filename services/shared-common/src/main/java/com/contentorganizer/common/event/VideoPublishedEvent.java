package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Event triggered when video is published to YouTube
 */
public class VideoPublishedEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("youtubeVideoId")
    private final String youtubeVideoId;
    
    @JsonProperty("youtubeUrl")
    private final String youtubeUrl;
    
    @JsonProperty("status")
    private final String status;
    
    @JsonProperty("publishedAt")
    private final LocalDateTime publishedAt;
    
    public VideoPublishedEvent(String projectId, String youtubeVideoId, String youtubeUrl, String status, LocalDateTime publishedAt) {
        super("VideoPublishedEvent");
        this.projectId = projectId;
        this.youtubeVideoId = youtubeVideoId;
        this.youtubeUrl = youtubeUrl;
        this.status = status;
        this.publishedAt = publishedAt;
    }
    
    @JsonCreator
    public VideoPublishedEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("youtubeVideoId") String youtubeVideoId,
            @JsonProperty("youtubeUrl") String youtubeUrl,
            @JsonProperty("status") String status,
            @JsonProperty("publishedAt") LocalDateTime publishedAt) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.youtubeVideoId = youtubeVideoId;
        this.youtubeUrl = youtubeUrl;
        this.status = status;
        this.publishedAt = publishedAt;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }
    
    public String getYoutubeUrl() {
        return youtubeUrl;
    }
    
    public String getStatus() {
        return status;
    }
    
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
    
    @Override
    public String toString() {
        return "VideoPublishedEvent{" +
                "projectId='" + projectId + '\'' +
                ", youtubeVideoId='" + youtubeVideoId + '\'' +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", status='" + status + '\'' +
                ", publishedAt=" + publishedAt +
                "} " + super.toString();
    }
} 