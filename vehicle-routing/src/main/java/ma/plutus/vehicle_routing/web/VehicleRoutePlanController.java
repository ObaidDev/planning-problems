package ma.plutus.vehicle_routing.web;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.domain.solution.ConstraintWeightOverrides;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.dto.ConstraintWeightOverrideDto;
import ma.plutus.vehicle_routing.dto.ErrorResponse;
import ma.plutus.vehicle_routing.dto.VehicleRouteSolution;
import ma.plutus.vehicle_routing.services.VehicleRoutingService;


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
    @Operation(
        summary = "Solve vehicle routing problem",
        description = "Accepts a vehicle routing problem and returns a solution. Validates the input data to ensure it meets the required constraints."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solution generated successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = String.class, example = "Solution123")
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request. This could be due to invalid or missing data in the request body.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class) // Use the ErrorResponse schema
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error. This could be due to a problem with the solver or an unexpected error.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class) // Use the ErrorResponse schema
        )
    )
    public ResponseEntity<Map<String,Object>> solve(
        @Parameter(
            description = "Vehicle routing problem to be solved",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VehicleRouteSolution.class) // Use the VehicleRouteSolution schema
            )
        )
        @RequestBody @Valid VehicleRouteSolution problem
    ) {

        String planingID = vehicleRoutingService.solve(problem) ;

        return new ResponseEntity<> (
            Map.of(
                "planingID" , planingID
            ),
            HttpStatus.CREATED
        ) ;
    }


    @GetMapping("/{planingID}")
    @Operation(
        summary = "Get vehicle routing solution by job ID",
        description = "Retrieves the vehicle routing solution for a specific job ID. The job ID is generated when a problem is submitted for solving."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solution retrieved successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = VehicleRouteSolution.class) // Use the VehicleRouteSolution schema
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Not found. This could be due to a non-existent job ID.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class) // Use the ErrorResponse schema
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal server error. This could be due to a problem an unexpected error.",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class) // Use the ErrorResponse schema
        )
    )
    public ResponseEntity<VehicleRouteSolution> getRouteSolution(
        @Parameter(
            description = "The ID of the job for which the solution is to be retrieved",
            required = true,
            example = "job123"
        )
        @PathVariable String planingID
    ) throws Exception {

        return new ResponseEntity<> (
            vehicleRoutingService.getRoutePlan(planingID) ,
            HttpStatus.OK
        ) ;
    }



    @PutMapping("/{planingID}")
    public ResponseEntity<Map<String,Object>> updateConstraintsWeight (

        @PathVariable String planingID , 
        @RequestBody List<ConstraintWeightOverrideDto> constraintWeightOverridesDtos
    ) {

        vehicleRoutingService.overWriteConstraintsWeight(planingID, constraintWeightOverridesDtos);

        return new ResponseEntity<>(
            Map.of(
                "message" , "Constraints Weights have just updated"
            ) ,
            HttpStatus.OK
        ) ;
    }

}
