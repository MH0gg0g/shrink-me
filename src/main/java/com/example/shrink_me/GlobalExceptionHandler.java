package com.example.shrink_me;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();

        ex.getBindingResult().getFieldErrors().forEach(er -> errors.put(er.getField(), er.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        var errorMap = Map.ofEntries(
                Map.entry("Timestamp", LocalDateTime.now()),
                Map.entry("status", HttpStatus.NOT_FOUND.value()),
                Map.entry("error", HttpStatus.NOT_FOUND.getReasonPhrase()),
                Map.entry("message", ex.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }
}
