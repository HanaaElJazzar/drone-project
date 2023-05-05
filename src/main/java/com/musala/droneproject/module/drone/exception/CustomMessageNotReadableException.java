package com.musala.droneproject.module.drone.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.musala.droneproject.module.common.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

/*
    Created by: Hanaa ElJazzar
    Created on: 04/05/2023
    This will handle HttpMessageNotReadableException Error for wrong
 */

@ControllerAdvice
public class CustomMessageNotReadableException {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<BasicResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        String message = ex.getMessage();
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        BasicResponse apiResponse = new BasicResponse();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setMessage(message);
        apiResponse.setSuccess(false);

        if (mostSpecificCause instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) mostSpecificCause;
            String fieldName = cause.getPath().get(0).getFieldName();
            Object invalidValue = cause.getValue();

            if (fieldName.equals("state")) {
                String validValues = Arrays.toString(DroneState.values());
                message = "Invalid drone state. Valid states are: " + validValues;
            } else if (fieldName.equals("model")) {
                String validValues = Arrays.toString(DroneModel.values());
                message = "Invalid drone model. Valid models are: " + validValues;
            } else if (fieldName.equals(("logType"))){
                // Added by: Hanaa on 05/05/2023
                // Checking if LogType we are watching the events on is checked in requests too.
                // Added to monitor drone performances with specific log type
                String validValues = Arrays.toString(DroneState.values());
                message = "Invalid Log Type. Valid log types are: " + validValues;
            }

            apiResponse.setMessage(message);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
