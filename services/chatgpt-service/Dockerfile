# Multi-stage build for ChatGPT Service
FROM openjdk:17-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy shared-common module
COPY services/shared-common services/shared-common

# Copy source code
COPY services/chatgpt-service/src services/chatgpt-service/src
COPY services/chatgpt-service/build.gradle services/chatgpt-service/

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew :services:chatgpt-service:build -x test

# Production stage
FROM openjdk:17-jre-slim

# Create non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/services/chatgpt-service/build/libs/*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 