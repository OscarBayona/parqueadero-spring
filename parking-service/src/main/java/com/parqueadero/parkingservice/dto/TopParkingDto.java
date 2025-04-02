package com.parqueadero.parkingservice.dto;

import java.math.BigDecimal;

public class TopParkingDto {
    private Long parkingId;
    private BigDecimal revenue;

    public TopParkingDto(Long parkingId, BigDecimal revenue) {
        this.parkingId = parkingId;
        this.revenue = revenue;
    }

    public Long getParkingId() {
        return parkingId;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }
}