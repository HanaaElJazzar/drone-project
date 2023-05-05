package com.musala.droneproject.module.common.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter @Getter
public class BasicResponse<T> implements Serializable {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public BasicResponse() {
    }

    public BasicResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }
}
