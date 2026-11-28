package com.example.agentdryrun.dtos;

import com.example.agentdryrun.entity.ClearanceStatus;
import com.example.agentdryrun.entity.ReportStatus;
import com.example.agentdryrun.entity.VerificationStatus;
import com.example.agentdryrun.entity.ViolationType;

import java.time.Instant;

public class ParkingReportDto {
    private Long reportId;
    private int reporterId;
    private VehicleDto vehicle;
    private Double latitude;
    private Double longitude;
    private Double reporterLatitude;
    private Double reporterLongitude;
    private String address;
    private ViolationType violationType;
    private String description;
    private Instant reportedAt;
    private Instant submittedAt;
    private ReportStatus status;
    private VerificationStatus verificationStatus;
    private ClearanceStatus clearanceStatus;

    public ParkingReportDto() {
    }

    public Long getReportId() {
        return this.reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public int getReporterId() {
        return this.reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public VehicleDto getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
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

    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public VerificationStatus getVerificationStatus() {
        return this.verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public ClearanceStatus getClearanceStatus() {
        return this.clearanceStatus;
    }

    public void setClearanceStatus(ClearanceStatus clearanceStatus) {
        this.clearanceStatus = clearanceStatus;
    }
}
