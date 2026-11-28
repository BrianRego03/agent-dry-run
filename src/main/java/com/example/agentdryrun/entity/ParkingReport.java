package com.example.agentdryrun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "parking_report")
public class ParkingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "reporter_latitude")
    private Double reporterLatitude;

    @Column(name = "reporter_longitude")
    private Double reporterLongitude;

    @Column(name = "address", length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "violation_type", nullable = false, length = 100)
    private ViolationType violationType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ReportStatus status = ReportStatus.PENDING_REVIEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 32)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "clearance_status", nullable = false, length = 32)
    private ClearanceStatus clearanceStatus = ClearanceStatus.NOT_CLEARED;

    public ParkingReport() {
    }

    public ParkingReport(
            User reporter,
            Vehicle vehicle,
            Double latitude,
            Double longitude,
            Double reporterLatitude,
            Double reporterLongitude,
            String address,
            ViolationType violationType,
            String description,
            Instant reportedAt
    ) {
        this.reporter = reporter;
        this.vehicle = vehicle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reporterLatitude = reporterLatitude;
        this.reporterLongitude = reporterLongitude;
        this.address = address;
        this.violationType = violationType;
        this.description = description;
        this.reportedAt = reportedAt;
        this.submittedAt = Instant.now();
        this.status = ReportStatus.PENDING_REVIEW;
        this.verificationStatus = VerificationStatus.PENDING;
        this.clearanceStatus = ClearanceStatus.NOT_CLEARED;
    }

    public Long getReportId() {
        return this.reportId;
    }

    public User getReporter() {
        return this.reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
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
