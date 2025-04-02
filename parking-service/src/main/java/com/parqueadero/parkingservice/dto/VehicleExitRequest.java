package com.parqueadero.parkingservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehicleExitRequest {
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "License plate must be 6 alphanumeric characters")
    private String licensePlate;

    @NotNull(message = "Parking ID is required")
    private Long parkingId;
}