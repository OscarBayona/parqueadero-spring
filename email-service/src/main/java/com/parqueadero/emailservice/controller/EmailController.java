package com.parqueadero.emailservice.controller;

import com.parqueadero.emailservice.dto.EmailRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @PostMapping
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailRequestDto request) {

        logger.info("Solicitud de correo recibida: {}", request);

        return ResponseEntity.ok(Map.of("mensaje", "Correo Enviado"));
    }
}