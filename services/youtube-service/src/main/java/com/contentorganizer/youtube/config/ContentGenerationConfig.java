package com.contentorganizer.youtube.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "youtube.content-generation")
public class ContentGenerationConfig {
    private TagConfig tags = new TagConfig();

    @Data
    public static class TagConfig {
        private LofiConfig lofi = new LofiConfig();
    }

    @Data
    public static class LofiConfig {
        private String titlePrompt;
        private String descriptionPrompt;
        private String tagsPrompt;
        private String category = "10"; // Music
        private String defaultPrivacy = "private";
    }
} 