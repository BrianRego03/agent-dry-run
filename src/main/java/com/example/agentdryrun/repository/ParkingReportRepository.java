package com.example.agentdryrun.repository;

import com.example.agentdryrun.entity.ClearanceStatus;
import com.example.agentdryrun.entity.ParkingReport;
import com.example.agentdryrun.entity.ReportStatus;
import com.example.agentdryrun.entity.VerificationStatus;
import com.example.agentdryrun.entity.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingReportRepository extends JpaRepository<ParkingReport, Long> {
    List<ParkingReport> findByReporterId(int reporterId);

    List<ParkingReport> findByVehicleVehicleId(Long vehicleId);

    List<ParkingReport> findByStatus(ReportStatus status);

    List<ParkingReport> findByVerificationStatus(VerificationStatus verificationStatus);

    List<ParkingReport> findByClearanceStatus(ClearanceStatus clearanceStatus);

    List<ParkingReport> findByViolationType(ViolationType violationType);
}
