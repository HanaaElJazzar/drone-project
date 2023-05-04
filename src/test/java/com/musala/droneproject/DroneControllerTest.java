package com.musala.droneproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musala.droneproject.module.drone.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.service.DroneService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    /*
        Test Successfully registering Drone
        Added By: Hanaa ElJazzar
        Date: 03/05/2023
    */
    @Test
    public void testRegisterDroneSuccess() throws Exception {
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 500.0, 100.0, DroneState.IDLE);

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

    /*
        Test Failed registeration of Drone by sending wrong betteryCapacity and weightLimit values.
        Added By: Hanaa ElJazzar
        Date: 03/05/2023
    */
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
                .andExpect(jsonPath("$.message").exists());
                //.andExpect(content().json(responseJson));

        verify(droneService, times(0)).saveDrone(any(Drone.class));
    }

    /*
        Test getAvailableDrones Controller Api.
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Test
    public void testGetAvailableDronesSuccess() throws Exception{
        //Create List of Drones
        List<Drone> drones = new ArrayList<>();
        drones.add(new Drone(1L, "DR1234567A", DroneModel.Lightweight, 300.0, 76.0, DroneState.IDLE));
        drones.add(new Drone(2L, "DR1234567B", DroneModel.Middleweight, 400.0, 13.0, DroneState.IDLE));
        drones.add(new Drone(3L, "DR1234567C", DroneModel.Cruiserweight, 450.0, 90.8, DroneState.IDLE));

        //Create a mock service to return available drones
        when(droneService.getAvailabeDrones()).thenReturn(drones);

        MvcResult result = mockMvc.perform(get("/api/v1/drones/getAvailableDrones")
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Assert the returned response entity object
        String responseStr = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());    //to deserialize the response
        BasicResponse<List<Drone>> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BasicResponse<List<Drone>>>() {});
        Assertions.assertEquals(drones.size(), actualResponse.getData().size());
        for (int i = 0; i < drones.size(); i++) {
            Assertions.assertEquals(drones.get(i).getId(), actualResponse.getData().get(i).getId());
            Assertions.assertEquals(drones.get(i).getSerialNumber(), actualResponse.getData().get(i).getSerialNumber());
            Assertions.assertEquals(drones.get(i).getModel(), actualResponse.getData().get(i).getModel());
            Assertions.assertEquals(drones.get(i).getState(), actualResponse.getData().get(i).getState());
            Assertions.assertEquals(drones.get(i).getBatteryCapacity(), actualResponse.getData().get(i).getBatteryCapacity());
            Assertions.assertEquals(drones.get(i).getWeightLimit(), actualResponse.getData().get(i).getWeightLimit());
        }
    }

    /*
        Test getBatteryCapacity Controller Api
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Test
    public void testGetBatteryCapacitySuccess() throws Exception{
        //Sample Drone
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 300.0, 80.0, DroneState.IDLE);

        //Create a MockService that returns BatteryCapacity
        when(droneService.getBatteryCapacity("serial123")).thenReturn(drone.getBatteryCapacity());

        mockMvc.perform(get("/api/v1/drones/getDroneBatteryCapacity")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.batteryCapacity", is(drone.getBatteryCapacity())))
                .andExpect(jsonPath("$.message", is("Battery Capacity Returned.")));

        verify(droneService, times(1)).getBatteryCapacity("1234");
    }
}
