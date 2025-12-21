package ru.smirnov.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class OrderStatusDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Принимаем enum как строку (например, "NEW", "PROCESSING", etc.)
        // Может прийти как строка или как enum, в любом случае конвертируем в строку
        if (p.currentToken().isScalarValue()) {
            return p.getText();
        }
        return p.getValueAsString();
    }
}

