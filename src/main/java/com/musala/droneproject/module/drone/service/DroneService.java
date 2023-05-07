package com.musala.droneproject.module.drone.service;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import org.springframework.stereotype.Component;

import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    Added DroneService interface to define in it list of drone services need to be used
 */
@Component
public interface DroneService {

    public Drone saveNewDrone(Drone drone) throws DuplicateSerialNumberException;

    public List<Drone> getAvailableDrones();

    public Double getBatteryCapacity(String serialNumber);

    public List<Drone> getAllDrones();

    public Drone getDrone(String serialNumber);

    public Drone updateDrone(Drone drone);

    public Long getNumberOfDronesFleet();

}
