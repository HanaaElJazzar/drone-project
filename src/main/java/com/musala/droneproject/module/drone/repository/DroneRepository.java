package com.musala.droneproject.module.drone.repository;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DroneRepository interface that extends JpaRepository<Drone, Long>
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {
    public List<Drone> findByStateOrderByIdAsc(DroneState state);
    public Drone findDroneBySerialNumber(String serialNumber);

    /* Added by Hanaa on 06/05/2023 to get the number of created drones to monitor # of drones in our fleet */
    @Query("SELECT COUNT(d) FROM Drone d")
    long countDrones();

}
