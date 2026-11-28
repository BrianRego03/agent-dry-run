package com.example.agentdryrun.service;

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
import com.example.agentdryrun.entity.ViolationType;
import com.example.agentdryrun.mappers.ParkingReportMapper;
import com.example.agentdryrun.repository.ParkingReportRepository;
import com.example.agentdryrun.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingReportServiceTest {

    @Mock
    private ParkingReportRepository parkingReportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ParkingReportMapper parkingReportMapper;

    @InjectMocks
    private ParkingReportService parkingReportService;

    @Test
    void registerReportWithExistingVehicle_savesReportAndReturnsDto() {
        User reporter = new User("Aarav Sharma", "aarav@example.com", "secret");
        Instant reportedAt = Instant.parse("2026-11-28T10:15:30Z");
        Vehicle vehicle = new Vehicle("MH12AB1234", "Tata", "Nexon", "White", reportedAt);

        CreateParkingReportRequest request = new CreateParkingReportRequest();
        request.setVehicleId(10L);
        request.setLatitude(18.5204);
        request.setLongitude(73.8567);
        request.setAddress("MG Road, Pune");
        request.setViolationType(ViolationType.PARKING_ON_FOOTPATH);
        request.setDescription("Blocking footpath");
        request.setReportedAt(reportedAt);

        ParkingReport mappedEntity = new ParkingReport();
        mappedEntity.setLatitude(18.5204);
        mappedEntity.setLongitude(73.8567);
        mappedEntity.setViolationType(ViolationType.PARKING_ON_FOOTPATH);
        mappedEntity.setReportedAt(reportedAt);

        ParkingReportDto expectedDto = new ParkingReportDto();
        expectedDto.setReportId(100L);
        expectedDto.setViolationType(ViolationType.PARKING_ON_FOOTPATH);

        when(userRepository.findById(1)).thenReturn(Optional.of(reporter));
        when(vehicleService.getById(10L)).thenReturn(vehicle);
        when(parkingReportMapper.toEntity(request)).thenReturn(mappedEntity);
        when(parkingReportRepository.save(any(ParkingReport.class))).thenAnswer(inv -> inv.getArgument(0));
        when(parkingReportMapper.toDto(any(ParkingReport.class))).thenReturn(expectedDto);

        ParkingReportDto result = parkingReportService.registerReportWithExistingVehicle(1, request);

        ArgumentCaptor<ParkingReport> reportCaptor = ArgumentCaptor.forClass(ParkingReport.class);
        verify(parkingReportRepository).save(reportCaptor.capture());

        ParkingReport saved = reportCaptor.getValue();
        assertThat(saved.getReporter()).isSameAs(reporter);
        assertThat(saved.getVehicle()).isSameAs(vehicle);
        assertThat(saved.getSubmittedAt()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(ReportStatus.PENDING_REVIEW);
        assertThat(saved.getVerificationStatus()).isEqualTo(VerificationStatus.PENDING);
        assertThat(saved.getClearanceStatus()).isEqualTo(ClearanceStatus.NOT_CLEARED);
        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    void registerReportWithExistingVehicle_whenReporterNotFound_throwsEntityNotFound() {
        CreateParkingReportRequest request = new CreateParkingReportRequest();
        request.setVehicleId(10L);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                parkingReportService.registerReportWithExistingVehicle(99, request));

        verify(vehicleService, never()).getById(any());
        verify(parkingReportRepository, never()).save(any());
    }

    @Test
    void registerReportWithNewVehicle_registersVehicleUsingReportedAtWhenOccurredAtOmitted() {
        User reporter = new User("Priya Nair", "priya@example.com", "secret");
        Instant reportedAt = Instant.parse("2026-11-28T11:30:00Z");

        RegisterVehicleRequest vehicleReq = new RegisterVehicleRequest();
        vehicleReq.setRegistrationNumber("KA01MJ5678");
        vehicleReq.setMake("Maruti Suzuki");
        vehicleReq.setModel("Swift");
        vehicleReq.setColour("Red");

        CreateParkingReportWithVehicleRequest request = new CreateParkingReportWithVehicleRequest();
        request.setVehicle(vehicleReq);
        request.setLatitude(12.9716);
        request.setLongitude(77.5946);
        request.setViolationType(ViolationType.NO_PARKING_ZONE);
        request.setReportedAt(reportedAt);

        Vehicle registeredVehicle = new Vehicle("KA01MJ5678", "Maruti Suzuki", "Swift", "Red", reportedAt);
        ParkingReport mappedEntity = new ParkingReport();
        ParkingReportDto expectedDto = new ParkingReportDto();
        expectedDto.setReportId(101L);

        when(userRepository.findById(2)).thenReturn(Optional.of(reporter));
        when(vehicleService.registerVehicle("KA01MJ5678", "Maruti Suzuki", "Swift", "Red", "2026-11-28T11:30:00Z"))
                .thenReturn(registeredVehicle);
        when(parkingReportMapper.toEntity(request)).thenReturn(mappedEntity);
        when(parkingReportRepository.save(any(ParkingReport.class))).thenAnswer(inv -> inv.getArgument(0));
        when(parkingReportMapper.toDto(any(ParkingReport.class))).thenReturn(expectedDto);

        ParkingReportDto result = parkingReportService.registerReportWithNewVehicle(2, request);

        verify(vehicleService).registerVehicle("KA01MJ5678", "Maruti Suzuki", "Swift", "Red", "2026-11-28T11:30:00Z");
        verify(parkingReportRepository).save(mappedEntity);
        assertThat(mappedEntity.getVehicle()).isSameAs(registeredVehicle);
        assertThat(mappedEntity.getReporter()).isSameAs(reporter);
        assertThat(result).isSameAs(expectedDto);
    }

    @Test
    void registerReportWithNewVehicle_whenOccurredAtProvided_usesProvidedOccurredAt() {
        User reporter = new User("Rohan Verma", "rohan@example.com", "secret");
        Instant reportedAt = Instant.parse("2026-11-28T12:00:00Z");
        String explicitOccurredAt = "2026-11-28T11:45:00Z";

        RegisterVehicleRequest vehicleReq = new RegisterVehicleRequest();
        vehicleReq.setRegistrationNumber("DL3CAB4321");
        vehicleReq.setMake("Mahindra");
        vehicleReq.setModel("XUV700");
        vehicleReq.setColour("Black");
        vehicleReq.setOccurredAt(explicitOccurredAt);

        CreateParkingReportWithVehicleRequest request = new CreateParkingReportWithVehicleRequest();
        request.setVehicle(vehicleReq);
        request.setLatitude(28.6139);
        request.setLongitude(77.2090);
        request.setViolationType(ViolationType.DOUBLE_PARKING);
        request.setReportedAt(reportedAt);

        Vehicle registeredVehicle = new Vehicle("DL3CAB4321", "Mahindra", "XUV700", "Black", Instant.parse(explicitOccurredAt));
        ParkingReport mappedEntity = new ParkingReport();
        ParkingReportDto expectedDto = new ParkingReportDto();

        when(userRepository.findById(3)).thenReturn(Optional.of(reporter));
        when(vehicleService.registerVehicle("DL3CAB4321", "Mahindra", "XUV700", "Black", explicitOccurredAt))
                .thenReturn(registeredVehicle);
        when(parkingReportMapper.toEntity(request)).thenReturn(mappedEntity);
        when(parkingReportRepository.save(any(ParkingReport.class))).thenAnswer(inv -> inv.getArgument(0));
        when(parkingReportMapper.toDto(any(ParkingReport.class))).thenReturn(expectedDto);

        parkingReportService.registerReportWithNewVehicle(3, request);

        verify(vehicleService).registerVehicle("DL3CAB4321", "Mahindra", "XUV700", "Black", explicitOccurredAt);
    }

    @Test
    void getById_whenExists_returnsDto() {
        ParkingReport report = new ParkingReport();
        ParkingReportDto dto = new ParkingReportDto();
        dto.setReportId(10L);

        when(parkingReportRepository.findById(10L)).thenReturn(Optional.of(report));
        when(parkingReportMapper.toDto(report)).thenReturn(dto);

        ParkingReportDto result = parkingReportService.getById(10L);

        assertThat(result).isSameAs(dto);
        verify(parkingReportRepository).findById(10L);
    }

    @Test
    void getById_whenNotFound_throwsEntityNotFound() {
        when(parkingReportRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> parkingReportService.getById(99L));
    }

    @Test
    void getReportsByReporter_returnsMappedDtos() {
        ParkingReport report = new ParkingReport();
        ParkingReportDto dto = new ParkingReportDto();
        dto.setReportId(12L);

        when(parkingReportRepository.findByReporterId(5)).thenReturn(List.of(report));
        when(parkingReportMapper.toDto(report)).thenReturn(dto);

        List<ParkingReportDto> results = parkingReportService.getReportsByReporter(5);

        assertThat(results).containsExactly(dto);
        verify(parkingReportRepository).findByReporterId(5);
    }
}
