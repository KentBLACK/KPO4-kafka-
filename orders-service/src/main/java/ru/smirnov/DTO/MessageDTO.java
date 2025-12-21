package ru.smirnov.DTO;

public class MessageDTO {
    private String eventType;

    private Long orderId;

    public MessageDTO() {}

    public MessageDTO(String eventType, Long orderId)
    {
        this.eventType = eventType;
        this.orderId = orderId;
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
}
