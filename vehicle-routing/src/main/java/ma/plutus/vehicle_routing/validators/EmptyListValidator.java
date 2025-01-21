package ma.plutus.vehicle_routing.validators;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ma.plutus.vehicle_routing.annotations.EmptyList;

public class EmptyListValidator implements ConstraintValidator<EmptyList, List<?>>{

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && list.isEmpty();
    }
    
}
