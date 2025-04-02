package com.parqueadero.parkingservice.service;

import com.parqueadero.parkingservice.dto.EmailRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailServiceConsumer {

    @Value("${email.service.url}")
    private String emailServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> sendEmail(EmailRequestDto emailRequestDto) {

        ResponseEntity<Map> response = restTemplate.postForEntity(emailServiceUrl, emailRequestDto, Map.class);
        return response.getBody();
    }
}