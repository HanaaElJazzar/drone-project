package com.musala.droneproject.module.drone.dto.response;

import com.musala.droneproject.module.common.dto.response.BasicResponse;
import com.musala.droneproject.module.medication.entity.Medication;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class DroneMedicationsResponse {
    private List<Medication> medications;
}
