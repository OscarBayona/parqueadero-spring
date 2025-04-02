package com.parqueadero.parkingservice.dto;

import com.parqueadero.parkingservice.entity.Vehicle;
import com.parqueadero.parkingservice.entity.VehicleStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDto {
    private Long id;
    private String licensePlate;
    private Long parkingId;
    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;
    private VehicleStatus status;

    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlate = vehicle.getLicensePlate();
        this.parkingId = vehicle.getParking().getId();
        this.entryDateTime = vehicle.getEntryDateTime();
        this.exitDateTime = vehicle.getExitDateTime();
        this.status = vehicle.getStatus();
    }

}
