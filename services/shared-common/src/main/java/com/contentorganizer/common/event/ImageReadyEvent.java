package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event triggered when images are ready for video creation
 */
public class ImageReadyEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("imageIds")
    private final List<String> imageIds;
    
    @JsonProperty("imageUrls")
    private final List<String> imageUrls;
    
    @JsonProperty("style")
    private final String style;
    
    public ImageReadyEvent(String projectId, List<String> imageIds, List<String> imageUrls, String style) {
        super("ImageReadyEvent");
        this.projectId = projectId;
        this.imageIds = imageIds;
        this.imageUrls = imageUrls;
        this.style = style;
    }
    
    @JsonCreator
    public ImageReadyEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("imageIds") List<String> imageIds,
            @JsonProperty("imageUrls") List<String> imageUrls,
            @JsonProperty("style") String style) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.imageIds = imageIds;
        this.imageUrls = imageUrls;
        this.style = style;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public List<String> getImageIds() {
        return imageIds;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public String getStyle() {
        return style;
    }
    
    @Override
    public String toString() {
        return "ImageReadyEvent{" +
                "projectId='" + projectId + '\'' +
                ", imageIds=" + imageIds +
                ", imageUrls=" + imageUrls +
                ", style='" + style + '\'' +
                "} " + super.toString();
    }
} 