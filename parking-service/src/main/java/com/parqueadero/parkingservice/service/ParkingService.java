package com.parqueadero.parkingservice.service;

import com.parqueadero.parkingservice.dto.ParkingDto;
import com.parqueadero.parkingservice.dto.ParkingRequest;
import com.parqueadero.parkingservice.entity.Parking;
import com.parqueadero.parkingservice.entity.User;
import com.parqueadero.parkingservice.exception.ParkingException;
import com.parqueadero.parkingservice.repository.ParkingRepository;
import com.parqueadero.parkingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ParkingDto createParking(ParkingRequest request) {
        Parking parking = new Parking();
        parking.setName(request.getName());
        parking.setCapacity(request.getCapacity());
        parking.setHourlyRate(request.getHourlyRate());
        parking.setAddress(request.getAddress());
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        parking.setOwner(owner);
        Parking saved = parkingRepository.save(parking);
        return new ParkingDto(saved);
    }

    // Actualizar un Parking a partir de ParkingRequest
    @Transactional
    public ParkingDto updateParking(Long id, ParkingRequest request) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        parking.setName(request.getName());
        parking.setCapacity(request.getCapacity());
        parking.setHourlyRate(request.getHourlyRate());
        parking.setAddress(request.getAddress());
        // Si se quiere actualizar el owner, se puede agregar lógica condicional
        Parking updated = parkingRepository.save(parking);
        return new ParkingDto(updated);
    }

    // Eliminar un Parking (se realiza sobre la entidad internamente)
    @Transactional
    public void deleteParking(Long id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        parkingRepository.delete(parking);
    }

    // Obtener Parking por id y mapear a DTO
    public ParkingDto getParkingById(Long id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        return new ParkingDto(parking);
    }

    // Listar todos los parkings (retornando DTOs)
    public List<ParkingDto> getAllParkings() {
        return parkingRepository.findAll().stream()
                .map(ParkingDto::new)
                .collect(Collectors.toList());
    }

    // Listar parkings asociados a un owner según su email
    public List<ParkingDto> getParkingsByOwnerEmail(String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        return parkingRepository.findByOwnerId(owner.getId()).stream()
                .map(ParkingDto::new)
                .collect(Collectors.toList());
    }

    public boolean isParkingOwnedBy(Long parkingId, String ownerEmail) {
        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new RuntimeException("Parking not found"));
        return parking.getOwner().getEmail().equalsIgnoreCase(ownerEmail);
    }
}
