package com.example.agentdryrun.controller;

import com.example.agentdryrun.dtos.CreateParkingReportRequest;
import com.example.agentdryrun.dtos.CreateParkingReportWithVehicleRequest;
import com.example.agentdryrun.dtos.ParkingReportDto;
import com.example.agentdryrun.service.ParkingReportService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parking-reports")
public class ParkingReportController {
    private final ParkingReportService parkingReportService;

    public ParkingReportController(ParkingReportService parkingReportService) {
        this.parkingReportService = parkingReportService;
    }

    @PostMapping("/existing-vehicle")
    public ResponseEntity<ParkingReportDto> registerWithExistingVehicle(
            @Valid @RequestBody CreateParkingReportRequest request,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    ) {
        int reporterId = resolveReporterId(authentication);
        ParkingReportDto created = parkingReportService.registerReportWithExistingVehicle(reporterId, request);
        return createdResponse(created, uriBuilder);
    }

    @PostMapping("/new-vehicle")
    public ResponseEntity<ParkingReportDto> registerWithNewVehicle(
            @Valid @RequestBody CreateParkingReportWithVehicleRequest request,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    ) {
        int reporterId = resolveReporterId(authentication);
        ParkingReportDto created = parkingReportService.registerReportWithNewVehicle(reporterId, request);
        return createdResponse(created, uriBuilder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingReportDto> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingReportService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ParkingReportDto>> getMyReports(Authentication authentication) {
        int reporterId = resolveReporterId(authentication);
        return ResponseEntity.ok(parkingReportService.getReportsByReporter(reporterId));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    private ResponseEntity<ParkingReportDto> createdResponse(ParkingReportDto created, UriComponentsBuilder uriBuilder) {
        URI uri = uriBuilder.path("/parking-reports/{id}")
                .buildAndExpand(created.getReportId())
                .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    private int resolveReporterId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Integer) {
            return (Integer) authentication.getPrincipal();
        }
        throw new IllegalArgumentException("Authenticated user ID not available");
    }
}
