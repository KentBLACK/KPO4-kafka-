package ru.smirnov.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.kafka.KafkaProducer;
import ru.smirnov.models.Order;
import ru.smirnov.models.OutboxMessage;
import ru.smirnov.repositories.OutboxRepository;

import java.util.List;

@Service
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public OutboxService(OutboxRepository outboxRepository, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }
    @Transactional
    public void saveMessage(Order order){
        OutboxMessage outboxMessage = new OutboxMessage("Order", order.getId().toString(), "ORDER_CREATED", order.getUserId().toString());
        outboxRepository.save(outboxMessage);
    }

    @Transactional
    @Scheduled(
        fixedDelay = 1000, initialDelay = 5000
    )
    public void sendOutboxMessages() {
        List<OutboxMessage> unsent = outboxRepository.findBySent(false);
        for (OutboxMessage msg : unsent) {
            try {
                kafkaProducer.sendMessage(objectMapper.writeValueAsString(msg));
                msg.markAsSent();
            } catch (Exception e) {
                msg.incrementAttempts(e.getMessage());
            }
            outboxRepository.save(msg);
        }
    }
}
