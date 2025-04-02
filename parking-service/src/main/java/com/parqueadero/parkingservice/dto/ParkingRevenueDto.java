package com.parqueadero.parkingservice.dto;

import java.math.BigDecimal;

public class ParkingRevenueDto {
    private BigDecimal today;
    private BigDecimal week;
    private BigDecimal month;
    private BigDecimal year;

    public ParkingRevenueDto(BigDecimal today, BigDecimal week, BigDecimal month, BigDecimal year) {
        this.today = today;
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public BigDecimal getToday() {
        return today;
    }

    public BigDecimal getWeek() {
        return week;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public BigDecimal getYear() {
        return year;
    }
}
