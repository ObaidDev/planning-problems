package ma.plutus.vehicle_routing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.plutus.vehicle_routing.enums.ConstraintTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstraintWeightOverrideDto {
    
    private String constraintName ;

    private ConstraintTypes type ;

    private Long pnelatyWith ;
}
