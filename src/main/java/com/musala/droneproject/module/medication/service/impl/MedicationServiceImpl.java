package com.musala.droneproject.module.medication.service.impl;

import com.musala.droneproject.module.medication.entity.Medication;
import com.musala.droneproject.module.medication.exception.DuplicateMedicationCodeException;
import com.musala.droneproject.module.medication.repository.MedicationRepository;
import com.musala.droneproject.module.medication.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    Created by: Hanaa ElJazzar
    Created on: 06/05/2023
    Added MedicationServiceImpl that will implement list the Medications Service methods to be used.
 */

@Service
public class MedicationServiceImpl implements MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public Medication saveMedication(Medication medication) throws DuplicateMedicationCodeException {
        Medication duplicateMedication = medicationRepository.findMedicationByCode(medication.getCode());
        if(duplicateMedication!=null && !duplicateMedication.getCode().isEmpty()){
            throw new DuplicateMedicationCodeException("Medication with code "+medication.getCode()+" already exists.");
        }
        return medicationRepository.save(medication);
    }

    @Override
    public Medication getMedication(String code) {
        return medicationRepository.findMedicationByCode(code);
    }

    @Override
    public Boolean checkIfMedicationLoadedIntoDrone(String medication_code) {
        Long l =  medicationRepository.findDroneOfMedication(medication_code);
        if(l != null)
            return true;

        return false;
    }
}
