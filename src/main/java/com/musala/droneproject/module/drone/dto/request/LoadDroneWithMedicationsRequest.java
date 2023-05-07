package com.musala.droneproject.module.drone.dto.request;

import com.musala.droneproject.module.medication.entity.Medication;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoadDroneWithMedicationsRequest {
    private String serialNumber;
    @Valid
    private List<Medication> medications;
}
