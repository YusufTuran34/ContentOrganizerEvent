package com.contentorganizer.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for video creation request
 */
public class VideoCreationRequest {
    
    @JsonProperty("title")
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;
    
    @JsonProperty("description")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    @JsonProperty("tags")
    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;
    
    @JsonProperty("musicGenre")
    private String musicGenre;
    
    @JsonProperty("imageStyle")
    private String imageStyle;
    
    @JsonProperty("videoResolution")
    private String videoResolution;
    
    // Default constructor
    public VideoCreationRequest() {
        this.videoResolution = "1080p";
        this.musicGenre = "lofi";
        this.imageStyle = "modern";
    }
    
    // Constructor with required fields
    public VideoCreationRequest(String title, String description, String tags) {
        this();
        this.title = title;
        this.description = description;
        this.tags = tags;
    }
    
    // Getters and setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String getMusicGenre() {
        return musicGenre;
    }
    
    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }
    
    public String getImageStyle() {
        return imageStyle;
    }
    
    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }
    
    public String getVideoResolution() {
        return videoResolution;
    }
    
    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }
    
    @Override
    public String toString() {
        return "VideoCreationRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", musicGenre='" + musicGenre + '\'' +
                ", imageStyle='" + imageStyle + '\'' +
                ", videoResolution='" + videoResolution + '\'' +
                '}';
    }
} 