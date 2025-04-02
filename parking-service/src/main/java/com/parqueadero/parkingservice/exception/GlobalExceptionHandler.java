package com.parqueadero.parkingservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParkingException.class)
    public ResponseEntity<Object> handleParkingException(ParkingException ex) {
        String body = "{\"mensaje\": \"" + ex.getMessage() + "\"}";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}
