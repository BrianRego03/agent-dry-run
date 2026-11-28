package com.example.agentdryrun.dtos;

import jakarta.validation.constraints.NotNull;

public class CreateParkingReportRequest extends BaseParkingReportRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    public CreateParkingReportRequest() {
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
