package ru.smirnov.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Long orderId) {
        super("Заказ с ID " + orderId + " не найден");
    }

    public OrderNotFoundException(Long userId, boolean byUser) {
        super("Заказ для пользователя " + userId + " не найден");
    }
}