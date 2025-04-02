package com.parqueadero.parkingservice.repository;

import com.parqueadero.parkingservice.entity.Parking;
import com.parqueadero.parkingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByOwnerId(Long ownerId);
    List<Parking> findByOwner(User owner);

    @Query("SELECT p FROM Parking p WHERE p.owner.id = :ownerId")
    List<Parking> findParkingsByOwnerId(Long ownerId);
}
