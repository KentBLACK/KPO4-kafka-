package ru.smirnov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.smirnov.DTO.*;

import java.util.List;

@Service
public class ApiService {

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    // Orders Service endpoints
    public Mono<List<OrderDTO>> getOrdersByUserId(String userId) {
        return webClient.get()
                .uri("/api/orders?userId={userId}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(new ParameterizedTypeReference<List<OrderDTO>>() {});
                    } else {
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(errorBody -> {
                                    return Mono.error(new RuntimeException("Ошибка при получении заказов: " + response.statusCode() + " - " + errorBody));
                                });
                    }
                });
    }

    public Mono<StatusDTO> getOrderStatus(Long orderId) {
        return webClient.get()
                .uri("/api/orders/status?orderId={orderId}", orderId)
                .retrieve()
                .bodyToMono(StatusDTO.class);
    }

    public Mono<Void> createOrder(CreateOrderDTO orderDTO) {
        return webClient.post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Payments Service endpoints
    public Mono<BalanceDTO> getBalance(String userId) {
        return webClient.get()
                .uri("/api/accounts/balance/?userId={userId}", userId)
                .retrieve()
                .bodyToMono(BalanceDTO.class);
    }

    public Mono<Void> createAccount(UserDTO userDTO) {
        return webClient.post()
                .uri("/api/accounts/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> upBalance(BalanceUpDTO balanceUpDTO) {
        return webClient.post()
                .uri("/api/accounts/upbalance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceUpDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

