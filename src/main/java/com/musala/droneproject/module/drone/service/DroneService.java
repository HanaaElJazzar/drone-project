package com.musala.droneproject.module.drone.service;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneService {
    @Autowired
    private DroneRepository droneRepository;

    public Drone saveDrone(Drone drone){
        return droneRepository.save(drone);
    }
}
