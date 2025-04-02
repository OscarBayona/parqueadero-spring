package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.dto.VehicleDto;
import com.parqueadero.parkingservice.dto.VehicleEntryRequest;
import com.parqueadero.parkingservice.dto.VehicleExitRequest;
import com.parqueadero.parkingservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/entry")
    public ResponseEntity<Object> registerVehicleEntry(
            @Valid @RequestBody VehicleEntryRequest request,
            Authentication authentication
    ) {
        Long vehicleId = vehicleService.registerVehicleEntry(
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("id", vehicleId));
    }

    @PostMapping("/exit")
    public ResponseEntity<Object> registerVehicleExit(@Valid @RequestBody VehicleExitRequest request) {
        vehicleService.registerVehicleExit(request);
        return ResponseEntity.ok("{\"mensaje\": \"Salida registrada\"}");
    }
}
