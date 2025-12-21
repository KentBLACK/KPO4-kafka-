package ru.smirnov.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
public class OutboxMessage {

    @Id
    private String id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "order_Id")
    private Long orderId;

    @Column(nullable = false)
    private boolean sent = false;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "attempts", nullable = false)
    private int attempts = 0;

    @Column(name = "last_error")
    private String lastError;

    public OutboxMessage() {
        // Пустой конструктор для JPA
    }

    public OutboxMessage(String eventType, Long orderId) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.orderId = orderId;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    // Бизнес-методы
    public void markAsSent() {
        this.sent = true;
        this.sentAt = LocalDateTime.now();
    }

    public void incrementAttempts(String error) {
        this.attempts++;
        this.lastError = error;
    }

    @Override
    public String toString() {
        return "OutboxMessage{" +
                "id='" + id + '\'' +
                ", eventType='" + eventType + '\'' +
                ", sent=" + sent +
                ", createdAt=" + createdAt +
                '}';
    }
}