package com.musala.droneproject.module.dronemonitorevent.entity;

import com.musala.droneproject.module.drone.entity.Drone;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.musala.droneproject.module.dronemonitorevent.enums.DroneMonitorLogType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/*
    Added by: Hanaa ElJazzar
    Date: 05/05/2023
    DroneMonitorEvent table/entity added to log monitor events for the Drone.
*/

@Entity
@Table(name = "DRONE_MONITOR_EVENTS")
@Setter
@Getter
public class DroneMonitorEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @Column(name = "timestamp", nullable = false)
    @NotNull(message = "timestamp has to be provided.")
    private LocalDateTime timestamp;

    @Column(name = "log_message", nullable = false)
    @NotNull(message = "message has to be provided.")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private DroneMonitorLogType logType;

    public DroneMonitorEvent() {
    }

    public DroneMonitorEvent(Drone drone, String message, DroneMonitorLogType logType) {
        this.drone = drone;
        this.message = message;
        this.logType = logType;
        this.timestamp = LocalDateTime.now();
    }
}
