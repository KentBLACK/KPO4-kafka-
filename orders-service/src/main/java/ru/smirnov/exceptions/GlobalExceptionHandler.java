package ru.smirnov.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleSaveDocumentException(OrderNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(501));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleSaveDocumentException(ConvertToDTOException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(502));
    }

    @ExceptionHandler
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(550));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(500));
    }
}