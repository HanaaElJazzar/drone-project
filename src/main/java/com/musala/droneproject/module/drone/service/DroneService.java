package com.musala.droneproject.module.drone.service;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneService {
    @Autowired
    private DroneRepository droneRepository;

    public Drone saveDrone(Drone drone) throws DuplicateSerialNumberException {

        Drone duplicateDrone = droneRepository.findDroneBySerialNumber(drone.getSerialNumber());
        if (duplicateDrone!= null && !duplicateDrone.getSerialNumber().isEmpty()) {
            throw new DuplicateSerialNumberException("Drone with serial number " + drone.getSerialNumber() + " already exists.");
        }
        return droneRepository.save(drone);
    }
}
