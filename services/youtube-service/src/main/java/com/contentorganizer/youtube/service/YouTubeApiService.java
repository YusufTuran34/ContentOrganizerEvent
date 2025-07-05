package com.contentorganizer.youtube.service;

import com.contentorganizer.youtube.config.YouTubeConfig;
import com.contentorganizer.youtube.model.UploadResult;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class YouTubeApiService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Content Organizer";

    private final YouTubeConfig youTubeConfig;
    private YouTube youTube;

    private synchronized YouTube getYouTubeService() throws IOException, GeneralSecurityException {
        if (youTube == null) {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = authorize();
            youTube = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        }
        return youTube;
    }

    private Credential authorize() throws IOException {
        try {
            return new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(youTubeConfig.getClientId(), youTubeConfig.getClientSecret())
                .build()
                .setRefreshToken(youTubeConfig.getRefreshToken());
        } catch (Exception e) {
            log.error("Failed to create credentials from refresh token", e);
            throw new IOException("Failed to authorize with YouTube", e);
        }
    }

    public Mono<UploadResult> uploadVideo(
        String videoPath,
        String title,
        String description,
        List<String> tags,
        String privacyStatus
    ) {
        return Mono.fromCallable(() -> {
            try {
                YouTube youtube = getYouTubeService();

                Video videoMetadata = new Video();
                VideoStatus status = new VideoStatus();
                status.setPrivacyStatus(privacyStatus);
                videoMetadata.setStatus(status);

                VideoSnippet snippet = new VideoSnippet();
                snippet.setTitle(title);
                snippet.setDescription(description);
                snippet.setTags(tags);
                videoMetadata.setSnippet(snippet);

                File videoFile = new File(videoPath);
                InputStreamContent mediaContent = new InputStreamContent(
                    "video/*",
                    new FileInputStream(videoFile)
                );
                mediaContent.setLength(videoFile.length());

                Video uploadedVideo = youtube.videos()
                    .insert(Collections.singletonList("snippet,status"), videoMetadata, mediaContent)
                    .execute();

                return UploadResult.builder()
                    .success(true)
                    .message("Video uploaded successfully")
                    .videoId(uploadedVideo.getId())
                    .build();

            } catch (GoogleJsonResponseException e) {
                log.error("Failed to upload video to YouTube: {}", e.getDetails().getMessage());
                return UploadResult.builder()
                    .success(false)
                    .message("YouTube API error: " + e.getDetails().getMessage())
                    .build();
            } catch (Exception e) {
                log.error("Failed to upload video to YouTube", e);
                return UploadResult.builder()
                    .success(false)
                    .message("Upload failed: " + e.getMessage())
                    .build();
            }
        });
    }

    public Mono<Video> getVideoInfo(String videoId) {
        return Mono.fromCallable(() -> {
            try {
                return getYouTubeService().videos()
                    .list(Collections.singletonList("snippet,status"))
                    .setId(Collections.singletonList(videoId))
                    .execute()
                    .getItems()
                    .get(0);
            } catch (Exception e) {
                log.error("Failed to get video info", e);
                throw e;
            }
        });
    }

    public Mono<Boolean> validateCredentials() {
        return Mono.fromCallable(() -> {
            try {
                getYouTubeService().videos()
                    .list(Collections.singletonList("snippet"))
                    .setMaxResults(1L)
                    .execute();
                return true;
            } catch (Exception e) {
                log.error("Failed to validate YouTube credentials", e);
                return false;
            }
        });
    }
} 