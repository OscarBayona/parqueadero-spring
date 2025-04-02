package com.parqueadero.parkingservice.dto;

public class TopOwnerDto {
    private String ownerEmail;
    private Long count;

    public TopOwnerDto(String ownerEmail, Long count) {
        this.ownerEmail = ownerEmail;
        this.count = count;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public Long getCount() {
        return count;
    }
}