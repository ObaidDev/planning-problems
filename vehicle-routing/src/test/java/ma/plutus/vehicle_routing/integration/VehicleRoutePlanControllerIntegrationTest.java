package ma.plutus.vehicle_routing.integration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.dto.Location;
import ma.plutus.vehicle_routing.dto.Vehicle;
import ma.plutus.vehicle_routing.dto.VehicleRouteSolution;
import ma.plutus.vehicle_routing.dto.Visit;



@Slf4j
public class VehicleRoutePlanControllerIntegrationTest extends BaseIntegrationTest{


    private static final ThreadLocal<String> sharedResponse = new ThreadLocal<>();



    @Test
    public void testPostAPlan() throws Exception {

        String jsonPayload = objectMapper.writeValueAsString(createValidVehicleRouteSolution());

        /**
         * Post vehicleRoutePlan
         */

        String responseFromTestPostAPlan = mockMvc.perform(
                                                MockMvcRequestBuilders.post("/routing/solve")
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .content(jsonPayload)
                                                )
                                            .andExpect(MockMvcResultMatchers.status().isOk())
                                            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                                            .andReturn().getResponse().getContentAsString();

        sharedResponse.set(responseFromTestPostAPlan);

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
    public void testPostAPlan_InvalidInput_MissingName() throws Exception {

        VehicleRouteSolution invalidInput = createValidVehicleRouteSolution();
        invalidInput.setName(null) ;

        String jsonPayload = objectMapper.writeValueAsString(invalidInput);

        // Act & Assert
        mockMvc.perform(
                MockMvcRequestBuilders.post("/routing/solve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("Name cannot be null"));
    }









    private VehicleRouteSolution createValidVehicleRouteSolution() {
        Location southWestCorner = new Location(39.7656099067391, -76.83782328143754);
        Location northEastCorner = new Location(40.77636644354855, -74.9300739430771);
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
        vehicleRouteSolution.setSouthWestCorner(southWestCorner);
        vehicleRouteSolution.setNorthEastCorner(northEastCorner);
        vehicleRouteSolution.setStartDateTime(LocalDateTime.of(2025, 1, 16, 7, 30));
        vehicleRouteSolution.setEndDateTime(LocalDateTime.of(2025, 1, 17, 0, 0));
        vehicleRouteSolution.setVehicles(List.of(vehicle));
        vehicleRouteSolution.setVisits(List.of(visit));

        return vehicleRouteSolution;
    }
    
}
