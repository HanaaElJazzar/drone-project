package com.musala.droneproject;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.dronemonitorevent.entity.DroneMonitorEvent;
import com.musala.droneproject.module.dronemonitorevent.enums.DroneMonitorLogType;
import com.musala.droneproject.module.dronemonitorevent.repository.DroneMonitorEventRepository;
import com.musala.droneproject.module.dronemonitorevent.service.DroneMonitorEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;

/*
    Added by: Hanaa ElJazzar
    Date: 05/05/2023
    Added to test DroneMonitorEvent Logging
*/
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DroneMonitorEventServiceTest {

    @Autowired
    private DroneMonitorEventService droneMonitorEventService;

    @MockBean
    private DroneMonitorEventRepository droneMonitorEventRepository;

    @Test
    public void testDroneMonitorLogEvent()
    {
        //Create a sample data to log
        Drone drone = new Drone("serialNumber123", DroneModel.Lightweight, 150.0, 20.0, DroneState.IDLE);
        String message = "Test Message to Log";
        DroneMonitorLogType droneMonitorLogType = DroneMonitorLogType.BATTERY_MONITOR;
        DroneMonitorEvent droneEvent = new DroneMonitorEvent(drone, message, droneMonitorLogType);

        //Mock Result
        when(droneMonitorEventRepository.save(any(DroneMonitorEvent.class))).thenReturn(droneEvent);

        //Call Service
        Boolean result = droneMonitorEventService.logEvent(drone, message, droneMonitorLogType);

        //Verify the call of the repository method at least once
        verify(droneMonitorEventRepository, times(1)).save(any(DroneMonitorEvent.class));

        //Assert results
        Assertions.assertEquals(result, true);

    }
}
