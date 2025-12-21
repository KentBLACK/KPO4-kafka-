package ru.smirnov.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inbox_messages")
public class InboxMessage {

    @Id
    private String id;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processing_attempts", nullable = false)
    private int processingAttempts = 0;

    @Column(name = "last_error")
    private String lastError;

    public InboxMessage() {
        // Пустой конструктор для JPA
    }

    public InboxMessage(Long orderId, String payload) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getProcessingAttempts() {
        return processingAttempts;
    }

    public void setProcessingAttempts(int processingAttempts) {
        this.processingAttempts = processingAttempts;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    // Бизнес-методы
    public void markAsProcessed() {
        this.processed = true;
        this.processedAt = LocalDateTime.now();
    }

    public void incrementAttempts(String error) {
        this.processingAttempts++;
        this.lastError = error;
    }

    @Override
    public String toString() {
        return "InboxMessage{" +
                "id='" + id + '\'' +
                ", processed=" + processed +
                ", createdAt=" + createdAt +
                '}';
    }
}