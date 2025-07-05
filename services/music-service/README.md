# Music Service

Music Service is a Spring Boot microservice that provides copyright-free music download capabilities using the Jamendo API. It's part of the Content Organizer Event Driven System.

## Features

- **Copyright-Free Music**: Downloads Creative Commons licensed music from Jamendo
- **Date-Based Organization**: Automatically organizes music files by date
- **Genre-Based Search**: Supports multiple music genres
- **Duration Planning**: Downloads music based on specified duration requirements
- **REST API**: Comprehensive REST endpoints with Swagger documentation
- **Health Monitoring**: Built-in health checks and actuator endpoints
- **Event-Driven Architecture**: Publishes events for completed downloads

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring WebFlux** (Reactive programming)
- **MongoDB** (Document storage)
- **Redis** (Event messaging)
- **Swagger/OpenAPI 3.0** (API documentation)
- **Jamendo API** (Music source)
- **Docker** (Containerization)

## Quick Start

### Prerequisites

- Java 17+
- MongoDB
- Redis
- Jamendo API credentials

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/YusufTuran34/ContentOrganizerEvent.git
   cd ContentOrganizerEventDrivenSystem/services/music-service
   ```

2. **Set up credentials**
   ```bash
   cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
   ```
   Edit `application-local.properties` with your Jamendo API credentials.

3. **Build and run**
   ```bash
   ./gradlew bootRun
   ```

### Docker Setup

1. **Build Docker image**
   ```bash
   docker build -t music-service .
   ```

2. **Run with Docker Compose**
   ```bash
   # From project root
   docker-compose up music-service
   ```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JAMENDO_CLIENT_ID` | Jamendo API client ID | - |
| `JAMENDO_CLIENT_SECRET` | Jamendo API client secret | - |
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017/contentorganizer` |
| `REDIS_HOST` | Redis server host | `localhost` |
| `REDIS_PORT` | Redis server port | `6379` |

### Application Properties

```properties
# Music Service Configuration
server.port=8081
spring.application.name=music-service

# Jamendo API Configuration
jamendo.api.client-id=${JAMENDO_CLIENT_ID}
jamendo.api.client-secret=${JAMENDO_CLIENT_SECRET}
jamendo.api.base-url=https://api.jamendo.com/v3.0

# Music Download Configuration
music.download.base-path=./music-downloads
music.download.max-concurrent-downloads=5
```

## API Endpoints

### Music Download

**POST** `/api/music/download`

Downloads copyright-free music based on genre and duration requirements.

**Request Body:**
```json
{
  "genre": "electronic",
  "durationHours": 2
}
```

**Response:**
```json
{
  "success": true,
  "error": null,
  "musicDirectoryPath": "/music/2024-07-05/electronic",
  "musicFilePaths": [
    "/music/2024-07-05/electronic/Artist_-_Track1.mp3",
    "/music/2024-07-05/electronic/Artist_-_Track2.mp3"
  ],
  "totalTracks": 15,
  "totalDurationMinutes": 125,
  "genre": "electronic",
  "downloadedAt": "2024-07-05T12:30:45",
  "sessionId": "music-session-12345678"
}
```

### Available Genres

**GET** `/api/music/genres`

Returns a list of available music genres.

**Response:**
```json
{
  "genres": [
    "electronic", "rock", "jazz", "classical", "pop",
    "ambient", "lofi", "chillout", "world", "folk",
    "blues", "country", "reggae", "funk", "metal"
  ],
  "note": "These are the most common genres available in Jamendo."
}
```

### Health Check

**GET** `/api/health`

Returns the health status of the service.

**Response:**
```json
"Music Service is running"
```

### Service Status

**GET** `/api/status`

Returns detailed status information.

**Response:**
```json
{
  "service": "Music Service",
  "version": "1.0.0",
  "status": "RUNNING",
  "description": "Copyright-free music download service using Jamendo API",
  "supportedFormats": ["MP3"],
  "features": [
    "Date-based file organization",
    "Genre-based music search",
    "Duration-based download planning",
    "Creative Commons licensed music"
  ]
}
```

## Swagger Documentation

Access the interactive API documentation at:
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs

## Health Monitoring

Spring Boot Actuator endpoints are available at:
- **Health**: http://localhost:8081/actuator/health
- **Info**: http://localhost:8081/actuator/info
- **Metrics**: http://localhost:8081/actuator/metrics

## File Organization

Downloaded music files are automatically organized in the following structure:

```
music-downloads/
├── 2024-07-05/
│   ├── electronic/
│   │   ├── Artist_-_Track1.mp3
│   │   └── Artist_-_Track2.mp3
│   └── jazz/
│       ├── Artist_-_Track1.mp3
│       └── Artist_-_Track2.mp3
└── 2024-07-06/
    └── rock/
        ├── Artist_-_Track1.mp3
        └── Artist_-_Track2.mp3
```

## Testing

### Unit Tests

```bash
./gradlew test
```

### Integration Tests

```bash
./gradlew integrationTest
```

### API Testing with curl

```bash
# Download electronic music for 2 hours
curl -X POST http://localhost:8081/api/music/download \
  -H "Content-Type: application/json" \
  -d '{"genre": "electronic", "durationHours": 2}'

# Get available genres
curl http://localhost:8081/api/music/genres

# Check health
curl http://localhost:8081/api/health
```

## Event Publishing

The service publishes the following events:

- `MusicDownloadStartedEvent`: When a download request is received
- `MusicDownloadCompletedEvent`: When music download is completed
- `MusicDownloadFailedEvent`: When music download fails

## Error Handling

The service handles various error scenarios:

- **Invalid genre**: Returns 400 Bad Request
- **API failures**: Returns 500 Internal Server Error with details
- **File system errors**: Returns 500 Internal Server Error
- **Network timeouts**: Automatic retry with exponential backoff

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please create an issue in the GitHub repository.

---

**Note**: This service requires valid Jamendo API credentials to function. Register at [Jamendo Developer Portal](https://developer.jamendo.com/) to obtain your API keys. 