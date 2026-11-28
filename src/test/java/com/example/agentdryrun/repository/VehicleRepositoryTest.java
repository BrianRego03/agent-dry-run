package com.example.agentdryrun.repository;

import com.example.agentdryrun.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void shouldSaveAndFindVehicleById() {
        Instant firstSeenAt = Instant.parse("2026-11-28T10:15:30Z");
        Vehicle vehicle = new Vehicle("MH12AB1234", "Tata", "Nexon", "White", firstSeenAt);

        Vehicle saved = vehicleRepository.saveAndFlush(vehicle);

        assertThat(saved.getVehicleId()).isNotNull();

        Optional<Vehicle> found = vehicleRepository.findById(saved.getVehicleId());
        assertThat(found).isPresent();
        assertThat(found.get().getRegistrationNumber()).isEqualTo("MH12AB1234");
        assertThat(found.get().getMake()).isEqualTo("Tata");
        assertThat(found.get().getModel()).isEqualTo("Nexon");
        assertThat(found.get().getColour()).isEqualTo("White");
        assertThat(found.get().getFirstSeenAt()).isEqualTo(firstSeenAt);
        assertThat(found.get().getOffenceCount()).isZero();
    }

    @Test
    void shouldSaveAndFindVehicleByRegistrationNumber() {
        Instant firstSeenAt = Instant.parse("2026-11-28T11:30:00Z");
        Vehicle swift = new Vehicle("KA01MJ5678", "Maruti Suzuki", "Swift", "Red", firstSeenAt);
        Vehicle xuv = new Vehicle("DL3CAB4321", "Mahindra", "XUV700", "Midnight Black", firstSeenAt);

        vehicleRepository.saveAndFlush(swift);
        vehicleRepository.saveAndFlush(xuv);

        Optional<Vehicle> found = vehicleRepository.findByRegistrationNumber("KA01MJ5678");
        assertThat(found).isPresent();
        assertThat(found.get().getMake()).isEqualTo("Maruti Suzuki");
        assertThat(found.get().getModel()).isEqualTo("Swift");
        assertThat(found.get().getColour()).isEqualTo("Red");

        assertThat(vehicleRepository.existsByRegistrationNumber("DL3CAB4321")).isTrue();
        assertThat(vehicleRepository.existsByRegistrationNumber("TN09CD9999")).isFalse();
        assertThat(vehicleRepository.findByRegistrationNumber("TN09CD9999")).isEmpty();
    }
}
