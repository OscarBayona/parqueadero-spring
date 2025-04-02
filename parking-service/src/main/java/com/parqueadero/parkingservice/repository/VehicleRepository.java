package com.parqueadero.parkingservice.repository;

import com.parqueadero.parkingservice.entity.Vehicle;
import com.parqueadero.parkingservice.entity.Parking;
import com.parqueadero.parkingservice.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlateAndStatus(String licensePlate, VehicleStatus status);
    long countByParkingAndStatus(Parking parking, VehicleStatus status);

    // Buscar vehículo activo (parqueado) por placa y parking
    Optional<Vehicle> findByLicensePlateAndParkingIdAndStatus(String licensePlate, Long parkingId, VehicleStatus status);

    // Listar vehículos que se encuentran actualmente parqueados en un parking
    List<Vehicle> findByParkingIdAndStatus(Long parkingId, VehicleStatus status);

    @Query("SELECT v.licensePlate, COUNT(v) as entryCount " +
            "FROM Vehicle v " +
            "GROUP BY v.licensePlate " +
            "ORDER BY entryCount DESC " +
            "LIMIT 10")
    List<Object[]> findTop10VehiclesByTotalEntries();

    @Query("SELECT v.licensePlate, COUNT(v) as entryCount " +
            "FROM Vehicle v " +
            "WHERE v.parking.id = :parkingId " +
            "GROUP BY v.licensePlate " +
            "ORDER BY entryCount DESC " +
            "LIMIT 10")
    List<Object[]> findTop10VehiclesByParkingEntries(Long parkingId);
}
