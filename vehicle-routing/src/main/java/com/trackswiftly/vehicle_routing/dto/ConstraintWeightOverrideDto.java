package com.trackswiftly.vehicle_routing.dto;

import com.trackswiftly.vehicle_routing.enums.ConstraintTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstraintWeightOverrideDto {
    
    private ConstraintEnum constraint ;

    private ConstraintTypes type ;

    private Long pnelatyWith ;
}
