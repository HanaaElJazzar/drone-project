package com.musala.droneproject.module.dummydata;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.service.DroneService;
import com.musala.droneproject.module.medication.entity.Medication;
import com.musala.droneproject.module.medication.service.MedicationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/*
    Added By: Hanaa ElJazzar
    Date: 07/05/2023
    DataInitializer class is added to generate some Dummy Data
 */
@Component
public class DataInitializer {

    @Autowired
    private DroneService droneService;

    @Autowired
    private MedicationService medicationService;

    @PostConstruct
    public void initializeData() {
        populateData();
    }

    private void populateData() {

        //Add Some Dummy Medications
        Medication med1 = new Medication("Medication03", 100.0, "MED03", null);
        med1 = medicationService.saveMedication(med1);
        Medication med2 = new Medication("Medication04", 50.0, "MED04", null);
        med2 = medicationService.saveMedication(med2);
        Medication med3 = new Medication("Medication05", 150.0, "MED05", null);
        medicationService.saveMedication(med3);
        Medication med4 = new Medication("Medication06", 200.0, "MED06", null);
        medicationService.saveMedication(med4);
        Medication med5 = new Medication("Medication07", 40.0, "MED07", null);
        medicationService.saveMedication(med5);
        Medication med6 = new Medication("Medication08", 177.0, "MED08", null);
        medicationService.saveMedication(med6);

        //Add Some Dummy Data
        Drone drone = new Drone("DR1234567D", DroneModel.Lightweight, 200.0, 76.0, DroneState.LOADED);
        drone = droneService.saveNewDrone(drone);
        drone.setMedications(new ArrayList<Medication>());
        drone.getMedications().add(med1);
        drone.getMedications().add(med2);
        droneService.updateDrone(drone);

		drone = new Drone("DR1234567E", DroneModel.Middleweight, 300.0, 13.0, DroneState.IDLE);
        droneService.saveNewDrone(drone);
        drone = new Drone( "DR1234567F", DroneModel.Cruiserweight, 450.0, 90.8, DroneState.DELIVERING);
        drone = droneService.saveNewDrone(drone);
        drone.setMedications(new ArrayList<Medication>());
        drone.getMedications().add(med3);
        drone.getMedications().add(med4);
        droneService.updateDrone(drone);

        drone = new Drone("DR1234567G", DroneModel.Lightweight, 150.0, 76.0, DroneState.IDLE);
        droneService.saveNewDrone(drone);
        drone = new Drone("DR1234567H", DroneModel.Middleweight, 350.0, 20.0, DroneState.IDLE);
        droneService.saveNewDrone(drone);

    }

}