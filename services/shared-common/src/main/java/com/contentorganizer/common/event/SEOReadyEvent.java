package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event triggered when SEO optimization is ready
 */
public class SEOReadyEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("optimizedTitle")
    private final String optimizedTitle;
    
    @JsonProperty("optimizedDescription")
    private final String optimizedDescription;
    
    @JsonProperty("keywords")
    private final List<String> keywords;
    
    @JsonProperty("tags")
    private final List<String> tags;
    
    @JsonProperty("thumbnail")
    private final String thumbnail;
    
    public SEOReadyEvent(String projectId, String optimizedTitle, String optimizedDescription, 
                        List<String> keywords, List<String> tags, String thumbnail) {
        super("SEOReadyEvent");
        this.projectId = projectId;
        this.optimizedTitle = optimizedTitle;
        this.optimizedDescription = optimizedDescription;
        this.keywords = keywords;
        this.tags = tags;
        this.thumbnail = thumbnail;
    }
    
    @JsonCreator
    public SEOReadyEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("optimizedTitle") String optimizedTitle,
            @JsonProperty("optimizedDescription") String optimizedDescription,
            @JsonProperty("keywords") List<String> keywords,
            @JsonProperty("tags") List<String> tags,
            @JsonProperty("thumbnail") String thumbnail) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.optimizedTitle = optimizedTitle;
        this.optimizedDescription = optimizedDescription;
        this.keywords = keywords;
        this.tags = tags;
        this.thumbnail = thumbnail;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public String getOptimizedTitle() {
        return optimizedTitle;
    }
    
    public String getOptimizedDescription() {
        return optimizedDescription;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    @Override
    public String toString() {
        return "SEOReadyEvent{" +
                "projectId='" + projectId + '\'' +
                ", optimizedTitle='" + optimizedTitle + '\'' +
                ", optimizedDescription='" + optimizedDescription + '\'' +
                ", keywords=" + keywords +
                ", tags=" + tags +
                ", thumbnail='" + thumbnail + '\'' +
                "} " + super.toString();
    }
} 