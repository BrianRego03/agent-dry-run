package com.example.agentdryrun.repository;

import com.example.agentdryrun.entity.ClearanceStatus;
import com.example.agentdryrun.entity.ParkingReport;
import com.example.agentdryrun.entity.ReportStatus;
import com.example.agentdryrun.entity.User;
import com.example.agentdryrun.entity.Vehicle;
import com.example.agentdryrun.entity.VerificationStatus;
import com.example.agentdryrun.entity.ViolationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParkingReportRepositoryTest {

    @Autowired
    private ParkingReportRepository parkingReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void shouldSaveAndFindParkingReportById() {
        User reporter = userRepository.saveAndFlush(new User("Aarav Sharma", "aarav.sharma@example.com", "secret123"));
        Vehicle vehicle = vehicleRepository.saveAndFlush(
                new Vehicle("MH12AB1234", "Tata", "Nexon", "White", Instant.parse("2026-11-28T09:00:00Z"))
        );

        Instant reportedAt = Instant.parse("2026-11-28T10:15:30Z");
        ParkingReport report = new ParkingReport(
                reporter,
                vehicle,
                18.5204,
                73.8567,
                18.5209,
                73.8571,
                "MG Road, Camp, Pune",
                ViolationType.PARKING_ON_FOOTPATH,
                "Vehicle blocking pedestrian footpath near junction",
                reportedAt
        );

        ParkingReport saved = parkingReportRepository.saveAndFlush(report);

        assertThat(saved.getReportId()).isNotNull();

        Optional<ParkingReport> found = parkingReportRepository.findById(saved.getReportId());
        assertThat(found).isPresent();
        assertThat(found.get().getReporter().getId()).isEqualTo(reporter.getId());
        assertThat(found.get().getVehicle().getVehicleId()).isEqualTo(vehicle.getVehicleId());
        assertThat(found.get().getLatitude()).isEqualTo(18.5204);
        assertThat(found.get().getLongitude()).isEqualTo(73.8567);
        assertThat(found.get().getReporterLatitude()).isEqualTo(18.5209);
        assertThat(found.get().getReporterLongitude()).isEqualTo(73.8571);
        assertThat(found.get().getAddress()).isEqualTo("MG Road, Camp, Pune");
        assertThat(found.get().getViolationType()).isEqualTo(ViolationType.PARKING_ON_FOOTPATH);
        assertThat(found.get().getReportedAt()).isEqualTo(reportedAt);
        assertThat(found.get().getSubmittedAt()).isNotNull();
        assertThat(found.get().getStatus()).isEqualTo(ReportStatus.PENDING_REVIEW);
        assertThat(found.get().getVerificationStatus()).isEqualTo(VerificationStatus.PENDING);
        assertThat(found.get().getClearanceStatus()).isEqualTo(ClearanceStatus.NOT_CLEARED);
    }

    @Test
    void shouldSaveWithoutVehicleAndFindByReporterVehicleStatusesAndViolationType() {
        User reporter = userRepository.saveAndFlush(new User("Priya Nair", "priya.nair@example.com", "secret123"));
        Vehicle vehicle = vehicleRepository.saveAndFlush(
                new Vehicle("KA01MJ5678", "Maruti Suzuki", "Swift", "Red", Instant.parse("2026-11-28T08:30:00Z"))
        );

        ParkingReport unresolvedReport = new ParkingReport(
                reporter,
                null,
                12.9716,
                77.5946,
                12.9720,
                77.5950,
                "Brigade Road, Bengaluru",
                ViolationType.NO_PARKING_ZONE,
                "Parked in no-parking zone",
                Instant.parse("2026-11-28T11:00:00Z")
        );

        ParkingReport verifiedReport = new ParkingReport(
                reporter,
                vehicle,
                12.9750,
                77.6000,
                12.9755,
                77.6004,
                "Indiranagar 100 Feet Road, Bengaluru",
                ViolationType.BLOCKING_DRIVEWAY,
                "Blocking residential gate",
                Instant.parse("2026-11-28T12:00:00Z")
        );
        verifiedReport.setStatus(ReportStatus.OPEN);
        verifiedReport.setVerificationStatus(VerificationStatus.VERIFIED);
        verifiedReport.setClearanceStatus(ClearanceStatus.CLEARANCE_PENDING);

        parkingReportRepository.saveAndFlush(unresolvedReport);
        parkingReportRepository.saveAndFlush(verifiedReport);

        List<ParkingReport> byReporter = parkingReportRepository.findByReporterId(reporter.getId());
        assertThat(byReporter).hasSize(2);

        List<ParkingReport> byVehicle = parkingReportRepository.findByVehicleVehicleId(vehicle.getVehicleId());
        assertThat(byVehicle).hasSize(1);
        assertThat(byVehicle.get(0).getViolationType()).isEqualTo(ViolationType.BLOCKING_DRIVEWAY);

        assertThat(parkingReportRepository.findByStatus(ReportStatus.OPEN))
                .extracting(ParkingReport::getAddress)
                .contains("Indiranagar 100 Feet Road, Bengaluru");
        assertThat(parkingReportRepository.findByVerificationStatus(VerificationStatus.VERIFIED))
                .extracting(ParkingReport::getViolationType)
                .contains(ViolationType.BLOCKING_DRIVEWAY);
        assertThat(parkingReportRepository.findByClearanceStatus(ClearanceStatus.CLEARANCE_PENDING))
                .hasSize(1);
        assertThat(parkingReportRepository.findByViolationType(ViolationType.NO_PARKING_ZONE))
                .extracting(ParkingReport::getVehicle)
                .containsNull();
    }
}
