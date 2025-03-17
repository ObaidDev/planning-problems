package ma.plutus.vehicle_routing.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import ai.timefold.solver.core.api.domain.solution.ConstraintWeightOverrides;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ma.plutus.vehicle_routing.utils.DrivingTimeCalculator;
import ma.plutus.vehicle_routing.utils.HaversineDrivingTimeCalculator;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@PlanningSolution
@Builder
@AllArgsConstructor
@Schema(description = "Represents the route solution for vehicles and visits.")
public class VehicleRouteSolution {

    @Schema(description = "The name of the route plan.", example = "demo")
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;


    @Schema(description = "The start date and time of the plan.", example = "2025-01-18T07:30:00")
    @NotNull(message = "Start date and time is required")
    // @FutureOrPresent(message = "Start date and time must be in the present or future")
    private LocalDateTime startDateTime;


    @Schema(description = "The end date and time of the plan.", example = "2025-01-19T00:00:00")
    @NotNull(message = "End date and time is required") 
    // @Future(message = "End date and time must be in the future")
    private LocalDateTime endDateTime;



    @Schema(description = "The list of vehicles in the plan.")
    @NotEmpty(message = "There must be at least one vehicle")
    @Valid
    @PlanningEntityCollectionProperty
    private List<Vehicle> vehicles ;



    @Schema(description = "The list of visits in the plan.")
    @NotEmpty(message = "There must be at least one visit")
    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    private List<Visit> visits ;


    @Schema(
        description = "The optimization score that reflects the current state of the optimization process. This score is calculated and updated during the optimization process. It is used for response purposes only and is not part of the request data.",
        example = "HardSoftLongScore(-1000, -500)"
    )
    @PlanningScore
    private HardSoftLongScore score;


    @Schema(
        description = "The status of the solver, indicating whether the optimization process has completed or is still ongoing. This is used for response purposes to indicate the solver's state.",
        example = "SOLVING"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SolverStatus solverStatus;


    @Schema(
        description = "A human-readable explanation of the optimization score, providing more context about the current state of the optimization. This field is used only in the response to explain the optimization result.",
        example = "The current optimization score is high, indicating a near-optimal solution."
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String scoreExplanation;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ConstraintWeightOverrides<HardSoftLongScore> constraintWeightOverrides;

    public VehicleRouteSolution() {
    }

    public VehicleRouteSolution(String name, HardSoftLongScore score, SolverStatus solverStatus) {
        this.name = name;
        this.score = score;
        this.solverStatus = solverStatus;
    }

    @JsonCreator
    public VehicleRouteSolution(@JsonProperty("name") String name,
            @JsonProperty("startDateTime") LocalDateTime startDateTime,
            @JsonProperty("endDateTime") LocalDateTime endDateTime,
            @JsonProperty("vehicles") List<Vehicle> vehicles,
            @JsonProperty("visits") List<Visit> visits) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.vehicles = vehicles;
        this.visits = visits;
        List<Location> locations = Stream.concat(
                vehicles.stream().map(Vehicle::getHomeLocation),
                visits.stream().map(Visit::getLocation)).toList();

        DrivingTimeCalculator drivingTimeCalculator = HaversineDrivingTimeCalculator.getInstance();
        drivingTimeCalculator.initDrivingTimeMaps(locations);
        

        Map<String , HardSoftLongScore> initOverwightsMap = Map.of() ;
        this.constraintWeightOverrides = ConstraintWeightOverrides.of(initOverwightsMap) ;

    }

    public String getName() {
        return name;
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