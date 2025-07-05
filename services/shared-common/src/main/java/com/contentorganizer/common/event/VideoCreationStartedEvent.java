package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Event triggered when video creation process starts
 */
public class VideoCreationStartedEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("title")
    private final String title;
    
    @JsonProperty("description")
    private final String description;
    
    @JsonProperty("tags")
    private final String tags;
    
    public VideoCreationStartedEvent(String projectId, String title, String description, String tags) {
        super("VideoCreationStartedEvent");
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }
    
    @JsonCreator
    public VideoCreationStartedEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("tags") String tags) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTags() {
        return tags;
    }
    
    @Override
    public String toString() {
        return "VideoCreationStartedEvent{" +
                "projectId='" + projectId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                "} " + super.toString();
    }
} 