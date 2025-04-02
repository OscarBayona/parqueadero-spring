package com.parqueadero.parkingservice.service;

import com.parqueadero.parkingservice.dto.ParkingRevenueDto;
import com.parqueadero.parkingservice.dto.TopOwnerDto;
import com.parqueadero.parkingservice.dto.TopParkingDto;
import com.parqueadero.parkingservice.dto.TopVehicleDto;
import com.parqueadero.parkingservice.exception.ParkingException;
import com.parqueadero.parkingservice.repository.VehicleHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IndicatorService {

    @Autowired
    private VehicleHistoryRepository vehicleHistoryRepository;

    // 1. Top 10 vehicles registrados en diferentes parqueaderos (global)
    public List<TopVehicleDto> getTopVehiclesAll() {
        return vehicleHistoryRepository.findTopVehiclesAll();
    }

    // 2. Top 10 vehículos registrados en un parqueadero específico
    public List<TopVehicleDto> getTopVehiclesByParking(Long parkingId) {
        return vehicleHistoryRepository.findTopVehiclesByParking(parkingId);
    }

    // 3. Vehículos que ingresaron por primera vez en un parqueadero
    public List<String> getFirstTimeVehicles(Long parkingId) {
        return vehicleHistoryRepository.findFirstTimeParkedVehicles(parkingId);
    }

    // 4. Obtener las ganancias de un parqueadero en diferentes periodos: today, week, month, year
    public BigDecimal getParkingRevenue(Long parkingId, String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        switch(period.toLowerCase()) {
            case "today":
                start = now.toLocalDate().atStartOfDay();
                break;
            case "week":
                // Asumimos que la semana inicia el lunes
                start = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
                break;
            case "month":
                start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
                break;
            case "year":
                start = now.withDayOfYear(1).toLocalDate().atStartOfDay();
                break;
            default:
                throw new ParkingException("Periodo no soportado. Use today, week, month o year.");
        }
        return vehicleHistoryRepository.calculateParkingRevenue(parkingId, start, now);
    }

    // 5. Top 3 socios con más ingresos de vehículos en la semana actual
    public List<TopOwnerDto> getTopOwnersCurrentWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        return vehicleHistoryRepository.findTopOwnersCurrentWeek(start, now);
    }

    // 6. Top 3 parqueaderos con mayor ganancia en la semana actual
    public List<TopParkingDto> getTopParkingsCurrentWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        return vehicleHistoryRepository.findTopParkingsByRevenue(start, now);
    }

    public ParkingRevenueDto getAllParkingRevenue(Long parkingId) {
        BigDecimal today = getParkingRevenue(parkingId, "today");
        BigDecimal week = getParkingRevenue(parkingId, "week");
        BigDecimal month = getParkingRevenue(parkingId, "month");
        BigDecimal year = getParkingRevenue(parkingId, "year");
        return new ParkingRevenueDto(today, week, month, year);
    }
}