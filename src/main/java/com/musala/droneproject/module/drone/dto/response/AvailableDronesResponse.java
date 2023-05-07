package com.musala.droneproject.module.drone.dto.response;

import com.musala.droneproject.module.drone.entity.Drone;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class AvailableDronesResponse {
    private List<Drone> availableDrones;
}
