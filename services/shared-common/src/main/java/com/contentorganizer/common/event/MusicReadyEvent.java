package com.contentorganizer.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Event triggered when music is ready for video creation
 */
public class MusicReadyEvent extends BaseEvent {
    
    @JsonProperty("projectId")
    private final String projectId;
    
    @JsonProperty("musicId")
    private final String musicId;
    
    @JsonProperty("musicUrl")
    private final String musicUrl;
    
    @JsonProperty("genre")
    private final String genre;
    
    @JsonProperty("duration")
    private final Integer duration;
    
    public MusicReadyEvent(String projectId, String musicId, String musicUrl, String genre, Integer duration) {
        super("MusicReadyEvent");
        this.projectId = projectId;
        this.musicId = musicId;
        this.musicUrl = musicUrl;
        this.genre = genre;
        this.duration = duration;
    }
    
    @JsonCreator
    public MusicReadyEvent(
            @JsonProperty("eventId") String eventId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("projectId") String projectId,
            @JsonProperty("musicId") String musicId,
            @JsonProperty("musicUrl") String musicUrl,
            @JsonProperty("genre") String genre,
            @JsonProperty("duration") Integer duration) {
        super(eventId, timestamp, eventType);
        this.projectId = projectId;
        this.musicId = musicId;
        this.musicUrl = musicUrl;
        this.genre = genre;
        this.duration = duration;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public String getMusicId() {
        return musicId;
    }
    
    public String getMusicUrl() {
        return musicUrl;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        return "MusicReadyEvent{" +
                "projectId='" + projectId + '\'' +
                ", musicId='" + musicId + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                "} " + super.toString();
    }
} 