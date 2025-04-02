package com.parqueadero.parkingservice.controller;

import com.parqueadero.parkingservice.dto.ParkingRevenueDto;
import com.parqueadero.parkingservice.dto.TopOwnerDto;
import com.parqueadero.parkingservice.dto.TopParkingDto;
import com.parqueadero.parkingservice.dto.TopVehicleDto;
import com.parqueadero.parkingservice.service.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/indicators")
public class IndicatorController {

    @Autowired
    private IndicatorService indicatorService;

    // 1. Top 10 vehículos en todos los parqueaderos
    @PreAuthorize("hasAnyRole('ADMIN','SOCIO')")
    @GetMapping("/top-vehicles-all")
    public ResponseEntity<List<TopVehicleDto>> getTopVehiclesAll() {
        return ResponseEntity.ok(indicatorService.getTopVehiclesAll());
    }

    // 2. Top 10 vehículos en un parqueadero
    @PreAuthorize("hasAnyRole('ADMIN','SOCIO')")
    @GetMapping("/top-vehicles/{parkingId}")
    public ResponseEntity<List<TopVehicleDto>> getTopVehiclesByParking(@PathVariable Long parkingId) {
        return ResponseEntity.ok(indicatorService.getTopVehiclesByParking(parkingId));
    }

    // 3. Vehículos por primera vez en un parqueadero
    @PreAuthorize("hasAnyRole('ADMIN','SOCIO')")
    @GetMapping("/first-time-vehicles/{parkingId}")
    public ResponseEntity<List<String>> getFirstTimeVehicles(@PathVariable Long parkingId) {
        return ResponseEntity.ok(indicatorService.getFirstTimeVehicles(parkingId));
    }

    // 4. Ganancias de un parqueadero según periodo (today, week, month, year)
    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/revenue/{parkingId}")
    public ResponseEntity<BigDecimal> getParkingRevenue(@PathVariable Long parkingId,
                                                        @RequestParam("period") String period) {
        return ResponseEntity.ok(indicatorService.getParkingRevenue(parkingId, period));
    }

    // Endpoint para que SOCIO consulte todas las ganancias de un parking
    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/revenues/{parkingId}")
    public ResponseEntity<ParkingRevenueDto> getAllParkingRevenue(@PathVariable Long parkingId) {
        ParkingRevenueDto revenue = indicatorService.getAllParkingRevenue(parkingId);
        return ResponseEntity.ok(revenue);
    }

    // 5. Top 3 socios con más ingresos en la semana actual (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-owners")
    public ResponseEntity<List<TopOwnerDto>> getTopOwnersCurrentWeek() {
        return ResponseEntity.ok(indicatorService.getTopOwnersCurrentWeek());
    }

    // 6. Top 3 parqueaderos con mayor ganancia en la semana actual (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-parkings")
    public ResponseEntity<List<TopParkingDto>> getTopParkingsCurrentWeek() {
        return ResponseEntity.ok(indicatorService.getTopParkingsCurrentWeek());
    }
}