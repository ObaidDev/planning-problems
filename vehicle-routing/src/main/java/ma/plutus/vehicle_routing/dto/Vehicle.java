package ma.plutus.vehicle_routing.dto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.plutus.vehicle_routing.annotations.EmptyList;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(scope = Vehicle.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@PlanningEntity
@Builder
@AllArgsConstructor
@Schema(description = "Represents a vehicle used in the route plan.")
public class Vehicle implements LocationAware {


    @PlanningId
    private String id;


    @Schema(description = "The capacity of the vehicle.", example = "15")
    @NotNull(message = "Capacity not null.")
    @Min(message = "Capactity must be equal or greater than 5" , value = 5)
    private int capacity;


    @Schema(description = "The home location of the vehicle.", example = "[40.605994321126936, -75.68106859680056]")
    @JsonIdentityReference
    @NotNull(message = "Home location must not be null.")
    private Location homeLocation;


    @Schema(description = "The departure time of the vehicle.", example = "2025-01-18T07:30:00")
    @NotNull(message = "The departure time of the vehicle must not be null")
    private LocalDateTime departureTime;


    @Schema(description = "The list of visits assigned to the vehicle 'empty list'." , example = "[]")
    @JsonIdentityReference(alwaysAsId = true)
    @EmptyList(message = "The list of visits must be empty list")
    @PlanningListVariable
    private List<Visit> visits;


    public Vehicle() {
    }

    public Vehicle(String id, int capacity, Location homeLocation, LocalDateTime departureTime) {
        this.id = id;
        this.capacity = capacity;
        this.homeLocation = homeLocation;
        this.departureTime = departureTime;
        this.visits = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    @JsonIgnore
    @Override
    public Location getLocation() {
        return homeLocation;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public int getTotalDemand() {
        int totalDemand = 0;
        for (Visit visit : visits) {
            totalDemand += visit.getDemand();
        }
        return totalDemand;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getTotalDrivingTimeSeconds() {
        if (visits.isEmpty()) {
            return 0;
        }

        long totalDrivingTime = 0;
        Location previousLocation = homeLocation;

        for (Visit visit : visits) {
            totalDrivingTime += previousLocation.getDrivingTimeTo(visit.getLocation());
            previousLocation = visit.getLocation();
        }
        totalDrivingTime += previousLocation.getDrivingTimeTo(homeLocation);

        return totalDrivingTime;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime arrivalTime() {
        if (visits.isEmpty()) {
            return departureTime;
        }

        Visit lastVisit = visits.get(visits.size() - 1);
        return lastVisit.getDepartureTime().plusSeconds(lastVisit.getLocation().getDrivingTimeTo(homeLocation));
    }

    @Override
    public String toString() {
        return id;
    }

}