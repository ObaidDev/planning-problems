package com.trackswiftly.vehicle_routing.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_routing.dto.ConstraintWeightOverrideDto;
import com.trackswiftly.vehicle_routing.enums.ConstraintTypes;

import ai.timefold.solver.core.api.domain.solution.ConstraintWeightOverrides;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

@Component
public class ConstraintsWeightMapper {




    public ConstraintWeightOverrides<HardSoftLongScore> toConstraintWeightOverrides(List<ConstraintWeightOverrideDto> constraintWeights) {

        if (constraintWeights == null || constraintWeights.isEmpty()) {

            Map<String , HardSoftLongScore> initOverwightsMap = Map.of() ;
            
            return ConstraintWeightOverrides.of(initOverwightsMap) ;
        }

        Map<String, HardSoftLongScore> constraintsMap = constraintWeights.stream()
        .collect(Collectors.toMap(
            constratin -> constratin.getConstraint().toString() ,
            constraintWeight -> {

                if (constraintWeight.getType() == ConstraintTypes.SOFT) {
                    return HardSoftLongScore.ofSoft(constraintWeight.getPnelatyWith());
                } else {
                    return HardSoftLongScore.ofHard(constraintWeight.getPnelatyWith());
                }
            }
        ));

        return ConstraintWeightOverrides.of(constraintsMap);
        
    }
    
}
