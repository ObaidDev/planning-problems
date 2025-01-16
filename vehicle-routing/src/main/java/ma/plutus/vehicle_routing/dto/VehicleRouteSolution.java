package ma.plutus.vehicle_routing.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolverStatus;
import lombok.Data;
import ma.plutus.vehicle_routing.utils.DrivingTimeCalculator;
import ma.plutus.vehicle_routing.utils.HaversineDrivingTimeCalculator;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@PlanningSolution
public class VehicleRouteSolution {

    private String name;

    private Location southWestCorner;
    private Location northEastCorner;


    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @PlanningEntityCollectionProperty
    private List<Vehicle> vehicles ;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    private List<Visit> visits ;


    @PlanningScore
    private HardSoftLongScore score;


    private SolverStatus solverStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String scoreExplanation;



    public VehicleRouteSolution() {
    }

    public VehicleRouteSolution(String name, HardSoftLongScore score, SolverStatus solverStatus) {
        this.name = name;
        this.score = score;
        this.solverStatus = solverStatus;
    }

    @JsonCreator
    public VehicleRouteSolution(@JsonProperty("name") String name,
            @JsonProperty("southWestCorner") Location southWestCorner,
            @JsonProperty("northEastCorner") Location northEastCorner,
            @JsonProperty("startDateTime") LocalDateTime startDateTime,
            @JsonProperty("endDateTime") LocalDateTime endDateTime,
            @JsonProperty("vehicles") List<Vehicle> vehicles,
            @JsonProperty("visits") List<Visit> visits) {
        this.name = name;
        this.southWestCorner = southWestCorner;
        this.northEastCorner = northEastCorner;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.vehicles = vehicles;
        this.visits = visits;
        List<Location> locations = Stream.concat(
                vehicles.stream().map(Vehicle::getHomeLocation),
                visits.stream().map(Visit::getLocation)).toList();

        DrivingTimeCalculator drivingTimeCalculator = HaversineDrivingTimeCalculator.getInstance();
        drivingTimeCalculator.initDrivingTimeMaps(locations);
    }

    public String getName() {
        return name;
    }

    public Location getSouthWestCorner() {
        return southWestCorner;
    }

    public Location getNorthEastCorner() {
        return northEastCorner;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getTotalDrivingTimeSeconds() {
        return vehicles == null ? 0 : vehicles.stream().mapToLong(Vehicle::getTotalDrivingTimeSeconds).sum();
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

    public String getScoreExplanation() {
        return scoreExplanation;
    }

    public void setScoreExplanation(String scoreExplanation) {
        this.scoreExplanation = scoreExplanation;
    }
    
}