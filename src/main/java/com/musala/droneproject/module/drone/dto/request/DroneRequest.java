package com.musala.droneproject.module.drone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class DroneRequest {
    private String serialNumber;
}
