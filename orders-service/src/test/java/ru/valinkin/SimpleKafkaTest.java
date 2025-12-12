package ru.valinkin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.valinkin.repositories.OrderRepository;
import ru.valinkin.repositories.OutboxRepository;

import static org.mockito.Mockito.mock;

@SpringBootTest
@EmbeddedKafka(topics = "test-topic")
class SimpleKafkaTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testKafkaConnection() {
        kafkaTemplate.send("test-topic", "Hello Kafka!");
    }
}