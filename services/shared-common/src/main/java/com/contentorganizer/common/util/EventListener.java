package com.contentorganizer.common.util;

import com.contentorganizer.common.event.BaseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Base class for event listeners
 */
public abstract class EventListener implements MessageListener {
    
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);
    
    private final ObjectMapper objectMapper;
    
    protected EventListener() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String eventJson = message.toString();
            String channelName = new String(message.getChannel());
            
            logger.info("Received event from channel '{}': {}", channelName, eventJson);
            
            handleEvent(channelName, eventJson);
            
        } catch (Exception e) {
            logger.error("Failed to process event from channel: {}", new String(message.getChannel()), e);
        }
    }
    
    /**
     * Handle the received event
     * 
     * @param channelName The channel name
     * @param eventJson The event JSON string
     */
    protected abstract void handleEvent(String channelName, String eventJson);
    
    /**
     * Deserialize event JSON to event object
     * 
     * @param eventJson The event JSON string
     * @param eventClass The event class type
     * @return The deserialized event object
     */
    protected <T extends BaseEvent> T deserializeEvent(String eventJson, Class<T> eventClass) {
        try {
            return objectMapper.readValue(eventJson, eventClass);
        } catch (Exception e) {
            logger.error("Failed to deserialize event: {}", eventJson, e);
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
} 