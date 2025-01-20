package ma.plutus.vehicle_routing.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutePlanNotFoundException extends RuntimeException{
    private final int code = 0;


    public RoutePlanNotFoundException(String message) {
        super(message);
    }
}
