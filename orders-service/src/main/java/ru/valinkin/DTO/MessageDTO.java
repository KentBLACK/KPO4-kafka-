package ru.valinkin.DTO;

public class MessageDTO {
    private String eventType;

    private String userId;

    public MessageDTO() {}

    public MessageDTO(String eventType, String userId)
    {
        this.eventType = eventType;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
