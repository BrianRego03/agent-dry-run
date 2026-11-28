package com.example.agentdryrun.service;

import com.example.agentdryrun.entity.Vehicle;
import com.example.agentdryrun.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_callsRepositorySave() {
        String occurredAt = "2026-11-28T10:15:30Z";
        when(vehicleRepository.existsByRegistrationNumber("MH12AB1234")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle saved = vehicleService.registerVehicle("MH12AB1234", "Tata", "Nexon", "White", occurredAt);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());

        Vehicle persisted = captor.getValue();
        assertThat(persisted.getRegistrationNumber()).isEqualTo("MH12AB1234");
        assertThat(persisted.getMake()).isEqualTo("Tata");
        assertThat(persisted.getModel()).isEqualTo("Nexon");
        assertThat(persisted.getColour()).isEqualTo("White");
        assertThat(persisted.getFirstSeenAt()).isEqualTo(Instant.parse(occurredAt));
        assertThat(saved).isSameAs(persisted);
    }

    @Test
    void registrationWithSpaces_removesSpaces() {
        when(vehicleRepository.existsByRegistrationNumber("KA01MJ5678")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle saved = vehicleService.registerVehicle(" KA 01 MJ 5678 ", "Maruti Suzuki", "Swift", "Red", "2026-11-28T11:00:00Z");

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());
        assertThat(captor.getValue().getRegistrationNumber()).isEqualTo("KA01MJ5678");
        assertThat(saved.getRegistrationNumber()).isEqualTo("KA01MJ5678");
    }

    @Test
    void registrationWithLowercaseLetters_convertsToUppercase() {
        when(vehicleRepository.existsByRegistrationNumber("DL3CAB4321")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle saved = vehicleService.registerVehicle("dl3cab4321", "Mahindra", "XUV700", "Black", "2026-11-28T12:00:00Z");

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());
        assertThat(captor.getValue().getRegistrationNumber()).isEqualTo("DL3CAB4321");
        assertThat(saved.getRegistrationNumber()).isEqualTo("DL3CAB4321");
    }

    @Test
    void vehicleAlreadyExists_throwsException() {
        when(vehicleRepository.existsByRegistrationNumber("TN09CD9999")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                vehicleService.registerVehicle("TN09CD9999", "Hyundai", "Creta", "Silver", "2026-11-28T13:00:00Z"));

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getExistingVehicle_returnsVehicle() {
        Vehicle vehicle = new Vehicle("MH12AB1234", "Tata", "Nexon", "White", Instant.parse("2026-11-28T10:15:30Z"));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getById(1L);

        assertThat(result).isSameAs(vehicle);
        verify(vehicleRepository).findById(1L);
    }

    @Test
    void getNonexistentVehicle_throwsException() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> vehicleService.getById(99L));
        verify(vehicleRepository).findById(99L);
    }
}
