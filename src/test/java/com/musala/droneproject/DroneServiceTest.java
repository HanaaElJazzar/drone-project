package com.musala.droneproject;

import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.repository.DroneRepository;
import com.musala.droneproject.module.drone.service.DroneService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.annotation.PrepareTestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DroneServiceTest That will test Service methods created to manage drones
 */

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DroneServiceTest {

    @Autowired
    private DroneService droneService;

    @MockBean
    private DroneRepository droneRepository;

    @Test
    public void testSaveDrone() {
        // create a new drone object
        Drone drone = new Drone("1234", DroneModel.Middleweight, 400.0, 90.0, DroneState.IDLE);

        // specify the behavior of the mock repository
        when(droneRepository.save(drone)).thenReturn(drone);

        // call the method being tested
        Drone savedDrone = droneService.saveDrone(drone);

        // verify that the mock repository was called with the correct arguments
        verify(droneRepository, times(1)).save(drone);

        // assert that the returned drone object is the same as the one passed as an argument
        Assertions.assertEquals(drone, savedDrone);
    }

    @Test
    public void testGetAvailableDrones(){
        //Create List of Mock sample Drone Data
        List<Drone> drones = new ArrayList<>();
        drones.add(new Drone(1L, "DR1234567A", DroneModel.Lightweight, 300.0, 76.0, DroneState.IDLE));
        drones.add(new Drone(2L, "DR1234567B", DroneModel.Middleweight, 400.0, 13.0, DroneState.IDLE));
        drones.add(new Drone(3L, "DR1234567C", DroneModel.Cruiserweight, 450.0, 90.8, DroneState.IDLE));

        //Set up the mock repository to return the available drones
        when(droneRepository.findByStateOrderByIdAsc(DroneState.IDLE)).thenReturn(drones);

        //Call the service method
        List<Drone> responseDrones = droneService.getAvailabeDrones();

        //Verify the repository method was called
        verify(droneRepository, times(1)).findByStateOrderByIdAsc(DroneState.IDLE);

        //verify the correct drones are returned
        Assertions.assertEquals(3, responseDrones.size());
        Assertions.assertEquals(drones, responseDrones);
        Assertions.assertTrue(responseDrones.stream().allMatch(d -> d.getState() == DroneState.IDLE));
    }

    /*
        Test getBatteryCapacity Service
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Test
    public void testGetBatteryCapacity(){
        Drone drone = new Drone("1234", DroneModel.Middleweight, 400.0, 90.0, DroneState.IDLE);

        //Mock Response of Repository
        when(droneRepository.findDroneBySerialNumber("1234")).thenReturn(drone);

        //Call getBatteryCapacityService
        Double actualBatteryCap = droneService.getBatteryCapacity("1234");

        //Verify the call of droneRepository.findDroneBySerialNumber(serialNumber) once
        verify(droneRepository, times(1)).findDroneBySerialNumber("1234");

        //Assert Results returned
        Assertions.assertEquals(drone.getBatteryCapacity(), actualBatteryCap);
    }
}
