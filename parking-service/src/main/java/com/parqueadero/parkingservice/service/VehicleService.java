package com.parqueadero.parkingservice.service;

import com.parqueadero.parkingservice.dto.VehicleDto;
import com.parqueadero.parkingservice.dto.VehicleEntryRequest;
import com.parqueadero.parkingservice.dto.VehicleExitRequest;
import com.parqueadero.parkingservice.entity.Parking;
import com.parqueadero.parkingservice.entity.Vehicle;
import com.parqueadero.parkingservice.entity.VehicleHistory;
import com.parqueadero.parkingservice.entity.VehicleStatus;
import com.parqueadero.parkingservice.exception.ParkingException;
import com.parqueadero.parkingservice.repository.ParkingRepository;
import com.parqueadero.parkingservice.repository.VehicleHistoryRepository;
import com.parqueadero.parkingservice.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private VehicleHistoryRepository vehicleHistoryRepository;

    // Registrar la entrada de un vehículo
    @Transactional
    public Long registerVehicleEntry(VehicleEntryRequest request) {
        // Validar que la placa tenga 6 caracteres alfanuméricos
        validateLicensePlate(request.getLicensePlate());
        // Verificar que no exista ya un vehículo con la misma placa y status INSIDE en algun parking
        vehicleRepository.findByLicensePlateAndStatus(request.getLicensePlate(), VehicleStatus.INSIDE)
                .ifPresent(v -> {
                    throw new ParkingException("No se puede registrar ingreso, ya existe la placa en este u otro parqueadero.");
                });

        Parking parking = parkingRepository.findById(request.getParkingId())
                .orElseThrow(() -> new ParkingException("Parking no encontrado"));

        validateParkingCapacity(parking);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setParking(parking);
        vehicle.setStatus(VehicleStatus.INSIDE);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return savedVehicle.getId();
    }

    // Registrar la salida de un vehículo
    @Transactional
    public void registerVehicleExit(VehicleExitRequest request) {
        // Buscar el vehículo activo en el parking
        Vehicle vehicle = vehicleRepository.findByLicensePlateAndParkingIdAndStatus(request.getLicensePlate(), request.getParkingId(), VehicleStatus.INSIDE)
                .orElseThrow(() -> new ParkingException("No se puede registrar salida, no existe la placa en el parking."));

        Long hours = calculateParkingHours(vehicle);
        BigDecimal cost = calculateParkingCost(vehicle);

        // Mueve el vehicle a history
        VehicleHistory history = new VehicleHistory();
        history.setLicensePlate(vehicle.getLicensePlate());
        history.setParking(vehicle.getParking());
        history.setEntryDateTime(vehicle.getEntryDateTime());
        history.setExitDateTime(LocalDateTime.now());
        history.setTotalCost(cost);
        history.setTotalHours(hours);

        vehicleHistoryRepository.save(history);

        // Eliminar de los vehicles activos
        vehicleRepository.delete(vehicle);
    }

    // Listar los vehículos actualmente parqueados en un parking específico
    public List<VehicleDto> getVehiclesByParking(Long parkingId) {
        List<Vehicle> vehicles = vehicleRepository.findByParkingIdAndStatus(parkingId, VehicleStatus.INSIDE);
        return vehicles.stream().map(VehicleDto::new).collect(Collectors.toList());
    }

    private void validateLicensePlate(String licensePlate) {
        if (!licensePlate.matches("^[a-zA-Z0-9]{6}$")) {
            throw new ParkingException("Invalid license plate format");
        }
    }

    private void validateParkingCapacity(Parking parking) {
        long currentVehiclesCount = vehicleRepository.countByParkingAndStatus(
                parking,
                VehicleStatus.INSIDE
        );

        if (currentVehiclesCount >= parking.getCapacity()) {
            throw new ParkingException("No se puede ingresar el vehículo, el parqueadero está en su máxima capacidad.");
        }
    }

    private long calculateParkingHours(Vehicle vehicle) {
        return java.time.Duration.between(
                vehicle.getEntryDateTime(),
                LocalDateTime.now()
        ).toHours();
    }

    private BigDecimal calculateParkingCost(Vehicle vehicle) {
        long hours = calculateParkingHours(vehicle);

        return vehicle.getParking().getHourlyRate().multiply(BigDecimal.valueOf(hours));
    }

    public VehicleDto getVehicleDetail(Long parkingId, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ParkingException("Vehículo no encontrado"));
        if (!vehicle.getParking().getId().equals(parkingId)) {
            throw new ParkingException("El vehículo no pertenece al parking especificado");
        }
        return new VehicleDto(vehicle);
    }
}
