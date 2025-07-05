# YouTube Service

Content Organizer YouTube Service provides comprehensive video upload and live streaming capabilities to YouTube with AI-powered content generation using ChatGPT integration.

## Features

- 🎥 **Video Upload**: Upload videos to YouTube with AI-generated metadata
- 📺 **Live Streaming**: Stream videos to YouTube Live using FFmpeg
- 🤖 **AI Content Generation**: Automatic title, description, and tags generation using ChatGPT
- 🏷️ **Tag-based Configuration**: Configurable content generation based on video tags
- 🔧 **FFmpeg Integration**: Video processing and streaming capabilities
- 📊 **Health Monitoring**: Spring Boot Actuator integration
- 📚 **API Documentation**: Comprehensive Swagger/OpenAPI documentation

## Technology Stack

- **Java 17** with Spring Boot 3.2.0
- **Google YouTube Data API v3** for video uploads
- **FFmpeg** for video processing and streaming
- **ChatGPT Service** integration for content generation
- **MongoDB** for data persistence
- **Redis** for caching and messaging
- **Docker** containerization support

## Quick Start

### Prerequisites

- Java 17+
- FFmpeg installed
- Google OAuth credentials for YouTube API
- YouTube channel with live streaming enabled
- ChatGPT service running (default: localhost:8080)
- MongoDB and Redis running

### Installation

1. **Clone and navigate to service:**
   ```bash
   cd services/youtube-service
   ```

2. **Configure credentials:**
   ```bash
   cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
   ```
   
   Edit `application-local.properties`:
   ```properties
   GOOGLE_CLIENT_ID=your-google-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   GOOGLE_REFRESH_TOKEN=your-refresh-token
   YOUTUBE_STREAM_KEY=your-stream-key
   ```

3. **Build and run:**
   ```bash
   ./gradlew bootRun
   ```

### Docker Setup

```bash
# Build Docker image
./gradlew build
docker build -t contentorganizer/youtube-service:1.0.0 .

# Run with Docker Compose (from project root)
docker-compose up youtube-service
```

## API Documentation

### Base URL
- **Development**: `http://localhost:8085`
- **Docker**: `http://localhost:8085`

### Swagger UI
- **URL**: `http://localhost:8085/swagger-ui.html`
- **API Docs**: `http://localhost:8085/api-docs`

## API Endpoints

### 1. Upload Video to YouTube

**POST** `/api/youtube/upload`

Uploads a video file to YouTube with AI-generated or custom metadata.

#### Request Body
```json
{
    "videoPath": "/path/to/video.mp4",
    "tag": "lofi",
    "customTitle": "My Custom Video Title",
    "customDescription": "Custom description for the video",
    "customTags": "custom, tags, here",
    "privacyStatus": "private",
    "categoryId": "10",
    "thumbnailPath": "/path/to/thumbnail.jpg"
}
```

#### Response
```json
{
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "status": "SUCCESS",
    "message": "Video uploaded successfully to YouTube",
    "videoId": "dQw4w9WgXcQ",
    "videoUrl": "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    "generatedTitle": "🎵 Relaxing Lofi Hip Hop Mix - 1 Hour of Chill Beats",
    "generatedDescription": "Perfect background music for studying, working, or relaxing...",
    "generatedTags": "lofi, hip hop, study music, chill beats, relaxing music",
    "videoPath": "/path/to/video.mp4",
    "tag": "lofi",
    "durationSeconds": 3600,
    "fileSizeBytes": 104857600,
    "startTime": "2024-01-01T12:00:00",
    "endTime": "2024-01-01T12:05:30",
    "processingTimeMs": "330000"
}
```

### 2. Start Live Stream to YouTube

**POST** `/api/youtube/stream`

Starts a live stream to YouTube using FFmpeg with the provided video file.

#### Request Body
```json
{
    "videoPath": "/path/to/video.mp4",
    "tag": "lofi",
    "customTitle": "My Live Stream Title",
    "customDescription": "Custom description for the stream",
    "quality": "720p",
    "bitrate": 2500,
    "fps": 30,
    "durationMinutes": 60,
    "loop": true
}
```

#### Response
```json
{
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "status": "STREAMING",
    "message": "Live stream started successfully",
    "streamUrl": "https://www.youtube.com/c/YourChannelName/live",
    "generatedTitle": "🔴 LIVE: Lofi Hip Hop Radio - Beats to Relax/Study",
    "generatedDescription": "24/7 lofi hip hop stream for studying, working, and relaxing...",
    "generatedTags": "lofi, live stream, study music, 24/7, chill beats",
    "videoPath": "/path/to/video.mp4",
    "tag": "lofi",
    "durationSeconds": 3600,
    "fileSizeBytes": 104857600,
    "startTime": "2024-01-01T12:00:00",
    "processingTimeMs": "5000"
}
```

### 3. Validate Configuration

**GET** `/api/youtube/validate`

Validates YouTube API credentials and service configuration.

#### Response
```json
{
    "valid": true,
    "message": "YouTube service configuration is valid",
    "timestamp": "2024-01-01T12:00:00"
}
```

### 4. Service Status

**GET** `/api/youtube/status`

Returns the current status and configuration information of the YouTube service.

#### Response
```json
{
    "service": "YouTube Service",
    "version": "1.0.0",
    "status": "RUNNING",
    "supportedTags": ["lofi"],
    "features": ["Video Upload", "Live Streaming", "AI Content Generation"],
    "timestamp": "2024-01-01T12:00:00"
}
```

## Content Generation Configuration

The service supports tag-based content generation. Currently supported tags:

### Lofi Tag Configuration

```properties
# Title generation
youtube.content-generation.tags.lofi.title-prompt=Create a catchy YouTube title for a lofi hip hop video that is {duration} long. Include emojis and make it appealing for study/relax content. Keep it under 60 characters.

# Description generation  
youtube.content-generation.tags.lofi.description-prompt=Write a YouTube description for a {duration} lofi hip hop video. Include: 1) Welcome message 2) What the music is good for (study, work, relax) 3) Tracklist placeholder 4) Social media links placeholder 5) Copyright notice. Make it engaging and SEO-friendly.

# Tags generation
youtube.content-generation.tags.lofi.tags-prompt=Generate 10-15 relevant YouTube tags for a {duration} lofi hip hop video. Include genres, moods, and use cases. Return as comma-separated values.

# Default settings
youtube.content-generation.tags.lofi.category=10
youtube.content-generation.tags.lofi.default-privacy=private
```

### Adding New Tags

To add support for new content types, add configuration in `application.properties`:

```properties
# Example: Adding support for "jazz" tag
youtube.content-generation.tags.jazz.title-prompt=Create a YouTube title for a {duration} jazz music video...
youtube.content-generation.tags.jazz.description-prompt=Write a description for a {duration} jazz video...
youtube.content-generation.tags.jazz.tags-prompt=Generate tags for a {duration} jazz video...
youtube.content-generation.tags.jazz.category=10
youtube.content-generation.tags.jazz.default-privacy=private
```

## Usage Examples

### cURL Examples

#### Upload Video
```bash
curl -X POST "http://localhost:8085/api/youtube/upload" \
  -H "Content-Type: application/json" \
  -d '{
    "videoPath": "/path/to/lofi-video.mp4",
    "tag": "lofi",
    "privacyStatus": "public"
  }'
```

#### Start Live Stream
```bash
curl -X POST "http://localhost:8085/api/youtube/stream" \
  -H "Content-Type: application/json" \
  -d '{
    "videoPath": "/path/to/lofi-video.mp4", 
    "tag": "lofi",
    "quality": "720p",
    "loop": true
  }'
```

#### Validate Configuration
```bash
curl "http://localhost:8085/api/youtube/validate"
```

### Java/Spring Example

```java
@Autowired
private WebClient webClient;

public void uploadVideo() {
    VideoUploadRequest request = new VideoUploadRequest();
    request.setVideoPath("/path/to/video.mp4");
    request.setTag("lofi");
    request.setPrivacyStatus("private");
    
    VideoResponse response = webClient.post()
        .uri("http://localhost:8085/api/youtube/upload")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(VideoResponse.class)
        .block();
    
    System.out.println("Video uploaded: " + response.getVideoUrl());
}
```

## FFmpeg Configuration

### Video Processing Settings

The service automatically processes videos for YouTube compatibility:

- **Codec**: H.264 (libx264)
- **Audio**: AAC
- **Format**: MP4
- **Pixel Format**: yuv420p
- **Frame Rate**: 30fps

### Streaming Settings

Live streaming supports multiple quality options:

| Quality | Resolution | Default Bitrate |
|---------|------------|-----------------|
| 480p    | 854x480    | 1000 kbps      |
| 720p    | 1280x720   | 2500 kbps      |
| 1080p   | 1920x1080  | 4000 kbps      |

## Error Handling

The service provides comprehensive error handling with detailed error responses:

```json
{
    "sessionId": "error-session",
    "status": "FAILED",
    "message": "Video upload process failed",
    "errorCode": "PROCESSING_ERROR",
    "errorDetails": "Unsupported tag: unknown. Supported tags: lofi"
}
```

### Common Error Codes

- `PROCESSING_ERROR`: General processing error
- `UPLOAD_FAILED`: YouTube upload failed
- `STREAM_ERROR`: Live streaming error
- `REQUEST_ERROR`: Invalid request parameters
- `STREAM_REQUEST_ERROR`: Stream request error

## Health Monitoring

### Actuator Endpoints

- **Health**: `http://localhost:8085/actuator/health`
- **Info**: `http://localhost:8085/actuator/info`
- **Metrics**: `http://localhost:8085/actuator/metrics`

### Health Check Response
```json
{
    "status": "UP",
    "components": {
        "mongo": {
            "status": "UP"
        },
        "redis": {
            "status": "UP"
        }
    }
}
```

## Dependencies

### Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable YouTube Data API v3
4. Create OAuth 2.0 credentials
5. Add your domain to authorized origins
6. Generate refresh token using OAuth 2.0 Playground

### FFmpeg Installation

#### macOS (Homebrew)
```bash
brew install ffmpeg
```

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install ffmpeg
```

#### Windows
Download from [FFmpeg official website](https://ffmpeg.org/download.html)

## Development

### Running Tests
```bash
./gradlew test
```

### Building
```bash
./gradlew build
```

### Code Quality
```bash
./gradlew check
```

## Troubleshooting

### Common Issues

1. **FFmpeg not found**
   - Ensure FFmpeg is installed and in PATH
   - Check FFmpeg installation: `ffmpeg -version`

2. **YouTube API authentication failed**
   - Verify Google OAuth credentials
   - Check refresh token validity
   - Ensure YouTube Data API is enabled

3. **ChatGPT service unavailable**
   - Verify ChatGPT service is running
   - Check service URL configuration

4. **Video upload failed**
   - Check video file format and size
   - Verify YouTube API quotas
   - Check internet connectivity

### Logs

Check application logs for detailed error information:
```bash
# Development
./gradlew bootRun

# Docker
docker logs youtube-service
```

## License

This project is part of the Content Organizer Event Driven System. 