package ma.plutus.vehicle_routing.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.trackswiftly.vehicle_routing.dto.VehicleRouteSolution;
import com.trackswiftly.vehicle_routing.services.VehicleRoutingService;
import com.trackswiftly.vehicle_routing.utils.TTLConcurrentMap;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.SolverJobBuilder;
import ai.timefold.solver.core.api.solver.SolverManager;




class VehicleRoutingServiceTest {
    
    @Mock
    private SolutionManager<VehicleRouteSolution, HardSoftLongScore> solutionManager;

    @Mock
    private SolverManager<VehicleRouteSolution, String> solverManager;

    @Mock
    private TTLConcurrentMap<String, VehicleRoutingService.Job> jobIdToJob;

    @InjectMocks
    private VehicleRoutingService vehicleRoutingService ;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this) ;
    }


    // @Test
    public void testSolve() {
        VehicleRouteSolution problem = new VehicleRouteSolution();
        
        String result = vehicleRoutingService.solve(problem);

        SolverJobBuilder<VehicleRouteSolution, String> solverJobBuilder = 
        Mockito.mock(SolverJobBuilder.class);


        Mockito.when(solverManager.solveBuilder()).thenReturn(solverJobBuilder) ;

        assertNotNull(result);

    }
}
