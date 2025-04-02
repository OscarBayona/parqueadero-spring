package com.parqueadero.parkingservice.dto;

public class TopVehicleDto {
    private String licensePlate;
    private Long count;

    public TopVehicleDto(String licensePlate, Long count) {
        this.licensePlate = licensePlate;
        this.count = count;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Long getCount() {
        return count;
    }
}