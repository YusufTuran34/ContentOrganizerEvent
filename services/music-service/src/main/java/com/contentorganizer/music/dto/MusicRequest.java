package com.contentorganizer.music.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;

@Schema(description = "Music request for downloading copyright-free music from Jamendo")
public class MusicRequest {
    
    @Schema(description = "Music genre", example = "electronic", allowableValues = {"electronic", "rock", "jazz", "classical", "pop", "ambient", "lofi", "chillout", "world", "folk"})
    @NotBlank(message = "Music genre cannot be blank")
    private String genre;
    
    @Schema(description = "Duration in hours", example = "2", minimum = "1", maximum = "24")
    @Positive(message = "Duration must be positive")
    @Max(value = 24, message = "Duration cannot exceed 24 hours")
    private Integer durationHours;
    
    public MusicRequest() {
    }
    
    public MusicRequest(String genre, Integer durationHours) {
        this.genre = genre;
        this.durationHours = durationHours;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public Integer getDurationHours() {
        return durationHours;
    }
    
    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }
    
    @Override
    public String toString() {
        return "MusicRequest{" +
                "genre='" + genre + '\'' +
                ", durationHours=" + durationHours +
                '}';
    }
} 