package com.parqueadero.parkingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vehicle_history")
public class VehicleHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;

    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalCost;

    private Long totalHours;
}
