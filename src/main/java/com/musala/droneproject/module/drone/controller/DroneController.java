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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
        response.setTimestamp(LocalDateTime.now());

        if (result.hasErrors()) {
            response.setSuccess(false);
            String errorMessage = "Invalid drone register request:";
//             result.getFieldErrors().stream()
//                    .map(FieldError::getDefaultMessage)
//                    .collect(Collectors.joining(", "));

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
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
