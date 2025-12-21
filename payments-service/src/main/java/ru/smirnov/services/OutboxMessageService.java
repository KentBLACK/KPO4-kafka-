package ru.smirnov.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.exceptions.ConvertToDTOException;
import ru.smirnov.kafka.KafkaProducer;
import ru.smirnov.models.OutboxMessage;
import ru.smirnov.repositories.OutboxMessageRepository;

import java.util.List;

@Service
public class OutboxMessageService {
    private final OutboxMessageRepository outboxMessageRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public OutboxMessageService(OutboxMessageRepository outboxMessageRepository, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void save(OutboxMessage outboxMessage) {
        outboxMessageRepository.save(outboxMessage);
    }

    @Transactional
    @Scheduled(
            fixedDelay = 1000,
            initialDelay = 5000
    )
    public void checkOutboxMessages() {
        List<OutboxMessage> messages = outboxMessageRepository.findBySent(false);
        for (OutboxMessage message : messages) {
            try {
                kafkaProducer.sendMessage(objectMapper.writeValueAsString(new SendDTO(message.getEventType(), message.getOrderId())));
                message.markAsSent();
            } catch (JsonProcessingException e){
                message.setAttempts(message.getAttempts() + 1);
                throw new ConvertToDTOException("При отправке в payments ошибка конвертации");
            }
            outboxMessageRepository.save(message);
        }
    }

    public static class SendDTO  {
        private String eventType;
        private Long orderId;

        public SendDTO() {
        }

        public SendDTO(String eventType, Long orderId) {
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
}
