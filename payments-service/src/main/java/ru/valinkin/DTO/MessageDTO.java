package ru.valinkin.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageDTO {

    private String id;

    private String aggregateType;

    private String aggregateId;

    private String eventType;

    private String payload;

    private LocalDateTime createdAt;

    private boolean sent = false;

    private LocalDateTime sentAt;

    private int attempts = 0;

    private String lastError;

    public MessageDTO() {}

    public MessageDTO(String id,
                      String aggregateType,
                      String aggregateId,
                      String eventType,
                      String payload,
                      LocalDateTime createdAt,
                      boolean sent,
                      LocalDateTime sentAt,
                      int attempts,
                      String lastError){
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.sent = sent;
        this.sentAt = sentAt;
        this.attempts = attempts;
        this.lastError = lastError;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAggregateType() { return aggregateType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }
}