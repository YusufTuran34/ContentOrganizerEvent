package com.contentorganizer.youtube.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "youtube")
public class YouTubeConfig {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String rtmpUrl;
    private StreamConfig stream;

    // Convenience methods to match the property names in application.properties
    public String getGoogleClientId() {
        return clientId;
    }

    public String getGoogleClientSecret() {
        return clientSecret;
    }

    public String getStreamKey() {
        return stream.getKey();
    }

    public String getStreamQuality() {
        return stream.getQuality().name();
    }

    public int getStreamBitrate() {
        return stream.getBitrate();
    }

    public int getStreamFrameRate() {
        return stream.getFrameRate();
    }
} 