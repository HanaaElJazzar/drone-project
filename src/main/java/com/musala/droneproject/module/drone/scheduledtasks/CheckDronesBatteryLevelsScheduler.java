package com.musala.droneproject.module.drone.scheduledtasks;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.service.DroneService;
import com.musala.droneproject.module.dronemonitorevent.enums.DroneMonitorLogType;
import com.musala.droneproject.module.dronemonitorevent.service.DroneMonitorEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@EnableScheduling
@Component
public class CheckDronesBatteryLevelsScheduler {
    @Autowired
    private DroneMonitorEventService droneMonitorEventService;

    @Autowired
    private DroneService droneService;

    static final Logger log = LoggerFactory.getLogger(CheckDronesBatteryLevelsScheduler.class);

    @Scheduled(fixedRate = 60000) // run every minute
    public void checkDronesBatteryLevels() {
        List<Drone> drones = droneService.getAllDrones();
        for (Drone drone : drones) {
            // Removed by: Hanaa on 05/05/2023 due to the need to check on all drones battery life.
            // The drone Battery Level might change for the drone while it is delivering the product. Thus, the check should be done for all

            // if (drone.getState() != DroneState.IDLE && drone.getState() != DroneState.RETURNING) {
            //   // ignore drones that are not available for loading
            //   continue;
            // }

            Double batteryCapacity = drone.getBatteryCapacity();
            if (batteryCapacity < 25.0) {
                // log an event for low battery level on console and on table DRONE_MONITOR_EVENTS
                log.info("Low battery Level: " + batteryCapacity + "% for Drone with serialNumber: " + drone.getSerialNumber());
                droneMonitorEventService.logEvent(drone, "Low battery Level: " + batteryCapacity + "% for Drone with serialNumber: " + drone.getSerialNumber(), DroneMonitorLogType.BATTERY_MONITOR);
            }
        }
    }
}
