package com.musala.droneproject.module.medication.exception;

public class DuplicateMedicationCodeException extends RuntimeException{
    public DuplicateMedicationCodeException(String message){
        super(message);
    }
}
