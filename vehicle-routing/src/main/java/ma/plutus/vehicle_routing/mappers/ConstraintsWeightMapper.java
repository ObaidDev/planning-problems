package ma.plutus.vehicle_routing.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ai.timefold.solver.core.api.domain.solution.ConstraintWeightOverrides;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ma.plutus.vehicle_routing.dto.ConstraintWeightOverrideDto;
import ma.plutus.vehicle_routing.enums.ConstraintTypes;

@Component
public class ConstraintsWeightMapper {




    public ConstraintWeightOverrides<HardSoftLongScore> toConstraintWeightOverrides(List<ConstraintWeightOverrideDto> constraintWeights) {

        if (constraintWeights == null || constraintWeights.isEmpty()) {
            return ConstraintWeightOverrides.of(Map.of()) ;
        }

        Map<String, HardSoftLongScore> constraintsMap = constraintWeights.stream()
        .collect(Collectors.toMap(
            ConstraintWeightOverrideDto::getConstraintName,
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
