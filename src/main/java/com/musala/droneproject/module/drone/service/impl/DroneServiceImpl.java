package com.musala.droneproject.module.drone.service.impl;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import com.musala.droneproject.module.drone.service.DroneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 04/05/2023
    Added DroneServiceImpl to add in it list of drone services need to be used
 */
@Service
public class DroneServiceImpl implements DroneService {
    @Autowired
    private DroneRepository droneRepository;

    /*
        Save/Create New Drone with duplicate validations
        Added By: Hanaa ElJazzar
        Date: 03/05/2023
     */
    public Drone saveNewDrone(Drone drone) throws DuplicateSerialNumberException {

        Drone duplicateDrone = droneRepository.findDroneBySerialNumber(drone.getSerialNumber());
        if (duplicateDrone != null && !duplicateDrone.getSerialNumber().isEmpty()) {
            throw new DuplicateSerialNumberException("Drone with serial number " + drone.getSerialNumber() + " already exists.");
        }
        return droneRepository.save(drone);
    }

    /*
        Get Available Drones (IDLE) that can be loaded.
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Override
    public List<Drone> getAvailableDrones() {
        List<Drone> drones = droneRepository.findByStateOrderByIdAsc(DroneState.IDLE);
        return drones;
    }

    /*
        Method to retrieve Battery Capacity of Specific Drone
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Override
    public Double getBatteryCapacity(String serialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(serialNumber);
        if (drone != null && drone.getBatteryCapacity() >= 0.0)
            return drone.getBatteryCapacity();
        return null;
    }

    @Override
    public List<Drone> getAllDrones() {
        List<Drone> drones = droneRepository.findAll();
        return drones;
    }

    /*
        Added By: Hanaa ElJazzar
        Date: 06/05/2023
        Added method to get the drone with status loaded and get its list of medications.
     */
    @Override
    public Drone getDrone(String serialNumber) {
        return droneRepository.findDroneBySerialNumber(serialNumber);
    }

    /*
        Added By: Hanaa ElJazzar
        Date: 06/05/2023
        Added method to update Drone information (State and Medications)
     */
    @Override
    @Transactional
    public Drone updateDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    /*
        Added By: Hanaa ElJazzar
        Date: 06/05/2023
        Added method get Number of Drones created in our System.
     */
    @Override
    public Long getNumberOfDronesFleet() {
        return droneRepository.countDrones();
    }


}
