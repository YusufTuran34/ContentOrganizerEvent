# Build stage
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy shared-common dependency
COPY services/shared-common services/shared-common

# Copy source code
COPY services/music-service/src services/music-service/src
COPY services/music-service/build.gradle services/music-service/

# Build the application
RUN chmod +x gradlew
RUN ./gradlew :services:music-service:bootJar --no-daemon

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Install ffmpeg for audio processing (optional)
RUN apt-get update && apt-get install -y \
    ffmpeg \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Create directory for music downloads
RUN mkdir -p /app/music-downloads

# Copy the built JAR from builder stage
COPY --from=builder /app/services/music-service/build/libs/*.jar app.jar

# Create non-root user
RUN groupadd -r musicservice && useradd -r -g musicservice musicservice
RUN chown -R musicservice:musicservice /app
USER musicservice

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 