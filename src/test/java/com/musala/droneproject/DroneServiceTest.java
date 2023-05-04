package com.musala.droneproject;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import com.musala.droneproject.module.drone.service.DroneService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DroneControllerTest That will test Controller Apis created to manage drones loading
 */

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DroneServiceTest {
//    @InjectMocks
    @Autowired
    private DroneService droneService;

//    @Mock
    @MockBean
    private DroneRepository droneRepository;

    @Test
    public void testSaveDrone() {
        // create a new drone object
        Drone drone = new Drone("1234", DroneModel.Middleweight, 400, 90.0, DroneState.IDLE);

        // specify the behavior of the mock repository
        when(droneRepository.save(drone)).thenReturn(drone);

        // call the method being tested
        Drone savedDrone = droneService.saveDrone(drone);

        // verify that the mock repository was called with the correct arguments
        verify(droneRepository, times(1)).save(drone);

        // assert that the returned drone object is the same as the one passed as an argument
        Assertions.assertEquals(drone, savedDrone);
    }

}
