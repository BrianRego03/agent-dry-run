package com.example.agentdryrun.service;

import com.example.agentdryrun.dtos.BaseParkingReportRequest;
import com.example.agentdryrun.dtos.CreateParkingReportRequest;
import com.example.agentdryrun.dtos.CreateParkingReportWithVehicleRequest;
import com.example.agentdryrun.dtos.ParkingReportDto;
import com.example.agentdryrun.dtos.RegisterVehicleRequest;
import com.example.agentdryrun.entity.ClearanceStatus;
import com.example.agentdryrun.entity.ParkingReport;
import com.example.agentdryrun.entity.ReportStatus;
import com.example.agentdryrun.entity.User;
import com.example.agentdryrun.entity.Vehicle;
import com.example.agentdryrun.entity.VerificationStatus;
import com.example.agentdryrun.mappers.ParkingReportMapper;
import com.example.agentdryrun.repository.ParkingReportRepository;
import com.example.agentdryrun.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ParkingReportService {
    private final ParkingReportRepository parkingReportRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;
    private final ParkingReportMapper parkingReportMapper;

    public ParkingReportService(
            ParkingReportRepository parkingReportRepository,
            UserRepository userRepository,
            VehicleService vehicleService,
            ParkingReportMapper parkingReportMapper
    ) {
        this.parkingReportRepository = parkingReportRepository;
        this.userRepository = userRepository;
        this.vehicleService = vehicleService;
        this.parkingReportMapper = parkingReportMapper;
    }

    @Transactional
    public ParkingReportDto registerReportWithExistingVehicle(int reporterId, CreateParkingReportRequest request) {
        User reporter = findReporterOrThrow(reporterId);
        Vehicle vehicle = vehicleService.getById(request.getVehicleId());
        return saveReport(reporter, vehicle, request);
    }

    @Transactional
    public ParkingReportDto registerReportWithNewVehicle(int reporterId, CreateParkingReportWithVehicleRequest request) {
        User reporter = findReporterOrThrow(reporterId);
        RegisterVehicleRequest vehicleRequest = request.getVehicle();
        String occurredAt = (vehicleRequest.getOccurredAt() != null && !vehicleRequest.getOccurredAt().isBlank())
                ? vehicleRequest.getOccurredAt()
                : request.getReportedAt().toString();

        Vehicle vehicle = vehicleService.registerVehicle(
                vehicleRequest.getRegistrationNumber(),
                vehicleRequest.getMake(),
                vehicleRequest.getModel(),
                vehicleRequest.getColour(),
                occurredAt
        );
        return saveReport(reporter, vehicle, request);
    }

    public ParkingReportDto getById(Long reportId) {
        ParkingReport report = parkingReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Parking report not found: " + reportId));
        return parkingReportMapper.toDto(report);
    }

    public List<ParkingReportDto> getReportsByReporter(int reporterId) {
        return parkingReportRepository.findByReporterId(reporterId).stream()
                .map(parkingReportMapper::toDto)
                .toList();
    }

    private ParkingReportDto saveReport(User reporter, Vehicle vehicle, BaseParkingReportRequest request) {
        ParkingReport report = parkingReportMapper.toEntity(request);
        report.setReporter(reporter);
        report.setVehicle(vehicle);
        report.setSubmittedAt(Instant.now());
        report.setStatus(ReportStatus.PENDING_REVIEW);
        report.setVerificationStatus(VerificationStatus.PENDING);
        report.setClearanceStatus(ClearanceStatus.NOT_CLEARED);
        ParkingReport saved = parkingReportRepository.save(report);
        return parkingReportMapper.toDto(saved);
    }

    private User findReporterOrThrow(int reporterId) {
        return userRepository.findById(reporterId)
                .orElseThrow(() -> new EntityNotFoundException("Reporter not found: " + reporterId));
    }
}
