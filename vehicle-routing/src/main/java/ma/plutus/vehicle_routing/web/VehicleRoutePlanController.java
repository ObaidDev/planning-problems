package ma.plutus.vehicle_routing.web;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.api.solver.SolverStatus;
import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.dto.Job;
import ma.plutus.vehicle_routing.dto.VehicleRouteSolution;
import ma.plutus.vehicle_routing.services.VehicleRoutingService;
import ma.plutus.vehicle_routing.utils.TTLConcurrentMap;

@Slf4j
@RestController
@RequestMapping("/routing")
public class VehicleRoutePlanController {

    private VehicleRoutingService vehicleRoutingService;
    
    @Autowired
    VehicleRoutePlanController(
        VehicleRoutingService vehicleRoutingService 
    ) {

        this.vehicleRoutingService = vehicleRoutingService ;        
    }
    

    @PostMapping("/solve")
    public String solve(@RequestBody VehicleRouteSolution problem) {
        return vehicleRoutingService.solve(problem) ;
    }


    @GetMapping("/{jobId}")
    public VehicleRouteSolution getRouteSolution(@PathVariable String jobId) throws Exception {

        return vehicleRoutingService.getRoutePlan(jobId) ;
    }




    // @PostMapping("/solve-directly")
    // public VehicleRouteSolution solveDirectly(@RequestBody VehicleRouteSolution problem) {
    //     return vehicleRoutingService.solveDirectly(problem) ;
    // }
}
