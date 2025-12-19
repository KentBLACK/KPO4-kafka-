package ru.smirnov.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.DTO.MessageDTO;
import ru.smirnov.exceptions.ConvertToDTOException;
import ru.smirnov.models.InboxMessage;
import ru.smirnov.services.InboxMessageService;

@Service
public class KafkaConsumer {
    private final ObjectMapper objectMapper;
    private final InboxMessageService inboxMessageService;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, InboxMessageService inboxMessageService) {
        this.objectMapper = objectMapper;
        this.inboxMessageService = inboxMessageService;
    }

    @KafkaListener(topics = "orders_topic", groupId = "payments")
    public void Listener(String message){
        System.out.println("Получил сообщение из order_topic: " + message);
        try {
            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);
            inboxMessageService.save(new InboxMessage(messageDTO.getId(), messageDTO.getPayload()));
        } catch (JsonProcessingException e) {
            throw new ConvertToDTOException(message = "Преобразование в DTO не получилось");
        }
    }
}
