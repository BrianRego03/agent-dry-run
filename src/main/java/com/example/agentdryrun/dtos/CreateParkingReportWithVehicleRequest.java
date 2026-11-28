package com.example.agentdryrun.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateParkingReportWithVehicleRequest extends BaseParkingReportRequest {
    @NotNull(message = "Vehicle details are required")
    @Valid
    private RegisterVehicleRequest vehicle;

    public CreateParkingReportWithVehicleRequest() {
    }

    public RegisterVehicleRequest getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(RegisterVehicleRequest vehicle) {
        this.vehicle = vehicle;
    }
}
