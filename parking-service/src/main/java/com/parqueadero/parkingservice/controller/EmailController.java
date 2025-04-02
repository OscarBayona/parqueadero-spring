package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.dto.EmailRequestDto;
import com.parqueadero.parkingservice.service.EmailServiceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailServiceConsumer emailServiceConsumer;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailRequestDto emailRequestDto) {
        Map<String, String> response = emailServiceConsumer.sendEmail(emailRequestDto);
        return ResponseEntity.ok(response);
    }
}