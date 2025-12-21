package ru.smirnov.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smirnov.DTO.MessageDTO;
import ru.smirnov.Enums.OrderStatus;
import ru.smirnov.exceptions.ConvertToDTOException;
import ru.smirnov.exceptions.OrderNotFoundException;
import ru.smirnov.models.Order;
import ru.smirnov.services.OrderService;

@Service
public class KafkaConsumer {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payments_topic", groupId = "orders")
    public void handleOrderCreated(String message){
        System.out.println("Получена информация о заказе: " + message);
        try {
            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);
            Order order = orderService.getOrderById(messageDTO.getOrderId());
            if (messageDTO.getEventType().equals("SUCCESSFUL")){
                order.setStatus(OrderStatus.FINISHED);
            }
            else{
                order.setStatus(OrderStatus.CANCELLED);
            }
            orderService.update(order);
        } catch (JsonProcessingException e){
            throw new ConvertToDTOException("Преобразование в DTO не получилось");
        }
        catch (OrderNotFoundException e){
            System.out.println("Заказ не найден...");
        }
    }
}
