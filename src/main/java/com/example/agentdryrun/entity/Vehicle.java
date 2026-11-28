package com.example.agentdryrun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "registration_number", nullable = false, unique = true, length = 32)
    private String registrationNumber;

    @Column(name = "make", length = 100)
    private String make;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "colour", length = 50)
    private String colour;

    @Column(name = "first_seen_at", updatable = false, nullable = false)
    private Instant firstSeenAt;

    @Column(name = "offence_count", nullable = false)
    private int offenceCount = 0;

    public Vehicle() {
    }

    public Vehicle(String registrationNumber, String make, String model, String colour) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.firstSeenAt = Instant.now();
        this.offenceCount = 0;
    }

    public Long getVehicleId() {
        return this.vehicleId;
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

    public int getOffenceCount() {
        return this.offenceCount;
    }

    public void setOffenceCount(int offenceCount) {
        this.offenceCount = offenceCount;
    }
}
