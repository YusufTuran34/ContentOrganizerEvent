package com.contentorganizer.common.util;

import com.contentorganizer.common.event.BaseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Utility class for publishing events to Redis
 */
@Component
public class EventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);
    
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public EventPublisher(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Publishes an event to Redis pub/sub channel
     * 
     * @param event The event to publish
     */
    public void publishEvent(BaseEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            String channelName = event.getEventType();
            
            logger.info("Publishing event to channel '{}': {}", channelName, event);
            
            redisTemplate.convertAndSend(channelName, eventJson);
            
            logger.info("Event published successfully to channel '{}'", channelName);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event: {}", event, e);
            throw new RuntimeException("Failed to publish event", e);
        } catch (Exception e) {
            logger.error("Failed to publish event to Redis: {}", event, e);
            throw new RuntimeException("Failed to publish event to Redis", e);
        }
    }
    
    /**
     * Publishes an event to a specific channel
     * 
     * @param channelName The channel name to publish to
     * @param event The event to publish
     */
    public void publishEvent(String channelName, BaseEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            
            logger.info("Publishing event to channel '{}': {}", channelName, event);
            
            redisTemplate.convertAndSend(channelName, eventJson);
            
            logger.info("Event published successfully to channel '{}'", channelName);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event: {}", event, e);
            throw new RuntimeException("Failed to publish event", e);
        } catch (Exception e) {
            logger.error("Failed to publish event to Redis: {}", event, e);
            throw new RuntimeException("Failed to publish event to Redis", e);
        }
    }
} 