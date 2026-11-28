package com.example.agentdryrun.service;

import com.example.agentdryrun.entity.Vehicle;
import com.example.agentdryrun.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle registerVehicle(String registrationNumber, String make, String model, String colour) {
        String normalized = normalizeRegistrationNumber(registrationNumber);
        if (vehicleRepository.existsByRegistrationNumber(normalized)) {
            throw new IllegalArgumentException("Vehicle with registration number already exists: " + normalized);
        }
        Vehicle vehicle = new Vehicle(normalized, make, model, colour);
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> getById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    public Optional<Vehicle> getByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(normalizeRegistrationNumber(registrationNumber));
    }

    public List<Vehicle> getAllVehicles(String sort) {
        if (sort == null || sort.isBlank()) {
            return vehicleRepository.findAll();
        }
        return vehicleRepository.findAll(Sort.by(sort));
    }

    @Transactional
    public Vehicle updateVehicleDetails(Long vehicleId, String make, String model, String colour) {
        Vehicle vehicle = findVehicleOrThrow(vehicleId);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setColour(colour);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle incrementOffenceCount(Long vehicleId) {
        Vehicle vehicle = findVehicleOrThrow(vehicleId);
        vehicle.setOffenceCount(vehicle.getOffenceCount() + 1);
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
        }
        vehicleRepository.deleteById(vehicleId);
    }

    private Vehicle findVehicleOrThrow(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
    }

    private String normalizeRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number must not be blank");
        }
        return registrationNumber.trim().replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
    }
}
