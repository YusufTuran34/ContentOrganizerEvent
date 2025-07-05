package com.contentorganizer.music.service;

import com.contentorganizer.music.dto.MusicRequest;
import com.contentorganizer.music.dto.MusicResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JamendoService {
    
    private static final Logger logger = LoggerFactory.getLogger(JamendoService.class);
    
    @Value("${jamendo.api.client-id}")
    private String clientId;
    
    @Value("${jamendo.api.client-secret}")
    private String clientSecret;
    
    @Value("${jamendo.api.base-url:https://api.jamendo.com/v3.0}")
    private String baseUrl;
    
    @Value("${music.download.base-path:/tmp/music}")
    private String baseMusicPath;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public JamendoService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public Mono<MusicResponse> downloadMusic(MusicRequest request) {
        logger.info("Starting music download for genre: {}, duration: {} hours", request.getGenre(), request.getDurationHours());
        
        String sessionId = "music-session-" + UUID.randomUUID().toString().substring(0, 8);
        
        return searchTracks(request.getGenre(), request.getDurationHours())
                .flatMap(tracks -> downloadTracks(tracks, request.getGenre(), sessionId))
                .onErrorResume(Exception.class, ex -> {
                    logger.error("Error downloading music: ", ex);
                    return Mono.just(new MusicResponse("Failed to download music: " + ex.getMessage()));
                });
    }
    
    private Mono<JsonNode> searchTracks(String genre, Integer durationHours) {
        // Calculate required number of tracks (assuming average 4 minutes per track)
        int requiredTracks = Math.max(15, (durationHours * 60) / 4);
        
        String url = baseUrl + "/tracks/";
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.jamendo.com")
                        .path("/v3.0/tracks/")
                        .queryParam("client_id", clientId)
                        .queryParam("format", "json")
                        .queryParam("limit", Math.min(requiredTracks, 200)) // API limit
                        .queryParam("tags", genre)
                        .queryParam("ccsa", "true") // Creative Commons Share-Alike license (copyright-free)
                        .queryParam("include", "musicinfo")
                        .queryParam("audiodlformat", "mp32") // MP3 format
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseTracksResponse)
                .doOnNext(response -> logger.info("Found {} tracks for genre: {}", 
                    response.path("headers").path("results_count").asInt(), genre))
                .onErrorMap(WebClientResponseException.class, ex -> 
                    new RuntimeException("Jamendo API error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString()));
    }
    
    private JsonNode parseTracksResponse(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            logger.error("Failed to parse Jamendo API response: {}", responseBody, e);
            throw new RuntimeException("Failed to parse Jamendo API response", e);
        }
    }
    
    private Mono<MusicResponse> downloadTracks(JsonNode tracksResponse, String genre, String sessionId) {
        return Mono.fromCallable(() -> {
            JsonNode tracks = tracksResponse.path("results");
            
            if (tracks.isEmpty()) {
                throw new RuntimeException("No tracks found for genre: " + genre);
            }
            
            // Create date-based directory
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String musicDirectory = baseMusicPath + "/" + dateFolder + "/" + genre;
            File directory = new File(musicDirectory);
            
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("Failed to create music directory: " + musicDirectory);
            }
            
            List<String> downloadedFiles = new ArrayList<>();
            int totalDurationSeconds = 0;
            int downloadedCount = 0;
            
            for (JsonNode track : tracks) {
                try {
                    String trackName = track.path("name").asText("unknown");
                    String artistName = track.path("artist_name").asText("unknown");
                    String audioUrl = track.path("audio").asText();
                    int duration = track.path("duration").asInt();
                    
                    if (audioUrl.isEmpty()) {
                        logger.warn("No audio URL found for track: {} by {}", trackName, artistName);
                        continue;
                    }
                    
                    // Clean filename
                    String filename = sanitizeFilename(artistName + " - " + trackName) + ".mp3";
                    String filePath = musicDirectory + "/" + filename;
                    
                    // Download the track
                    if (downloadTrack(audioUrl, filePath)) {
                        downloadedFiles.add(filePath);
                        totalDurationSeconds += duration;
                        downloadedCount++;
                        
                        logger.info("Downloaded: {} ({} seconds)", filename, duration);
                    }
                    
                } catch (Exception e) {
                    logger.error("Failed to download track: {}", e.getMessage());
                }
            }
            
            if (downloadedFiles.isEmpty()) {
                throw new RuntimeException("No tracks were successfully downloaded");
            }
            
            int totalDurationMinutes = totalDurationSeconds / 60;
            
            logger.info("Download completed: {} tracks, {} minutes total", downloadedCount, totalDurationMinutes);
            
            return new MusicResponse(
                true,
                musicDirectory,
                downloadedFiles,
                downloadedCount,
                totalDurationMinutes,
                genre,
                sessionId
            );
        });
    }
    
    private boolean downloadTrack(String audioUrl, String filePath) {
        try {
            logger.debug("Downloading track from: {} to: {}", audioUrl, filePath);
            
            URL url = new URL(audioUrl);
            try (InputStream inputStream = url.openStream();
                 FileOutputStream outputStream = new FileOutputStream(filePath)) {
                
                IOUtils.copy(inputStream, outputStream);
                return true;
            }
            
        } catch (Exception e) {
            logger.error("Failed to download track from {}: {}", audioUrl, e.getMessage());
            return false;
        }
    }
    
    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_")
                      .replaceAll("_{2,}", "_")
                      .replaceAll("^_|_$", "");
    }
} 