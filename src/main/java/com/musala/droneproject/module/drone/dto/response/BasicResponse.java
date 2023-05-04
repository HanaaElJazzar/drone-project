package com.musala.droneproject.module.drone.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class BasicResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public BasicResponse() {
    }

    public BasicResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
