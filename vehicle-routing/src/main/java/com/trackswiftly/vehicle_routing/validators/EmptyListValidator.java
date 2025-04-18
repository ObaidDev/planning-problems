package com.trackswiftly.vehicle_routing.validators;

import java.util.List;

import com.trackswiftly.vehicle_routing.annotations.EmptyList;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmptyListValidator implements ConstraintValidator<EmptyList, List<?>>{

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && list.isEmpty();
    }
    
}
