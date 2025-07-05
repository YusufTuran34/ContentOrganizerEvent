# ChatGPT Service

OpenAI ChatGPT integration service for the Content Organizer Event Driven System.

## Overview

This service provides a REST API for integrating with OpenAI's ChatGPT API. It handles prompt processing and returns AI-generated content responses.

## Features

- **OpenAI Integration**: Direct integration with ChatGPT API
- **Reactive Architecture**: Built with Spring WebFlux for non-blocking operations
- **Input Validation**: Comprehensive request validation with meaningful error messages
- **Error Handling**: Robust error handling with detailed error responses
- **API Documentation**: Complete Swagger/OpenAPI 3.0 documentation
- **Health Checks**: Spring Boot Actuator for monitoring and health checks
- **Security**: Environment-based configuration for sensitive data
- **Docker Support**: Full containerization with multi-stage builds

## Tech Stack

- **Java 17** - Modern Java features
- **Spring Boot 3.2.0** - Application framework
- **Spring WebFlux** - Reactive web framework
- **Spring Boot Actuator** - Production monitoring
- **Swagger/OpenAPI 3.0** - API documentation
- **Docker** - Containerization

## API Endpoints

### Chat Endpoints

#### POST /api/chat
Send a prompt to ChatGPT and receive an AI-generated response.

**Request Body:**
```json
{
  "prompt": "Lofi radio için tanıtım mesajı"
}
```

**Response:**
```json
{
  "response": "🎵 Lofi Radio - Sakin ve Huzurlu Müzikler...",
  "tokensUsed": 150,
  "model": "gpt-3.5-turbo",
  "success": true,
  "error": null
}
```

### Health & Status Endpoints

#### GET /actuator/health
Spring Boot Actuator health check endpoint.

#### GET /api/health
Custom health check endpoint.

#### GET /api/status
Detailed service status information.

## Configuration

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `OPENAI_API_KEY` | OpenAI API key | - | Yes |
| `openai.api.model` | OpenAI model to use | gpt-3.5-turbo | No |
| `openai.api.max-tokens` | Maximum tokens per request | 150 | No |
| `openai.api.temperature` | Response creativity (0.0-2.0) | 0.7 | No |

### Application Properties

```properties
# OpenAI Configuration
openai.api.key=${OPENAI_API_KEY}
openai.api.model=gpt-3.5-turbo
openai.api.max-tokens=150
openai.api.temperature=0.7

# Server Configuration
server.port=8080
spring.application.name=chatgpt-service
```

## Quick Start

### Prerequisites

- Java 17+
- OpenAI API key
- Docker (optional)

### Local Development

1. **Clone and navigate to the service:**
   ```bash
   cd services/chatgpt-service
   ```

2. **Set up local configuration:**
   ```bash
   cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
   ```

3. **Add your OpenAI API key:**
   ```bash
   echo "OPENAI_API_KEY=your_api_key_here" >> src/main/resources/application-local.properties
   ```

4. **Run the service:**
   ```bash
   ../../gradlew bootRun
   ```

5. **Access Swagger UI:**
   - http://localhost:8080/swagger-ui.html

6. **Test the API:**
   ```bash
   curl -X POST http://localhost:8080/api/chat \
     -H "Content-Type: application/json" \
     -d '{"prompt": "Lofi radio için tanıtım mesajı"}'
   ```

### Docker Deployment

1. **Build and run with Docker Compose:**
   ```bash
   # From project root
   docker-compose up chatgpt-service
   ```

2. **Access the service:**
   - API: http://localhost:8086/api/chat
   - Swagger UI: http://localhost:8086/swagger-ui.html
   - Health Check: http://localhost:8086/actuator/health

## API Testing Examples

### Using curl

```bash
# Test chat endpoint
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Write a short promotional message for a lofi radio station"
  }'

# Health check
curl http://localhost:8080/actuator/health

# Service status
curl http://localhost:8080/api/status
```

### Using Swagger UI

1. Navigate to http://localhost:8080/swagger-ui.html
2. Click on "ChatGPT API" section
3. Try out the `/api/chat` endpoint with sample data
4. View response schemas and examples

## Development

### Building the Project

```bash
# Build the service
../../gradlew :services:chatgpt-service:build

# Run tests
../../gradlew :services:chatgpt-service:test

# Build Docker image
docker build -t chatgpt-service .
```

### Project Structure

```
chatgpt-service/
├── src/main/java/com/contentorganizer/chatgpt/
│   ├── ChatGptServiceApplication.java     # Main application class
│   ├── controller/
│   │   └── ChatController.java            # REST controllers
│   ├── dto/
│   │   ├── ChatRequest.java               # Request DTOs
│   │   └── ChatResponse.java              # Response DTOs
│   └── service/
│       └── ChatGptService.java            # Business logic
├── src/main/resources/
│   ├── application.properties             # Main configuration
│   └── application-local.properties.template # Local config template
├── build.gradle                          # Gradle build configuration
├── Dockerfile                            # Docker configuration
└── README.md                             # This file
```

## Monitoring

### Health Checks

The service provides multiple health check endpoints:

- **Actuator Health**: `/actuator/health` - Spring Boot standard health check
- **Custom Health**: `/api/health` - Simple service status
- **Status Info**: `/api/status` - Detailed service information

### Metrics

Access detailed metrics at:
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application information

### Logging

Configure logging levels in `application.properties`:

```properties
# Service-specific logging
logging.level.com.contentorganizer.chatgpt=INFO

# WebClient debugging
logging.level.org.springframework.web.reactive.function.client=DEBUG
```

## Error Handling

The service provides comprehensive error handling:

### Common Error Responses

```json
{
  "response": null,
  "tokensUsed": null,
  "model": null,
  "success": false,
  "error": "Invalid API key or unauthorized access"
}
```

### Error Types

- **400 Bad Request**: Invalid prompt or request parameters
- **401 Unauthorized**: Invalid or missing OpenAI API key
- **429 Too Many Requests**: Rate limit exceeded
- **500 Internal Server Error**: Service or OpenAI API errors

## Security

- **Environment Variables**: Sensitive data stored in environment variables
- **No Hardcoded Secrets**: All credentials loaded from environment
- **Input Validation**: Comprehensive request validation
- **Error Sanitization**: Sensitive information filtered from error responses

## Contributing

1. Follow the established code structure
2. Add comprehensive tests for new features
3. Update documentation for API changes
4. Use meaningful commit messages
5. Ensure Docker builds work correctly

## Support

For issues or questions related to the ChatGPT service:

1. Check the logs for detailed error information
2. Verify OpenAI API key is correctly configured
3. Ensure network connectivity to OpenAI API
4. Test with simple prompts first
5. Check rate limits and usage quotas 