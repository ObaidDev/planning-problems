package ma.plutus.vehicle_routing.dto;

import java.time.Instant;
import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@PlanningSolution
public class VehicleRouteSolution {

    private String name;

    private Location southWestCorner;
    private Location northEastCorner;


    private Instant startDateTime;

    private Instant endDateTime;

    @PlanningEntityCollectionProperty
    private List<Vehicle> vehicles ;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    private List<Visit> visits ;


    @PlanningScore
    private HardSoftLongScore score;


    private SolverStatus solverStatus;


    private String scoreExplanation;














    public long getTotalDrivingTimeSeconds() {
        return vehicles == null ? 0 : vehicles.stream().mapToLong(Vehicle::getTotalDrivingTimeSeconds).sum();
    }
    
}