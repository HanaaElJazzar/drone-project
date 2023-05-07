package com.musala.droneproject.module.drone.controller;

import com.musala.droneproject.module.common.config.DronePropertyValues;
import com.musala.droneproject.module.common.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.dto.request.DroneRequest;
import com.musala.droneproject.module.drone.dto.request.LoadDroneWithMedicationsRequest;
import com.musala.droneproject.module.drone.dto.response.AvailableDronesResponse;
import com.musala.droneproject.module.drone.dto.response.DroneBatteryResponse;
import com.musala.droneproject.module.drone.dto.response.DroneMedicationsResponse;
import com.musala.droneproject.module.drone.dto.response.DroneResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import com.musala.droneproject.module.drone.service.DroneService;
import com.musala.droneproject.module.medication.entity.Medication;
import com.musala.droneproject.module.medication.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    Added DroneController to create in it list of apis needed in the project
 */

@RestController
@RequestMapping("/api/v1/drones")
public class DroneController {

    @Autowired
    private final DroneService droneService;

    @Autowired
    private final MedicationService medicationService;

    @Autowired
    private DronePropertyValues dronePropertyValues;

    public DroneController(DroneService droneService, MedicationService medicationService, DronePropertyValues dronePropertyValues) {
        this.droneService = droneService;
        this.medicationService = medicationService;
        this.dronePropertyValues = dronePropertyValues;
    }

    /*
        Register Drone Api
        Added by: Hanaa ElJazzar
        Date: 03/05/2023
     */
    @PostMapping(value = "/registerDrone", produces = "application/json")
    public ResponseEntity<BasicResponse<DroneResponse>> registerDrone(@Valid @RequestBody Drone drone, BindingResult result) {
        DroneResponse droneResponse = new DroneResponse();
        BasicResponse<DroneResponse> response = new BasicResponse<DroneResponse>();
        response.setTimestamp(LocalDateTime.now());

        if (result.hasErrors()) {
            response.setSuccess(false);
            String errorMessage = "Invalid drone register request:";

            for (FieldError fieldError : result.getFieldErrors()) {
                errorMessage += String.format(" %s (%s),", fieldError.getField(), fieldError.getDefaultMessage());
            }

            errorMessage = errorMessage.substring(0, errorMessage.length() - 1); // remove the last comma
            response.setMessage(errorMessage);
            // populate response with error details
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            //Check if the number of Drones in the fleet is less than 10 to proceed
            Long countFleet = droneService.getNumberOfDronesFleet();
            if (countFleet < dronePropertyValues.getDroneFleet()) {
                Drone registeredDrone = droneService.saveNewDrone(drone);
                response.setSuccess(true);
                response.setMessage("New Drone created successfully");
                droneResponse.setDrone(registeredDrone);
                response.setData(droneResponse);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }else{
                //If WE have number of Drones equal to the parametrized number of drones in our fleet, through an error message
                response.setSuccess(false);
                response.setMessage("We have "+dronePropertyValues.getDroneFleet()+" drones in our fleet. You cannot add more.");
            }
        } catch (DuplicateSerialNumberException e) {
            //If Duplicate Serial Number Exception is thrown
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            //If any other Exception happened while saving/creating the drone
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
        Get Available Drones for Loading
        Added by: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @GetMapping(path = "/getAvailableDrones", produces = "application/json")
    public ResponseEntity<BasicResponse<AvailableDronesResponse>> getAvailableDroneForLoading() {

        AvailableDronesResponse availableDronesResponse = new AvailableDronesResponse();
        List<Drone> drones = new ArrayList<Drone>();
        BasicResponse<AvailableDronesResponse> response = new BasicResponse<AvailableDronesResponse>();
        response.setTimestamp(LocalDateTime.now());

        try {
            //List returned successfully
            drones = droneService.getAvailableDrones();
            response.setSuccess(true);

            if (drones != null && !drones.isEmpty()) {
                //If list returned drones
                availableDronesResponse.setAvailableDrones(drones);
                response.setData(availableDronesResponse);
                response.setMessage("Available drones retrieved successfully.");
            } else {
                //If no drones available for loading
                response.setMessage("No Drones available to be loaded.");
                response.setSuccess(false);
            }
        } catch (Exception e) {
            //If any exception happened at retrieval
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
        Api to get Battery Capacity for specific Drone
        Added by: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @GetMapping(path = "/getDroneBatteryCapacity", produces = "application/json")
    public ResponseEntity<DroneBatteryResponse> getDroneBatteryCapacity(@RequestBody DroneRequest droneRequest) {

        DroneBatteryResponse response = new DroneBatteryResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            //Check if serialNumber is sent in request
            if (droneRequest.getSerialNumber() != null && !droneRequest.getSerialNumber().isEmpty()) {
                Double batteryCapacity = droneService.getBatteryCapacity(droneRequest.getSerialNumber());

                //When BatteryCapacity found
                if (batteryCapacity != null) {
                    response.setSuccess(true);
                    response.setMessage("Battery Capacity Returned.");
                    response.setBatteryCapacity(batteryCapacity);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    //Else Drone not found
                    response.setMessage("Drone not found. No Battery Capacity available.");
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            } else {
                response.setSuccess(false);
                response.setMessage("Drone serialNumber should be provided.");
            }
        } catch (Exception e) {
            //If any exception happened at retrieval
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
        Added by: Hanaa ElJazzar
        Date: 06/05/2023
        getLoadedMedications returns the list of medications loaded into a drone.
     */
    @GetMapping(path = "/getLoadedMedications", produces = "application/json")
    public ResponseEntity<BasicResponse<DroneMedicationsResponse>> getLoadedMedications(@RequestBody DroneRequest droneRequest) {
        BasicResponse<DroneMedicationsResponse> basicResponse = new BasicResponse<DroneMedicationsResponse>();
        DroneMedicationsResponse response = new DroneMedicationsResponse();
        basicResponse.setTimestamp(LocalDateTime.now());

        try {
            //Check if serialNumber is sent in request
            if (droneRequest.getSerialNumber() != null && !droneRequest.getSerialNumber().isEmpty()) {
                Drone drone = droneService.getDrone(droneRequest.getSerialNumber());

                //Drones with states IDLE or RETURNING or DELIVERED have no medications attached to them
                if (drone != null) {
                    if (drone.getState().equals(DroneState.LOADED) || drone.getState().equals(DroneState.DELIVERING)) {
                        if (drone.getMedications() != null && drone.getMedications().size() > 0) {
                            response.setMedications(drone.getMedications());
                            basicResponse.setSuccess(true);
                            basicResponse.setMessage("Medications Loaded into drone " + drone.getSerialNumber() + " returned successfully.");
                            basicResponse.setData(response);
                            return new ResponseEntity<>(basicResponse, HttpStatus.FOUND);
                        } else {
                            //Drone with Correct statuses LOADED or DELIVERING wrong to have empty medications list
                            basicResponse.setMessage("ERROR: Drone is in loading or delivering status and has no medications. ");
                            basicResponse.setSuccess(false);
                            return new ResponseEntity<>(basicResponse, HttpStatus.NOT_FOUND);
                        }
                    } else {
                        basicResponse.setMessage("Drone loaded with medications has to be in statuses: " + DroneState.LOADED + " or " + DroneState.DELIVERING + " only.");
                    }
                } else {
                    basicResponse.setMessage("Drone with serialNumber " + droneRequest.getSerialNumber() + " doesn't exists.");
                    basicResponse.setSuccess(false);
                    return new ResponseEntity<>(basicResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                basicResponse.setMessage("ERROR: Drone serialNumber should be provided.");
            }
            basicResponse.setSuccess(false);
        } catch (Exception e) {
            //If any exception happened at retrieval
            basicResponse.setSuccess(false);
            basicResponse.setMessage(e.getMessage());
        }

        return new ResponseEntity<>(basicResponse, HttpStatus.BAD_REQUEST);
    }

    /*
        Added by: Hanaa
        Date: 06/05/2023
        loadDroneWithMedications api Load a specific drone with a list of medications and checks if those medications
        do not exist already, it saved the medications first or retrieves them from DB.
     */
    @PostMapping(path = "/loadDroneWithMedications", produces = "application/json")
    public ResponseEntity<BasicResponse<DroneResponse>> loadDroneWithMedications(@Valid @RequestBody LoadDroneWithMedicationsRequest loadDroneWithMedicationsRequest, BindingResult result) {

        BasicResponse<DroneResponse> response = new BasicResponse<DroneResponse>();
        DroneResponse loadDroneResponse = new DroneResponse();
        response.setTimestamp(LocalDateTime.now());
        List<Medication> loadedMedications = new ArrayList<Medication>();

        if (result.hasErrors()) {
            response.setSuccess(false);
            String errorMessage = "Invalid drone load drone with medications request:";

            for (FieldError fieldError : result.getFieldErrors()) {
                errorMessage += String.format(" %s (%s),", fieldError.getField(), fieldError.getDefaultMessage());
            }

            errorMessage = errorMessage.substring(0, errorMessage.length() - 1); // remove the last comma
            response.setMessage(errorMessage);
            // populate response with error details
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            //Check if serialNumber is sent in request
            if (loadDroneWithMedicationsRequest.getSerialNumber() != null && !loadDroneWithMedicationsRequest.getSerialNumber().isEmpty()) {
                //Check if Drone is available and Idle, we can start loading it in case the medications sent has the same weight, and available in DB.
                Drone drone = droneService.getDrone(loadDroneWithMedicationsRequest.getSerialNumber());
                if (drone != null && drone.getState().equals(DroneState.IDLE)) {
                    // Check drone Battery Capacity If it is greater than 25%
                    if (drone.getBatteryCapacity() > 25.0) {
                        // Calculate the total weight of the loaded medications

                        //Set drone in LOADING state to start the loading process and checks
                        drone.setState(DroneState.LOADING);
                        drone = droneService.updateDrone(drone);

                        Double totalWeight = 0.0;
                        // For each medication, if medication is not available in DB, add it.
                        for (Medication med : loadDroneWithMedicationsRequest.getMedications()) {

                            //Check if Medication is already loaded
                            Medication medex = medicationService.getMedication(med.getCode());
                            //If Medication not found in Database, then save it in the database in Medication table.
                            if (medex == null) {
                                medex = medicationService.saveMedication(med);
                            } else {
                                //If Medication found, we need to check if it is not already loaded in another drone
                                if (medicationService.checkIfMedicationLoadedIntoDrone(medex.getCode())) {
                                    response.setMessage("ERROR: Medication " + medex.getCode() + " already loaded into another drone.");
                                    response.setSuccess(false);
                                    drone.setState(DroneState.IDLE);
                                    drone = droneService.updateDrone(drone);
                                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                                }
                            }

                            loadedMedications.add(medex);
                            totalWeight += med.getWeight();
                        }

                        if (totalWeight <= drone.getWeightLimit()) {
                            //If weight limit is allowed
                            drone.setMedications(loadedMedications);
                            drone.setState(DroneState.LOADED);
                            //save the loaded medications and link them to the drone
                            droneService.updateDrone(drone);
                            loadDroneResponse.setDrone(drone);
                            response.setData(loadDroneResponse);
                            response.setSuccess(true);
                            response.setMessage("Medications Loaded successfully to drone with serialNumber: " + drone.getSerialNumber());
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else {
                            //Drone state modified back to IDLE because the LOADING process failed for the drone
                            drone.setState(DroneState.IDLE);
                            droneService.updateDrone(drone);
                            response.setMessage("ERROR: Total weight of medications to be loaded greater than the weight limit allowed for the drone.");
                        }
                    } else {
                        response.setMessage("ERROR: Available drone has Battery Level less than 25%. It cannot be loaded.");
                    }
                } else {
                    response.setMessage("ERROR: Available drone not found for serialNumber: " + loadDroneWithMedicationsRequest.getSerialNumber());
                    response.setSuccess(false);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            } else {
                response.setMessage("ERROR: Drone serialNumber should be provided.");
            }
            response.setSuccess(false);
        } catch (Exception e) {
            //If any exception happened at retrieval and for a drone found so far, set it back to IDLE state
            Drone drone = droneService.getDrone(loadDroneWithMedicationsRequest.getSerialNumber());
            if (drone != null) {
                drone.setState(DroneState.LOADING);
                droneService.updateDrone(drone);
            }

            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
