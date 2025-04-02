package com.parqueadero.parkingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6, nullable = false)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;

    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @PrePersist
    protected void onCreate() {
        entryDateTime = LocalDateTime.now();
    }
}
