package ma.plutus.vehicle_routing.integration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.trackswiftly.vehicle_routing.dto.Location;
import com.trackswiftly.vehicle_routing.dto.Vehicle;
import com.trackswiftly.vehicle_routing.dto.VehicleRouteSolution;
import com.trackswiftly.vehicle_routing.dto.Visit;

import lombok.extern.slf4j.Slf4j;



@Slf4j
class VehicleRoutePlanControllerIntegrationTest extends BaseIntegrationTest{


    private static final ThreadLocal<String> sharedResponse = new ThreadLocal<>();



    @Test
    void testPostAPlan() throws Exception {

        String jsonPayload = objectMapper.writeValueAsString(createValidVehicleRouteSolution());

        /**
         * Post vehicleRoutePlan
         */

        String responseFromTestPostAPlan = mockMvc.perform(
                                                MockMvcRequestBuilders.post("/routing/solve")
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .content(jsonPayload)
                                                )
                                            .andExpect(MockMvcResultMatchers.status().isCreated())
                                            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                            .andReturn().getResponse().getContentAsString();
        
        Map<String,Object> response = objectMapper.readValue(
            responseFromTestPostAPlan
            , 
            new TypeReference<Map<String,Object>>() {}
            );

        sharedResponse.set(response.get("planingID").toString());

        log.info("Response from testPostAPlan: {} ✅", responseFromTestPostAPlan);
    }




    @Test
    public void testGetAPlan() throws Exception {

        log.debug("jobId : {} ✅" , sharedResponse.get());

        mockMvc.perform(
            MockMvcRequestBuilders.get("/routing/" + sharedResponse.get())
                                    .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("name").value("demo"));
    }



    @Test
    void testPostAPlan_InvalidInput_MissingName() throws Exception {

        VehicleRouteSolution invalidInput = createValidVehicleRouteSolution();
        invalidInput.setName(null) ;

        String jsonPayload = objectMapper.writeValueAsString(invalidInput);

        // Act & Assert
        MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.post("/routing/solve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPayload)
                            )
                            .andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists()) 
                            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").exists())
                            .andReturn();

        /***
         * 
         * assert the type of the field in the response 
         */

        String responseJson = result.getResponse().getContentAsString();
        
        /***
         * assert message
         */

        String message = JsonPath.read(responseJson, "$.message");

        Assertions.assertThat(message).isInstanceOf(String.class) ;



        /***
         * assert message
         */

        List<?> errors = JsonPath.read(responseJson, "$.errors");
        Assertions.assertThat(errors).isInstanceOf(List.class);
    }









    private VehicleRouteSolution createValidVehicleRouteSolution() {

        Location vehicleHomeLocation = new Location(40.605994321126936, -75.68106859680056);
        Location visitLocation = new Location(40.397480680074565, -76.05412711128848);

        Visit visit = Visit.builder()
                .id("1")
                .name("Hugo Rye")
                .location(visitLocation)
                .demand(2)
                .minStartTime(LocalDateTime.of(2025, 1, 16, 8, 0))
                .maxEndTime(LocalDateTime.of(2025, 1, 16, 12, 0))
                .serviceDuration(Duration.ofSeconds(1800))
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id("1")
                .capacity(15)
                .homeLocation(vehicleHomeLocation)
                .departureTime(LocalDateTime.of(2025, 1, 16, 7, 30))
                .visits(Collections.emptyList()) // Empty list
                .build();

        VehicleRouteSolution vehicleRouteSolution = new VehicleRouteSolution();
        vehicleRouteSolution.setName("demo");
        vehicleRouteSolution.setStartDateTime(LocalDateTime.of(2025, 1, 16, 7, 30));
        vehicleRouteSolution.setEndDateTime(LocalDateTime.of(2025, 1, 17, 0, 0));
        vehicleRouteSolution.setVehicles(List.of(vehicle));
        vehicleRouteSolution.setVisits(List.of(visit));

        return vehicleRouteSolution;
    }
    
}
