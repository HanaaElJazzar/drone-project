package com.musala.droneproject.module.dronemonitorevent.service;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.dronemonitorevent.enums.DroneMonitorLogType;

/*
    Created by: Hanaa ElJazzar
    Created on: 05/05/2023
    Added DroneMonitorEventService interface to log drone performance and battery monitors in database table
 */
public interface DroneMonitorEventService {
    public Boolean logEvent(Drone drone, String message, DroneMonitorLogType logType);
}
