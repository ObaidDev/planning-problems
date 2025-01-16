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
@AutoConfigureMockMvc
@EnabledInNativeImage
public class VehicleRoutePlanControllerIntegrationTest extends BaseIntegrationTest{



    private final MockMvc mockMvc ;

    private final ObjectMapper objectMapper ;

    private static final ThreadLocal<String> sharedResponse = new ThreadLocal<>();


    @Autowired
    VehicleRoutePlanControllerIntegrationTest(
        MockMvc mockMvc ,
        ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc ;

        this.objectMapper = objectMapper ;
    }



    @Test
    public void testPostAPlan() throws Exception {

        Location southWestCorner = new Location(39.7656099067391, -76.83782328143754);
        Location northEastCorner = new Location(40.77636644354855, -74.9300739430771);
        Location vehicleHomeLocation = new Location(40.605994321126936, -75.68106859680056);
        Location visitLocation = new Location(40.397480680074565, -76.05412711128848);

        // Create Visit object
        Visit visit = new Visit();
        visit.setId("1");
        visit.setName("Hugo Rye");
        visit.setLocation(visitLocation);
        visit.setDemand(2);
        visit.setMinStartTime(LocalDateTime.of(2025, 1, 16, 8, 0));
        visit.setMaxEndTime(LocalDateTime.of(2025, 1, 16, 12, 0));
        visit.setServiceDuration(Duration.ofSeconds(1800));

        // Create Vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setId("1");
        vehicle.setCapacity(15);
        vehicle.setHomeLocation(vehicleHomeLocation);
        vehicle.setDepartureTime(LocalDateTime.of(2025, 1, 16, 7, 30));
        vehicle.setVisits(Collections.emptyList()); // Empty visits initially

        // Create VehicleRouteSolution object
        VehicleRouteSolution vehicleRouteSolution = new VehicleRouteSolution();
        vehicleRouteSolution.setName("demo");
        vehicleRouteSolution.setSouthWestCorner(southWestCorner);
        vehicleRouteSolution.setNorthEastCorner(northEastCorner);
        vehicleRouteSolution.setStartDateTime(LocalDateTime.of(2025, 1, 16, 7, 30));
        vehicleRouteSolution.setEndDateTime(LocalDateTime.of(2025, 1, 17, 0, 0));
        vehicleRouteSolution.setVehicles(List.of(vehicle));
        vehicleRouteSolution.setVisits(List.of(visit));

        String jsonPayload = objectMapper.writeValueAsString(vehicleRouteSolution);



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
    
}
