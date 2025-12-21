package ru.smirnov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.exceptions.OrderNotFoundException;
import ru.smirnov.models.Order;
import ru.smirnov.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxService outboxService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OutboxService outboxService) {
        this.orderRepository = orderRepository;
        this.outboxService = outboxService;
    }

    public Order getOrderByUserId(String userid) {
        return orderRepository.findByUserId(userid).orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));
    }

    public Order getOrderById(Long Id){
        return orderRepository.findById(Id).orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));
    }

    @Transactional
    public void save(Order order){
        orderRepository.save(order);
        outboxService.saveMessage(order);
    }

    @Transactional
    public void update(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrderByUserId(String id) {
        return orderRepository.getAllByUserId(id);
    }
}
