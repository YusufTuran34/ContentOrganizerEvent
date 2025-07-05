package com.contentorganizer.music.controller;

import com.contentorganizer.music.dto.MusicRequest;
import com.contentorganizer.music.dto.MusicResponse;
import com.contentorganizer.music.service.JamendoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Tag(name = "Music API", description = "Copyright-free music download from Jamendo")
public class MusicController {
    
    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);
    
    private final JamendoService jamendoService;
    
    @Autowired
    public MusicController(JamendoService jamendoService) {
        this.jamendoService = jamendoService;
    }
    
    @PostMapping("/music/download")
    @Operation(
        summary = "Download copyright-free music",
        description = "Downloads copyright-free music from Jamendo based on genre and duration requirements. " +
                     "Files are organized in date-based directories and downloaded in MP3 format."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Music download completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error or external API failure")
    })
    public Mono<ResponseEntity<MusicResponse>> downloadMusic(
            @Parameter(description = "Music download request with genre and duration", required = true)
            @Valid @RequestBody MusicRequest request) {
        
        logger.info("Received music download request: {}", request);
        
        return jamendoService.downloadMusic(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        logger.info("Music download completed successfully: {} tracks, {} minutes", 
                            response.getTotalTracks(), response.getTotalDurationMinutes());
                        return ResponseEntity.ok(response);
                    } else {
                        logger.error("Music download failed: {}", response.getError());
                        return ResponseEntity.status(500).body(response);
                    }
                })
                .onErrorResume(Exception.class, ex -> {
                    logger.error("Unexpected error in music download endpoint", ex);
                    MusicResponse errorResponse = new MusicResponse("Internal server error: " + ex.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
    }
    
    @GetMapping("/music/genres")
    @Operation(
        summary = "Get available music genres",
        description = "Returns a list of available music genres supported by the service"
    )
    @ApiResponse(responseCode = "200", description = "Available genres retrieved successfully")
    public ResponseEntity<Object> getAvailableGenres() {
        logger.debug("Available genres requested");
        
        return ResponseEntity.ok(new Object() {
            public String[] genres = {
                "electronic", "rock", "jazz", "classical", "pop", 
                "ambient", "lofi", "chillout", "world", "folk",
                "blues", "country", "reggae", "funk", "metal"
            };
            public String note = "These are the most common genres available in Jamendo. " +
                               "Other genres might also be available.";
        });
    }
    
    // Health endpoint is handled by Spring Boot Actuator at /actuator/health
    // Custom health endpoint removed as per .cursorrules requirements
    
    @GetMapping("/status")
    @Operation(
        summary = "Service status endpoint",
        description = "Returns detailed status information about the Music service"
    )
    @ApiResponse(responseCode = "200", description = "Status information retrieved successfully")
    public ResponseEntity<Object> status() {
        logger.debug("Status check requested");
        
        return ResponseEntity.ok(new Object() {
            public String service = "Music Service";
            public String version = "1.0.0";
            public String status = "RUNNING";
            public String description = "Copyright-free music download service using Jamendo API";
            public String[] supportedFormats = {"MP3"};
            public String[] features = {
                "Date-based file organization",
                "Genre-based music search",
                "Duration-based download planning",
                "Creative Commons licensed music"
            };
        });
    }
} 