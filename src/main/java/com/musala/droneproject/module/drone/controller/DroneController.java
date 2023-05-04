package com.musala.droneproject.module.drone.controller;

import com.musala.droneproject.module.drone.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drones")
public class DroneController {

    @Autowired
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping(value = "/registerDrone")
    public ResponseEntity<BasicResponse<Drone>> registerDrone(@Valid @RequestBody Drone drone, BindingResult result) {
        BasicResponse<Drone> response = new BasicResponse<Drone>();
        if (result.hasErrors()) {
            response.setSuccess(false);
            String errorMessage = "Invalid drone register request:";
            for (FieldError fieldError : result.getFieldErrors()) {
                errorMessage += String.format(" %s (%s),", fieldError.getField(), fieldError.getDefaultMessage());
            }
            response.setMessage(errorMessage);
            // populate response with error details
            return ResponseEntity.badRequest().body(response);
        }

        Drone registeredDrone = droneService.saveDrone(drone);
        response.setSuccess(true);
        response.setMessage("New Drone created successfully");
        response.setData(registeredDrone);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
