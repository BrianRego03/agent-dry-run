package com.example.agentdryrun.dtos;

import java.time.Instant;

public class VehicleDto {
    private Long vehicleId;
    private String registrationNumber;
    private String make;
    private String model;
    private String colour;
    private Instant firstSeenAt;
    private int offenceCount;

    public VehicleDto() {
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMake() {
        return this.make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Instant getFirstSeenAt() {
        return this.firstSeenAt;
    }

    public void setFirstSeenAt(Instant firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public int getOffenceCount() {
        return this.offenceCount;
    }

    public void setOffenceCount(int offenceCount) {
        this.offenceCount = offenceCount;
    }
}
