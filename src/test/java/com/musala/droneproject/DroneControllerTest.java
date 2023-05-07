package com.musala.droneproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musala.droneproject.module.common.dto.response.BasicResponse;
import com.musala.droneproject.module.drone.dto.response.AvailableDronesResponse;
import com.musala.droneproject.module.drone.dto.response.DroneMedicationsResponse;
import com.musala.droneproject.module.drone.entity.Drone;
import com.musala.droneproject.module.drone.enums.DroneModel;
import com.musala.droneproject.module.drone.enums.DroneState;
import com.musala.droneproject.module.drone.service.DroneService;
import com.musala.droneproject.module.medication.entity.Medication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

        when(droneService.saveNewDrone(any(Drone.class))).thenReturn(drone);

        mockMvc.perform(post("/api/v1/drones/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(droneJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("New Drone created successfully")))
                .andExpect(jsonPath("$.data.drone.serialNumber", is("serial123")))
                .andExpect(jsonPath("$.data.drone.model", is("Lightweight")))
                .andExpect(jsonPath("$.data.drone.weightLimit", is(500.0)))
                .andExpect(jsonPath("$.data.drone.batteryCapacity", is(100.0)))
                .andExpect(jsonPath("$.data.drone.state", is("IDLE")));

        verify(droneService, times(1)).saveNewDrone(any(Drone.class));
    }

    /*
        Test Failed registeration of Drone by sending wrong betteryCapacity and weightLimit values.
        Added By: Hanaa ElJazzar
        Date: 03/05/2023
    */
    @Test
    public void testRegisterDroneFail() throws Exception {
        //Test Using wrong WeightLimit and wrong Battery Capacity Values to return Error Message
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 600.0, 200.0, DroneState.IDLE);

        // create a request to save the drone
        ObjectMapper objectMapper = new ObjectMapper();
        String droneJson = objectMapper.writeValueAsString(drone);

        /*
           Removed Response as Exact validation of objects for different message composition
           and just checks the validity of the error message returned not empty
           Removed by Hanaa on 06/07/2023
         */
        // BasicResponse<Drone> response = new BasicResponse<Drone>();
        // response.setSuccess(false);
        // response.setMessage("Invalid drone register request: batteryCapacity (Battery capacity cannot exceed 100%), weightLimit (Weight limit cannot exceed 500 grams)");

        // create a request to save the drone
        //ObjectMapper responseMapper = new ObjectMapper();
        //String responseJson = responseMapper.writeValueAsString(response);
        // -- END 06/07/2023

        when(droneService.saveNewDrone(any(Drone.class))).thenReturn(drone);
        mockMvc.perform(post("/api/v1/drones/registerDrone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(droneJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message").exists());
        //.andExpect(content().json(responseJson));

        //Validate save method is not called at all
        verify(droneService, times(0)).saveNewDrone(any(Drone.class));
    }

    /*
        Test getAvailableDrones Controller Api.
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Test
    public void testGetAvailableDronesSuccess() throws Exception {
        //Create List of Drones
        List<Drone> drones = new ArrayList<>();
        drones.add(new Drone(1L, "DR1234567A", DroneModel.Lightweight, 300.0, 76.0, DroneState.IDLE));
        drones.add(new Drone(2L, "DR1234567B", DroneModel.Middleweight, 400.0, 13.0, DroneState.IDLE));
        drones.add(new Drone(3L, "DR1234567C", DroneModel.Cruiserweight, 450.0, 90.8, DroneState.IDLE));

        //Create a mock service to return available drones
        when(droneService.getAvailableDrones()).thenReturn(drones);

        MvcResult result = mockMvc.perform(get("/api/v1/drones/getAvailableDrones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Assert the returned response entity object
        //String responseStr = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());    //to deserialize the response
        BasicResponse<AvailableDronesResponse> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BasicResponse<AvailableDronesResponse>>() {
        });
        Assertions.assertEquals(drones.size(), actualResponse.getData().getAvailableDrones().size());
        List<Drone> availableDrones = actualResponse.getData().getAvailableDrones();
        for (int i = 0; i < drones.size(); i++) {
            Assertions.assertEquals(drones.get(i).getId(), availableDrones.get(i).getId());
            Assertions.assertEquals(drones.get(i).getSerialNumber(), availableDrones.get(i).getSerialNumber());
            Assertions.assertEquals(drones.get(i).getModel(), availableDrones.get(i).getModel());
            Assertions.assertEquals(drones.get(i).getState(), availableDrones.get(i).getState());
            Assertions.assertEquals(drones.get(i).getBatteryCapacity(), availableDrones.get(i).getBatteryCapacity());
            Assertions.assertEquals(drones.get(i).getWeightLimit(), availableDrones.get(i).getWeightLimit());
        }
    }

    /*
        Test getBatteryCapacity Controller Api
        Added By: Hanaa ElJazzar
        Date: 04/05/2023
     */
    @Test
    public void testGetBatteryCapacitySuccess() throws Exception {
        //Sample Drone
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 300.0, 80.0, DroneState.IDLE);

        //Create a MockService that returns BatteryCapacity
        when(droneService.getBatteryCapacity("serial123")).thenReturn(drone.getBatteryCapacity());
        String json = "{ \"serialNumber\": \"serial123\"}";

        mockMvc.perform(get("/api/v1/drones/getDroneBatteryCapacity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.batteryCapacity", is(drone.getBatteryCapacity())))
                .andExpect(jsonPath("$.message", is("Battery Capacity Returned.")));

        verify(droneService, times(1)).getBatteryCapacity("serial123");
    }

    /*
        Added by: Hanaa ElJazzar
        Date: 07/05/2023
        Added to Test getLoadedMedications API
     */
    @Test
    public void testGetLoadedMedications() throws Exception{
        //Sample Testing Data
        Drone drone = new Drone("serial123", DroneModel.Lightweight, 300.0, 80.0, DroneState.LOADED);
        Medication medication1 = new Medication("Medication01", 100.0, "MED01", null);
        Medication medication2 = new Medication("Medication02", 50.0, "MED02", null);

        drone.setMedications(new ArrayList<Medication>());
        drone.getMedications().add(medication1);
        drone.getMedications().add(medication2);

        //Mock Sample Returned Data
        when(droneService.getDrone("serial123")).thenReturn(drone);

        //Api Request
        String json = "{ \"serialNumber\": \"serial123\"}";

        MvcResult result =  mockMvc.perform(get("/api/v1/drones/getLoadedMedications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.message", is("Medications Loaded into drone serial123 returned successfully.")))
                .andExpect(jsonPath("$.success", is(true)))
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());    //to deserialize the response
        BasicResponse<DroneMedicationsResponse> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<BasicResponse<DroneMedicationsResponse>>() {
        });
        Assertions.assertEquals(drone.getMedications().size(), actualResponse.getData().getMedications().size());
        List<Medication> loadedMedications = actualResponse.getData().getMedications();
        for (int i = 0; i < drone.getMedications().size(); i++) {
            Assertions.assertEquals(drone.getMedications().get(i).getWeight(), loadedMedications.get(i).getWeight());
            Assertions.assertEquals(drone.getMedications().get(i).getName(), loadedMedications.get(i).getName());
            Assertions.assertEquals(drone.getMedications().get(i).getCode(), loadedMedications.get(i).getCode());
            Assertions.assertEquals(drone.getMedications().get(i).getImage(), loadedMedications.get(i).getImage());
        }

        verify(droneService, times(1)).getDrone("serial123");
    }
}
