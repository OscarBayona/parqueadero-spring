package com.parqueadero.parkingservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkingRequest {
    private String name;
    private Long ownerId;
    private Integer capacity;
    private BigDecimal hourlyRate;
    private String address;
}
