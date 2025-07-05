package com.contentorganizer.music.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Music response with downloaded music information")
public class MusicResponse {
    
    @Schema(description = "Success status of the operation", example = "true")
    private boolean success;
    
    @Schema(description = "Error message if operation failed", example = "null")
    private String error;
    
    @Schema(description = "Directory path where music files are stored", example = "/music/2024-07-05/electronic")
    private String musicDirectoryPath;
    
    @Schema(description = "List of downloaded music file paths", example = "['/music/2024-07-05/electronic/track1.mp3', '/music/2024-07-05/electronic/track2.mp3']")
    private List<String> musicFilePaths;
    
    @Schema(description = "Total number of tracks downloaded", example = "15")
    private Integer totalTracks;
    
    @Schema(description = "Total duration in minutes", example = "125")
    private Integer totalDurationMinutes;
    
    @Schema(description = "Music genre requested", example = "electronic")
    private String genre;
    
    @Schema(description = "Download completion time", example = "2024-07-05T12:30:45")
    private LocalDateTime downloadedAt;
    
    @Schema(description = "Download session ID for tracking", example = "music-session-123456")
    private String sessionId;
    
    public MusicResponse() {
    }
    
    public MusicResponse(boolean success, String musicDirectoryPath, List<String> musicFilePaths, 
                        Integer totalTracks, Integer totalDurationMinutes, String genre, String sessionId) {
        this.success = success;
        this.musicDirectoryPath = musicDirectoryPath;
        this.musicFilePaths = musicFilePaths;
        this.totalTracks = totalTracks;
        this.totalDurationMinutes = totalDurationMinutes;
        this.genre = genre;
        this.sessionId = sessionId;
        this.downloadedAt = LocalDateTime.now();
    }
    
    public MusicResponse(String error) {
        this.success = false;
        this.error = error;
        this.downloadedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMusicDirectoryPath() {
        return musicDirectoryPath;
    }
    
    public void setMusicDirectoryPath(String musicDirectoryPath) {
        this.musicDirectoryPath = musicDirectoryPath;
    }
    
    public List<String> getMusicFilePaths() {
        return musicFilePaths;
    }
    
    public void setMusicFilePaths(List<String> musicFilePaths) {
        this.musicFilePaths = musicFilePaths;
    }
    
    public Integer getTotalTracks() {
        return totalTracks;
    }
    
    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }
    
    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }
    
    public void setTotalDurationMinutes(Integer totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public LocalDateTime getDownloadedAt() {
        return downloadedAt;
    }
    
    public void setDownloadedAt(LocalDateTime downloadedAt) {
        this.downloadedAt = downloadedAt;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    @Override
    public String toString() {
        return "MusicResponse{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", musicDirectoryPath='" + musicDirectoryPath + '\'' +
                ", totalTracks=" + totalTracks +
                ", totalDurationMinutes=" + totalDurationMinutes +
                ", genre='" + genre + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", downloadedAt=" + downloadedAt +
                '}';
    }
} 