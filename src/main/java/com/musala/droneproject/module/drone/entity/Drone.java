package com.musala.droneproject.module.drone.entity;

import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.medication.entity.Medication;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Index;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DRONE Database Table to use it for registering and managing drones records
 */
@Entity
@Table(name = "DRONE")
@Setter
@Getter
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100, message = "Serial number cannot exceed 100 characters")
    @Column(unique = true)
    @Index(name = "idx_serial_number")
    @NotNull(message = "Serial number cannot be null")
    private String serialNumber;

    @NotNull(message = "Drone Model cannot be null")
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Min(value=1, message = "Weight limit must be at least 1 grams")
    @Max(value=500, message = "Weight limit cannot exceed 500 grams")
    private Double weightLimit;

    @Min(value = 0, message = "Battery capacity must be at least 0%")
    @Max(value=100, message = "Battery capacity cannot exceed 100%")
    private Double batteryCapacity;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Drone State cannot be null")
    private DroneState state;

    //Added by Hanaa on 06/05/2023 to handle list of medications the drone can load and deliver at a time
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "drone_id", nullable = true)
    private List<Medication> medications = new ArrayList<>();

    public Drone() {
    }

    public Drone(Long id, String serialNumber, DroneModel model, Double weightLimit, Double batteryCapacity, DroneState state) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }

    public Drone(String serialNumber, DroneModel model, Double weightLimit, Double batteryCapacity, DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }
}


