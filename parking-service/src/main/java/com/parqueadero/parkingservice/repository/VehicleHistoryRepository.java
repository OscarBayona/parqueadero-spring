package com.parqueadero.parkingservice.repository;

import com.parqueadero.parkingservice.dto.TopOwnerDto;
import com.parqueadero.parkingservice.dto.TopParkingDto;
import com.parqueadero.parkingservice.dto.TopVehicleDto;
import com.parqueadero.parkingservice.entity.VehicleHistory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleHistoryRepository extends JpaRepository<VehicleHistory, Long> {

    @Query("SELECT SUM(vh.totalCost) " +
            "FROM VehicleHistory vh " +
            "WHERE vh.parking.id = :parkingId AND " +
            "vh.exitDateTime BETWEEN :startDate AND :endDate")
    BigDecimal calculateParkingRevenue(Long parkingId, LocalDateTime startDate, LocalDateTime endDate);

    // Top 10 vehículos en todos los parqueaderos
    @Query("SELECT new com.parqueadero.parkingservice.dto.TopVehicleDto(vh.licensePlate, COUNT(vh)) " +
            "FROM VehicleHistory vh " +
            "GROUP BY vh.licensePlate " +
            "ORDER BY COUNT(vh) DESC")
    List<TopVehicleDto> findTopVehiclesAll(Pageable pageable);

    default List<TopVehicleDto> findTopVehiclesAll() {
        return findTopVehiclesAll(PageRequest.of(0, 10));
    }

    // Top 10 vehículos en un parqueadero específico
    @Query("SELECT new com.parqueadero.parkingservice.dto.TopVehicleDto(vh.licensePlate, COUNT(vh)) " +
            "FROM VehicleHistory vh " +
            "WHERE vh.parking.id = :parkingId " +
            "GROUP BY vh.licensePlate " +
            "ORDER BY COUNT(vh) DESC")
    List<TopVehicleDto> findTopVehiclesByParking(@Param("parkingId") Long parkingId, Pageable pageable);

    default List<TopVehicleDto> findTopVehiclesByParking(Long parkingId) {
        return findTopVehiclesByParking(parkingId, PageRequest.of(0, 10));
    }

    // Top 3 socios con más ingresos en la semana actual
    @Query("SELECT new com.parqueadero.parkingservice.dto.TopOwnerDto(p.owner.email, COUNT(vh)) " +
            "FROM VehicleHistory vh JOIN vh.parking p " +
            "WHERE vh.entryDateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY p.owner.email " +
            "ORDER BY COUNT(vh) DESC")
    List<TopOwnerDto> findTopOwnersCurrentWeek(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate, Pageable pageable);

    default List<TopOwnerDto> findTopOwnersCurrentWeek(LocalDateTime startDate, LocalDateTime endDate) {
        return findTopOwnersCurrentWeek(startDate, endDate, PageRequest.of(0, 3));
    }

    // Top 3 parqueaderos con mayor ganancia en la semana actual
    @Query("SELECT new com.parqueadero.parkingservice.dto.TopParkingDto(p.id, SUM(vh.totalCost)) " +
            "FROM VehicleHistory vh JOIN vh.parking p " +
            "WHERE vh.exitDateTime BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id " +
            "ORDER BY SUM(vh.totalCost) DESC")
    List<TopParkingDto> findTopParkingsByRevenue(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate, Pageable pageable);

    default List<TopParkingDto> findTopParkingsByRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return findTopParkingsByRevenue(startDate, endDate, PageRequest.of(0, 3));
    }

    @Query("SELECT v.licensePlate " +
            "FROM Vehicle v " +
            "WHERE v.parking.id = :parkingId " +
            "AND v.status = 'INSIDE' " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM VehicleHistory vh " +
            "    WHERE vh.parking.id = :parkingId " +
            "      AND vh.licensePlate = v.licensePlate" +
            ")")
    List<String> findFirstTimeParkedVehicles(@Param("parkingId") Long parkingId);
}