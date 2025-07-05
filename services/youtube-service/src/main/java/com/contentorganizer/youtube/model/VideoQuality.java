package com.contentorganizer.youtube.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoQuality {
    LOW(640, 360, 1000000),    // 1 Mbps
    MEDIUM(1280, 720, 2500000), // 2.5 Mbps
    HIGH(1920, 1080, 4500000);  // 4.5 Mbps

    private final int width;
    private final int height;
    private final int bitrate;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBitrate() {
        return bitrate;
    }
} 