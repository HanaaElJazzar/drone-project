package com.musala.droneproject;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = {"com.musala.droneproject"})
@EnableScheduling
@EnableJpaRepositories(basePackages = {"com.musala.droneproject"})
public class DroneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneProjectApplication.class, args);
    }

}
