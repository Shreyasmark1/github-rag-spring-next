package com.example.spring_rag.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    ResponseEntity<Map<String, Object>> handleBaseException(ApiException ex){
        return error(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Validation Failed");
        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<Map<String, Object>> handleGeneric(Throwable ex){
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus httpStatus, String message){
        return ResponseEntity.status(httpStatus).body(Map.of(
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "message", message,
                "timestamp", Instant.now().toString()
        ));
    }
}
