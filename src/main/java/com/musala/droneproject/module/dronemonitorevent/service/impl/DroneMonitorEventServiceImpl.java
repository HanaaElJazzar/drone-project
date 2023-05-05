package com.musala.droneproject.module.dronemonitorevent.service.impl;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.dronemonitorevent.entity.DroneMonitorEvent;
import com.musala.droneproject.module.dronemonitorevent.enums.DroneMonitorLogType;
import com.musala.droneproject.module.dronemonitorevent.repository.DroneMonitorEventRepository;
import com.musala.droneproject.module.dronemonitorevent.service.DroneMonitorEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    Created by: Hanaa ElJazzar
    Created on: 05/05/2023
    Added DroneMonitorEventServiceImpl interface to log drone performance and battery monitors in database table
 */
@Service
public class DroneMonitorEventServiceImpl implements DroneMonitorEventService {

    @Autowired
    private DroneMonitorEventRepository droneMonitorEventRepository;

    @Override
    public Boolean logEvent(Drone drone, String message, DroneMonitorLogType logType) {
        try{
            DroneMonitorEvent droneEvent = new DroneMonitorEvent(drone, message, logType);
            droneMonitorEventRepository.save(droneEvent);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
