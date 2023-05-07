package com.musala.droneproject.module.medication.service;

import com.musala.droneproject.module.medication.entity.Medication;
import org.springframework.stereotype.Service;

/*
    Created by: Hanaa ElJazzar
    Created on: 06/05/2023
    Added MedicationService interface that will list the Medications Service methods to be used.
 */

@Service
public interface MedicationService {
    public Medication saveMedication(Medication medication);

    public Medication getMedication(String code);

    public Boolean checkIfMedicationLoadedIntoDrone(String medication_code);
}
