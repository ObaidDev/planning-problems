package com.trackswiftly.vehicle_routing.constraints;


import org.jspecify.annotations.NonNull;

import com.trackswiftly.vehicle_routing.dto.ConstraintEnum;
import com.trackswiftly.vehicle_routing.dto.Vehicle;
import com.trackswiftly.vehicle_routing.dto.Visit;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class VehicleConstraintProvider implements ConstraintProvider{


    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory factory) {

        Constraint[] constraints =  new Constraint[] {
            vehicleCapacity(factory),
            serviceFinishedAfterMaxEndTime(factory),
            minimizeTravelTime(factory)
        };

        log.debug("constraints : {}" , constraints);

        return constraints ;
    }




    protected Constraint vehicleCapacity(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .filter(vehicle -> vehicle.getTotalDemand() > vehicle.getCapacity())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        vehicle -> vehicle.getTotalDemand() - vehicle.getCapacity())
                .asConstraint(ConstraintEnum.VEHICLE_CAPACITY.toString());
    }

    protected Constraint serviceFinishedAfterMaxEndTime(ConstraintFactory factory) {
        return factory.forEach(Visit.class)
                .filter(Visit::isServiceFinishedAfterMaxEndTime)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        Visit::getServiceFinishedDelayInMinutes)
                .asConstraint(ConstraintEnum.SERVICE_FINISHED_AFTER_MAX_END_TIME.toString());
    }




    protected Constraint minimizeTravelTime(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        Vehicle::getTotalDrivingTimeSeconds)
                .asConstraint(ConstraintEnum.MINIMIZE_TRAVEL_TIME.toString());
    }
    
}
