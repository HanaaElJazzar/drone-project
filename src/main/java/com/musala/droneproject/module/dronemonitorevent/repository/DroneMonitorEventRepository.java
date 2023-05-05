package com.musala.droneproject.module.dronemonitorevent.repository;

import com.musala.droneproject.module.dronemonitorevent.entity.DroneMonitorEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneMonitorEventRepository extends JpaRepository<DroneMonitorEvent, Long> {

}
