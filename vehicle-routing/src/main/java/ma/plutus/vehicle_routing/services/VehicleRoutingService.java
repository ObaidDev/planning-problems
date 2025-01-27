package ma.plutus.vehicle_routing.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ai.timefold.solver.core.api.domain.solution.ConstraintWeightOverrides;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.api.solver.SolverStatus;
import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.dto.ConstraintWeightOverrideDto;
import ma.plutus.vehicle_routing.dto.VehicleRouteSolution;
import ma.plutus.vehicle_routing.exception.RoutePlanNotFoundException;
import ma.plutus.vehicle_routing.mappers.ConstraintsWeightMapper;
import ma.plutus.vehicle_routing.utils.TTLConcurrentMap;


@Slf4j
@Service
public class VehicleRoutingService {


    // private final Solver<VehicleRouteSolution> solver;

    private final SolutionManager<VehicleRouteSolution, HardSoftLongScore> solutionManager;

    private final SolverManager<VehicleRouteSolution, String> solverManager;



    private static final int MAX_RECOMMENDED_FIT_LIST_SIZE = 5;


    private final TTLConcurrentMap<String, Job> jobIdToJob = new TTLConcurrentMap<>(120_00);

    private final ConstraintsWeightMapper constraintsWeightMapper ;
    

    @Autowired
    VehicleRoutingService(
        ConstraintsWeightMapper constraintsWeightMapper ,
        SolutionManager<VehicleRouteSolution, HardSoftLongScore> solutionManager ,
        @Qualifier("solverManager30Seconds") SolverManager<VehicleRouteSolution, String> solverManager 
    ) {

        this.solverManager = solverManager ;
        this.solutionManager = solutionManager ;
        this.constraintsWeightMapper = constraintsWeightMapper ;
    }



    public String solve(VehicleRouteSolution problem) {
        String jobId = UUID.randomUUID().toString();
        jobIdToJob.put(jobId, Job.ofRoutePlan(problem));
        solverManager.solveBuilder()
                .withProblemId(jobId)
                .withProblemFinder(joBId -> jobIdToJob.get(jobId).routePlan)
                .withBestSolutionConsumer(solution -> jobIdToJob.put(jobId, Job.ofRoutePlan(solution)))
                .withExceptionHandler((joBId, exception) -> {
                    jobIdToJob.put(jobId, Job.ofException(exception));
                    log.error("Failed solving jobId ({}).", jobId, exception);
                })
                .run();
        return jobId;
    }

    public VehicleRouteSolution getRoutePlan( String jobId) throws Exception {
        VehicleRouteSolution routePlan = getRoutePlanAndCheckForExceptions(jobId);
        SolverStatus solverStatus = solverManager.getSolverStatus(jobId);
        String scoreExplanation = solutionManager.explain(routePlan).getSummary();
        routePlan.setSolverStatus(solverStatus);
        routePlan.setScoreExplanation(scoreExplanation);
        return routePlan;
    }


    public void overWriteConstraintsWeight(String jobId , List<ConstraintWeightOverrideDto> constraintWeightOverridesDtos) {

        VehicleRouteSolution routePlan = getRoutePlanAndCheckForExceptions(jobId);

        routePlan.setConstraintWeightOverrides(
        constraintsWeightMapper.toConstraintWeightOverrides(constraintWeightOverridesDtos)
        );
    }











    public record Job(VehicleRouteSolution routePlan, Throwable exception) {

        static Job ofRoutePlan(VehicleRouteSolution routePlan) {
            return new Job(routePlan, null);
        }

        static Job ofException(Throwable exception) {
            return new Job(null, exception);
        }

    }



    private VehicleRouteSolution getRoutePlanAndCheckForExceptions(String jobId) {
        Job job = jobIdToJob.get(jobId);
        if (job == null) {
            throw new RoutePlanNotFoundException("No route plan found.");
        }
        if (job.exception != null) {
            throw new RoutePlanNotFoundException(job.exception.getMessage());
        }
        return job.routePlan;
    }
}
