package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.dto.ParkingDto;
import com.parqueadero.parkingservice.dto.ParkingRequest;
import com.parqueadero.parkingservice.dto.VehicleDto;
import com.parqueadero.parkingservice.service.ParkingService;
import com.parqueadero.parkingservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parkings")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @Autowired
    private VehicleService vehicleService;

    // Crear un Parking (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ParkingDto> createParking(@RequestBody ParkingRequest request) {
        ParkingDto created = parkingService.createParking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar un Parking (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ParkingDto> updateParking(@PathVariable Long id,
                                                    @RequestBody ParkingRequest request) {
        ParkingDto updated = parkingService.updateParking(id, request);
        return ResponseEntity.ok(updated);
    }

    // Eliminar un Parking (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParking(@PathVariable Long id) {
        parkingService.deleteParking(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener Parking por id (ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingDto> getParking(@PathVariable Long id) {
        ParkingDto parkingDto = parkingService.getParkingById(id);
        return ResponseEntity.ok(parkingDto);
    }

    // Listar todos los Parkings (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ParkingDto>> getAllParkings() {
        List<ParkingDto> list = parkingService.getAllParkings();
        return ResponseEntity.ok(list);
    }

    // Listar los Parkings asociados al usuario autenticado (SOCIO o ADMIN)
    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @GetMapping("/mine")
    public ResponseEntity<List<ParkingDto>> getMyParkings(Authentication authentication) {
        String email = authentication.getName();
        List<ParkingDto> list = parkingService.getParkingsByOwnerEmail(email);
        return ResponseEntity.ok(list);
    }


    // Listado de vehículos en un parking específico (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{parkingId}/vehicles")
    public ResponseEntity<List<VehicleDto>> getVehiclesByParking(@PathVariable Long parkingId) {
        List<VehicleDto> vehicles = vehicleService.getVehiclesByParking(parkingId);
        return ResponseEntity.ok(vehicles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{parkingId}/vehicles/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicleDetail(@PathVariable Long parkingId, @PathVariable Long vehicleId) {
        VehicleDto vehicle = vehicleService.getVehicleDetail(parkingId, vehicleId);
        return ResponseEntity.ok(vehicle);
    }


    // Listar vehículos de un parking que le pertenece (SOCIO)
    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/{parkingId}/my-vehicles")
    public ResponseEntity<?> getVehiclesForOwner(@PathVariable Long parkingId, Authentication authentication) {
        String email = authentication.getName();
        if (!parkingService.isParkingOwnedBy(parkingId, email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"mensaje\": \"Acceso denegado: el parking no pertenece al usuario.\"}");
        }
        List<VehicleDto> vehicles = vehicleService.getVehiclesByParking(parkingId);
        return ResponseEntity.ok(vehicles);
    }

    // Detalle de un vehículo específico en el parking que le pertenezca (SOCIO)
    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/{parkingId}/my-vehicles/{vehicleId}")
    public ResponseEntity<?> getVehicleDetailForOwner(@PathVariable Long parkingId,
                                                      @PathVariable Long vehicleId,
                                                      Authentication authentication) {
        String email = authentication.getName();
        if (!parkingService.isParkingOwnedBy(parkingId, email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"mensaje\": \"Acceso denegado: el parking no pertenece al usuario.\"}");
        }
        VehicleDto vehicle = vehicleService.getVehicleDetail(parkingId, vehicleId);
        return ResponseEntity.ok(vehicle);
    }
}