package com.musala.droneproject.module.drone.service;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    Added DroneService to add in it list of drone services need to be used
 */
@Service
public class DroneService {
    @Autowired
    private DroneRepository droneRepository;

    /*
        Save/Create New Drone with duplicate validations
     */
    public Drone saveDrone(Drone drone) throws DuplicateSerialNumberException {

        Drone duplicateDrone = droneRepository.findDroneBySerialNumber(drone.getSerialNumber());
        if (duplicateDrone!= null && !duplicateDrone.getSerialNumber().isEmpty()) {
            throw new DuplicateSerialNumberException("Drone with serial number " + drone.getSerialNumber() + " already exists.");
        }
        return droneRepository.save(drone);
    }

    /*
        Get Available Drones (IDLE) that can be loaded.
     */
    public List<Drone> getAvailabeDrones() {
        List<Drone> drones = droneRepository.findByStateOrderByIdAsc(DroneState.IDLE);
        return drones;
    }
}
