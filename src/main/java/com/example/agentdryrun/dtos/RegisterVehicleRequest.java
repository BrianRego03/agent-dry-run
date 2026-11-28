package com.example.agentdryrun.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterVehicleRequest {
    @NotBlank(message = "Registration number is required")
    @Size(max = 32, message = "Registration number must be at most 32 characters")
    private String registrationNumber;

    @Size(max = 100, message = "Make must be at most 100 characters")
    private String make;

    @Size(max = 100, message = "Model must be at most 100 characters")
    private String model;

    @Size(max = 50, message = "Colour must be at most 50 characters")
    private String colour;

    private String occurredAt;

    public RegisterVehicleRequest() {
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

    public String getOccurredAt() {
        return this.occurredAt;
    }

    public void setOccurredAt(String occurredAt) {
        this.occurredAt = occurredAt;
    }
}
