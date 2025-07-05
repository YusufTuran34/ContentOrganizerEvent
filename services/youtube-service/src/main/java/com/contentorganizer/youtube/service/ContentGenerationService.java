package com.contentorganizer.youtube.service;

import com.contentorganizer.youtube.config.ContentGenerationConfig;
import com.contentorganizer.youtube.model.VideoMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentGenerationService {

    private final WebClient chatGptWebClient;
    private final ContentGenerationConfig contentGenerationConfig;

    public Mono<GeneratedMetadata> generateMetadata(VideoMetadata metadata) {
        return Mono.zip(
            generateTitle(metadata),
            generateDescription(metadata),
            generateTags(metadata)
        ).map(tuple -> new GeneratedMetadata(
            tuple.getT1(),
            tuple.getT2(),
            tuple.getT3()
        ));
    }

    public Mono<String> generateTitle(VideoMetadata metadata) {
        String prompt = contentGenerationConfig.getTags().getLofi().getTitlePrompt()
            .replace("{duration}", formatDuration(metadata.getDurationSeconds()));

        return generateContent(prompt);
    }

    public Mono<String> generateDescription(VideoMetadata metadata) {
        String prompt = contentGenerationConfig.getTags().getLofi().getDescriptionPrompt()
            .replace("{duration}", formatDuration(metadata.getDurationSeconds()));

        return generateContent(prompt);
    }

    public Mono<List<String>> generateTags(VideoMetadata metadata) {
        String prompt = contentGenerationConfig.getTags().getLofi().getTagsPrompt()
            .replace("{duration}", formatDuration(metadata.getDurationSeconds()));

        return generateContent(prompt)
            .map(tags -> Arrays.asList(tags.split(",")));
    }

    private Mono<String> generateContent(String prompt) {
        return chatGptWebClient.post()
            .uri("/generate")
            .bodyValue(prompt)
            .retrieve()
            .bodyToMono(String.class)
            .doOnError(e -> log.error("Failed to generate content: {}", e.getMessage()));
    }

    private String formatDuration(long seconds) {
        Duration duration = Duration.ofSeconds(seconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long remainingSeconds = duration.toSecondsPart();

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
        } else {
            return String.format("%d:%02d", minutes, remainingSeconds);
        }
    }

    public static class GeneratedMetadata {
        private final String title;
        private final String description;
        private final List<String> tags;

        public GeneratedMetadata(String title, String description, List<String> tags) {
            this.title = title;
            this.description = description;
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getTags() {
            return tags;
        }
    }
} 