package com.musala.droneproject.module.drone.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class DroneBatteryResponse extends BasicResponse{
    private Double batteryCapacity;
}
