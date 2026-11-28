package com.example.agentdryrun.controller;

import com.example.agentdryrun.config.SecurityConfig;
import com.example.agentdryrun.dtos.CreateParkingReportRequest;
import com.example.agentdryrun.dtos.CreateParkingReportWithVehicleRequest;
import com.example.agentdryrun.dtos.ParkingReportDto;
import com.example.agentdryrun.dtos.VehicleDto;
import com.example.agentdryrun.entity.ClearanceStatus;
import com.example.agentdryrun.entity.ReportStatus;
import com.example.agentdryrun.entity.VerificationStatus;
import com.example.agentdryrun.entity.ViolationType;
import com.example.agentdryrun.service.JwtService;
import com.example.agentdryrun.service.ParkingReportService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingReportController.class)
@Import(SecurityConfig.class)
class ParkingReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParkingReportService parkingReportService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Authentication reporterAuth(int userId) {
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private ParkingReportDto sampleDto(Long reportId, int reporterId, String regNo) {
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setVehicleId(5L);
        vehicleDto.setRegistrationNumber(regNo);
        vehicleDto.setMake("Tata");
        vehicleDto.setModel("Nexon");
        vehicleDto.setColour("White");
        vehicleDto.setFirstSeenAt(Instant.parse("2026-11-28T10:15:30Z"));
        vehicleDto.setOffenceCount(0);

        ParkingReportDto dto = new ParkingReportDto();
        dto.setReportId(reportId);
        dto.setReporterId(reporterId);
        dto.setVehicle(vehicleDto);
        dto.setLatitude(18.5204);
        dto.setLongitude(73.8567);
        dto.setReporterLatitude(18.5209);
        dto.setReporterLongitude(73.8571);
        dto.setAddress("MG Road, Pune");
        dto.setViolationType(ViolationType.PARKING_ON_FOOTPATH);
        dto.setDescription("Parked on footpath");
        dto.setReportedAt(Instant.parse("2026-11-28T10:15:30Z"));
        dto.setSubmittedAt(Instant.parse("2026-11-28T10:16:00Z"));
        dto.setStatus(ReportStatus.PENDING_REVIEW);
        dto.setVerificationStatus(VerificationStatus.PENDING);
        dto.setClearanceStatus(ClearanceStatus.NOT_CLEARED);
        return dto;
    }

    @Test
    void registerWithExistingVehicle_whenValid_returnsCreated() throws Exception {
        ParkingReportDto created = sampleDto(10L, 1, "MH12AB1234");
        when(parkingReportService.registerReportWithExistingVehicle(eq(1), any(CreateParkingReportRequest.class)))
                .thenReturn(created);

        String body = """
                {
                  "vehicleId": 5,
                  "latitude": 18.5204,
                  "longitude": 73.8567,
                  "reporterLatitude": 18.5209,
                  "reporterLongitude": 73.8571,
                  "address": "MG Road, Pune",
                  "violationType": "PARKING_ON_FOOTPATH",
                  "description": "Parked on footpath",
                  "reportedAt": "2026-11-28T10:15:30Z"
                }
                """;

        mockMvc.perform(post("/parking-reports/existing-vehicle")
                        .with(authentication(reporterAuth(1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/parking-reports/10")))
                .andExpect(jsonPath("$.reportId").value(10))
                .andExpect(jsonPath("$.reporterId").value(1))
                .andExpect(jsonPath("$.vehicle.registrationNumber").value("MH12AB1234"))
                .andExpect(jsonPath("$.violationType").value("PARKING_ON_FOOTPATH"))
                .andExpect(jsonPath("$.status").value("PENDING_REVIEW"));

        verify(parkingReportService).registerReportWithExistingVehicle(eq(1), any(CreateParkingReportRequest.class));
    }

    @Test
    void registerWithNewVehicle_whenValid_returnsCreated() throws Exception {
        ParkingReportDto created = sampleDto(11L, 1, "KA01MJ5678");
        when(parkingReportService.registerReportWithNewVehicle(eq(1), any(CreateParkingReportWithVehicleRequest.class)))
                .thenReturn(created);

        String body = """
                {
                  "vehicle": {
                    "registrationNumber": "KA01MJ5678",
                    "make": "Maruti Suzuki",
                    "model": "Swift",
                    "colour": "Red"
                  },
                  "latitude": 12.9716,
                  "longitude": 77.5946,
                  "reporterLatitude": 12.9720,
                  "reporterLongitude": 77.5950,
                  "address": "Brigade Road, Bengaluru",
                  "violationType": "NO_PARKING_ZONE",
                  "description": "Parked in no-parking zone",
                  "reportedAt": "2026-11-28T11:00:00Z"
                }
                """;

        mockMvc.perform(post("/parking-reports/new-vehicle")
                        .with(authentication(reporterAuth(1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/parking-reports/11")))
                .andExpect(jsonPath("$.reportId").value(11))
                .andExpect(jsonPath("$.vehicle.registrationNumber").value("KA01MJ5678"));

        verify(parkingReportService).registerReportWithNewVehicle(eq(1), any(CreateParkingReportWithVehicleRequest.class));
    }

    @Test
    void registerWithExistingVehicle_whenMissingVehicleId_returnsBadRequest() throws Exception {
        String body = """
                {
                  "latitude": 18.5204,
                  "longitude": 73.8567,
                  "violationType": "PARKING_ON_FOOTPATH",
                  "reportedAt": "2026-11-28T10:15:30Z"
                }
                """;

        mockMvc.perform(post("/parking-reports/existing-vehicle")
                        .with(authentication(reporterAuth(1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerWithNewVehicle_whenServiceThrowsIllegalArgument_returnsBadRequest() throws Exception {
        when(parkingReportService.registerReportWithNewVehicle(eq(1), any(CreateParkingReportWithVehicleRequest.class)))
                .thenThrow(new IllegalArgumentException("Vehicle with registration number already exists: KA01MJ5678"));

        String body = """
                {
                  "vehicle": {
                    "registrationNumber": "KA01MJ5678",
                    "make": "Maruti Suzuki",
                    "model": "Swift",
                    "colour": "Red"
                  },
                  "latitude": 12.9716,
                  "longitude": 77.5946,
                  "violationType": "NO_PARKING_ZONE",
                  "reportedAt": "2026-11-28T11:00:00Z"
                }
                """;

        mockMvc.perform(post("/parking-reports/new-vehicle")
                        .with(authentication(reporterAuth(1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Vehicle with registration number already exists: KA01MJ5678"));
    }

    @Test
    void getReportById_whenFound_returnsOk() throws Exception {
        when(parkingReportService.getById(10L)).thenReturn(sampleDto(10L, 1, "MH12AB1234"));

        mockMvc.perform(get("/parking-reports/10")
                        .with(authentication(reporterAuth(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(10))
                .andExpect(jsonPath("$.vehicle.registrationNumber").value("MH12AB1234"))
                .andExpect(jsonPath("$.clearanceStatus").value("NOT_CLEARED"));

        verify(parkingReportService).getById(10L);
    }

    @Test
    void getReportById_whenNotFound_returnsNotFound() throws Exception {
        when(parkingReportService.getById(99L))
                .thenThrow(new EntityNotFoundException("Parking report not found: 99"));

        mockMvc.perform(get("/parking-reports/99")
                        .with(authentication(reporterAuth(1))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Parking report not found: 99"));
    }

    @Test
    void getMyReports_whenAuthenticated_returnsList() throws Exception {
        when(parkingReportService.getReportsByReporter(1))
                .thenReturn(List.of(sampleDto(10L, 1, "MH12AB1234")));

        mockMvc.perform(get("/parking-reports/my")
                        .with(authentication(reporterAuth(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reportId").value(10))
                .andExpect(jsonPath("$[0].reporterId").value(1));

        verify(parkingReportService).getReportsByReporter(1);
    }

    @Test
    void getReportById_whenUnauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/parking-reports/10"))
                .andExpect(status().isUnauthorized());
    }
}
