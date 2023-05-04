package com.musala.droneproject.module.drone.exception;

/*
    Created by: Hanaa ElJazzar
    Created on: 04/05/2023
    This will handle Duplicate Serial Number Exception Error
 */
public class DuplicateSerialNumberException extends RuntimeException {

    public DuplicateSerialNumberException(String message) {
        super(message);
    }
}
