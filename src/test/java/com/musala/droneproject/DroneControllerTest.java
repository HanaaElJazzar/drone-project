package com.musala.droneproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.droneproject.module.drone.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.service.DroneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    Created by: Hanaa ElJazzar
    Created on: 03/05/2023
    This will create DroneControllerTest That will test Controller Apis created to manage drones loading
 */

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    /* Test Successfully registering Drone */
    @Test
    public void testRegisterDroneSuccess() throws Exception {
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 500.0, 100.0, DroneState.IDLE);
        //drone.setId(1L);

        //String json = "{ \"serialNumber\": \"serial123\", \"model\": \"Lightweight\", \"weightLimit\": 500, \"batteryCapacity\": 100, \"state\": \"IDLE\" }";
        // create a request to save the drone
        ObjectMapper objectMapper = new ObjectMapper();
        String droneJson = objectMapper.writeValueAsString(drone);

        when(droneService.saveDrone(any(Drone.class))).thenReturn(drone);
        mockMvc.perform(post("/api/v1/drones/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(droneJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("New Drone created successfully")))
                .andExpect(jsonPath("$.data.serialNumber", is("serial123")))
                .andExpect(jsonPath("$.data.model", is("Lightweight")))
                .andExpect(jsonPath("$.data.weightLimit", is(500.0)))
                .andExpect(jsonPath("$.data.batteryCapacity", is(100.0)))
                .andExpect(jsonPath("$.data.state", is("IDLE")));

        verify(droneService, times(1)).saveDrone(any(Drone.class));
    }

    /* Test Failed registeration of Drone by sending wrong betteryCapacity and weightLimit values */
    @Test
    public void testRegisterDroneFail() throws Exception {
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 600.0, 200.0, DroneState.IDLE);

        // create a request to save the drone
        ObjectMapper objectMapper = new ObjectMapper();
        String droneJson = objectMapper.writeValueAsString(drone);

        BasicResponse<Drone> response = new BasicResponse<Drone>();
        response.setSuccess(false);
        response.setMessage("Invalid drone register request: batteryCapacity (Battery capacity cannot exceed 100%), weightLimit (Weight limit cannot exceed 500 grams)");

        // create a request to save the drone
        ObjectMapper responseMapper = new ObjectMapper();
        String responseJson = responseMapper.writeValueAsString(response);

        when(droneService.saveDrone(any(Drone.class))).thenReturn(drone);
        mockMvc.perform(post("/api/v1/drones/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(droneJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
                //.andExpect(content().json(responseJson));

        verify(droneService, times(0)).saveDrone(any(Drone.class));
    }
}
