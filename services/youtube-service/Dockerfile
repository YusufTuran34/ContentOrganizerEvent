# YouTube Service Dockerfile with FFmpeg support

# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create directories for FFmpeg
RUN mkdir -p /app/temp-videos /app/processed-videos

# Copy the built JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Copy application properties
COPY src/main/resources/application.properties .
COPY src/main/resources/application-local.properties .

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"] 