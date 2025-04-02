package com.parqueadero.parkingservice.dto;

import jakarta.validation.constraints.*;
import com.parqueadero.parkingservice.entity.Parking;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ParkingDto {

    private Long id;

    @NotBlank(message = "Parking name is required")
    private String name;

    @NotNull(message = "Owner is required")
    private Long ownerId;
    private String ownerEmail;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @Positive(message = "Hourly rate must be positive")
    private BigDecimal hourlyRate;
    private String address;

    public ParkingDto(Parking parking) {
        this.id = parking.getId();
        this.name = parking.getName();
        this.capacity = parking.getCapacity();
        this.hourlyRate = parking.getHourlyRate();
        this.address = parking.getAddress();
        if (parking.getOwner() != null) {
            this.ownerId = parking.getOwner().getId();
            this.ownerEmail = parking.getOwner().getEmail();
        }
    }
}
