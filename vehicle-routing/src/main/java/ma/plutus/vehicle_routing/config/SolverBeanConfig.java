package ma.plutus.vehicle_routing.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ai.timefold.solver.core.api.score.ScoreManager;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import ma.plutus.vehicle_routing.constraints.VehicleConstraintProvider;
import ma.plutus.vehicle_routing.dto.Vehicle;
import ma.plutus.vehicle_routing.dto.VehicleRouteSolution;
import ma.plutus.vehicle_routing.dto.Visit;



@Configuration
public class SolverBeanConfig {

    @Bean("solverManager30Seconds")
    public SolverManager<VehicleRouteSolution, String> solverManager(SolverConfig solverConfig) {

        return SolverManager.create(solverConfig);
    }




    @Bean
    public SolverConfig solverConfig() {
        return new SolverConfig()
            .withSolutionClass(VehicleRouteSolution.class)
            .withEntityClassList(List.of(Vehicle.class, Visit.class))
            .withConstraintProviderClass(VehicleConstraintProvider.class)
            .withTerminationConfig(new TerminationConfig()
                .withSecondsSpentLimit(120L) // Terminate after 30 seconds
            );
    }
}