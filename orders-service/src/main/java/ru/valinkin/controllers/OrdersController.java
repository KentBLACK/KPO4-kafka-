package ru.valinkin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.valinkin.DTO.OrderDTO;
import ru.valinkin.models.Order;
import ru.valinkin.services.OrderService;
import ru.valinkin.services.OutboxService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders/")
@Tag(name = "Заказы", description = "Сервис заказов")
public class OrdersController {
    private final OrderService orderService;
    private final OutboxService outboxService;

    @Autowired
    public OrdersController(OrderService orderService, OutboxService outboxService) {
        this.orderService = orderService;
        this.outboxService = outboxService;
    }

    @GetMapping()
    @Operation(summary = "Получение заказов по имени заказчика")
    public List<OrderDTO> getOrders(@RequestParam("userId") String userId) {
        List<Order> orders = orderService.getAllOrderByUserId(userId);
        System.out.println("Кол-во найденных заказов: " + orders.size());
        List<OrderDTO> ordersDTO = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setUserId(order.getUserId());
            orderDTO.setAmount(order.getAmount());
            orderDTO.setDescription(order.getDescription());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setCreatedAt(order.getCreatedAt());
            orderDTO.setUpdatedAt(order.getUpdatedAt());
            ordersDTO.add(orderDTO);
        }
        return ordersDTO;
    }

    @PostMapping()
    @Operation(summary = "Создание заказа")
    public void createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        System.out.println("Отправил сохраняться в orderService");
        orderService.save(new Order(createOrderDTO.getUserId(), createOrderDTO.getAmount(), createOrderDTO.getDescription()));
    }

    public static class CreateOrderDTO{
        private String userId;
        private BigDecimal amount;
        private String description;

        public CreateOrderDTO(){}

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
