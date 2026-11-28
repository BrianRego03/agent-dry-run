package com.example.agentdryrun.dtos;

import com.example.agentdryrun.entity.ViolationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class BaseParkingReportRequest {
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private Double reporterLatitude;

    private Double reporterLongitude;

    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    @NotNull(message = "Violation type is required")
    private ViolationType violationType;

    private String description;

    @NotNull(message = "Reported timestamp is required")
    private Instant reportedAt;

    public BaseParkingReportRequest() {
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getReporterLatitude() {
        return this.reporterLatitude;
    }

    public void setReporterLatitude(Double reporterLatitude) {
        this.reporterLatitude = reporterLatitude;
    }

    public Double getReporterLongitude() {
        return this.reporterLongitude;
    }

    public void setReporterLongitude(Double reporterLongitude) {
        this.reporterLongitude = reporterLongitude;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ViolationType getViolationType() {
        return this.violationType;
    }

    public void setViolationType(ViolationType violationType) {
        this.violationType = violationType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getReportedAt() {
        return this.reportedAt;
    }

    public void setReportedAt(Instant reportedAt) {
        this.reportedAt = reportedAt;
    }
}
