package com.musala.droneproject.module.drone.repository;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DroneRepository interface that extends JpaRepository<Drone, Long>
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {
    public List<Drone> findDronesByState(DroneState state);
    public Drone findDroneBySerialNumber(String serialNumber);
}
