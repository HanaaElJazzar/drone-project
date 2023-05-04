package com.musala.droneproject.module.drone.controller;

import com.musala.droneproject.module.drone.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.exception.DuplicateSerialNumberException;
import com.musala.droneproject.module.drone.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    /*
        Register Drone Api
     */
    @PostMapping(value = "/registerDrone")
    public ResponseEntity<BasicResponse<Drone>> registerDrone(@Valid @RequestBody Drone drone, BindingResult result) {
        BasicResponse<Drone> response = new BasicResponse<Drone>();
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
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Drone registeredDrone = droneService.saveDrone(drone);
            response.setSuccess(true);
            response.setMessage("New Drone created successfully");
            response.setData(registeredDrone);
        }catch(DuplicateSerialNumberException e){
            //If Duplicate Serial Number Exception is thrown
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e){
            //If any other Exception happened while saving/creating the drone
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
        Get Available Drones for Loading
     */
    @GetMapping(path= "/getAvailableDrones", produces = "application/json")
    public ResponseEntity<BasicResponse<List<Drone>>> getAvailableDroneForLoading() {

        List<Drone> drones;
        BasicResponse<List<Drone>> response = new BasicResponse<List<Drone>>();
        response.setTimestamp(LocalDateTime.now());

        try {
            //List returned successfully
            drones = droneService.getAvailabeDrones();
            response.setSuccess(true);

            if(drones != null && !drones.isEmpty())
            {
                //If list returned drones
                response.setData(drones);
                response.setMessage("Available drones retrieved successfully.");
            }else{
                //If no drones available for loading
                response.setMessage("No Drones available to be loaded.");
                response.setSuccess(false);
            }
        }catch (Exception e){
            //If any exception happened at retrieval
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
