package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Event triggered when video is ready for publication
 */
public class VideoReadyEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("videoId")
    private final String videoId;
    
    @JsonProperty("videoUrl")
    private final String videoUrl;
    
    @JsonProperty("duration")
    private final Integer duration;
    
    @JsonProperty("resolution")
    private final String resolution;
    
    public VideoReadyEvent(String projectId, String videoId, String videoUrl, Integer duration, String resolution) {
        super("VideoReadyEvent");
        this.projectId = projectId;
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.resolution = resolution;
    }
    
    @JsonCreator
    public VideoReadyEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("videoId") String videoId,
            @JsonProperty("videoUrl") String videoUrl,
            @JsonProperty("duration") Integer duration,
            @JsonProperty("resolution") String resolution) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.resolution = resolution;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public String getVideoId() {
        return videoId;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    @Override
    public String toString() {
        return "VideoReadyEvent{" +
                "projectId='" + projectId + '\'' +
                ", videoId='" + videoId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", duration=" + duration +
                ", resolution='" + resolution + '\'' +
                "} " + super.toString();
    }
} 