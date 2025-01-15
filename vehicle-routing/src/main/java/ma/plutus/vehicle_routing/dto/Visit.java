package ma.plutus.vehicle_routing.dto;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import ai.timefold.solver.core.api.domain.variable.PreviousElementShadowVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class Visit {
    
    @PlanningId
    private String id;
    private String name;
    private Location location;
    private int demand;
    private Instant minStartTime;
    private Instant maxEndTime;
    private Duration serviceDuration;

    @InverseRelationShadowVariable(sourceVariableName = "visits")
    private Vehicle vehicle;

    @PreviousElementShadowVariable(sourceVariableName = "visits")
    private Visit previousVisit;

    private Instant arrivalTime ;


    private void updateArrivalTime() {
        if (previousVisit == null && vehicle == null) {
            arrivalTime = null;
            return;
        }
        Instant departureTime = previousVisit == null ? vehicle.getDepartureTime() : previousVisit.getDepartureTime();
        arrivalTime = departureTime != null ? departureTime.plusSeconds(getDrivingTimeSecondsFromPreviousStandstill()) : null;
    }




    public long getDrivingTimeSecondsFromPreviousStandstill() {
        if (vehicle == null) {
            throw new IllegalStateException(
                    "This method must not be called when the shadow variables are not initialized yet.");
        }
        if (previousVisit == null) {
            return vehicle.getHomeLocation().getDrivingTimeTo(location);
        }
        return previousVisit.getLocation().getDrivingTimeTo(location);
    }



    public Instant getDepartureTime() {

        if (arrivalTime == null) {
            return null;
        }

        return getStartServiceTime().plus(serviceDuration);
    }



    public Instant getStartServiceTime() {

        if (arrivalTime == null) {
            return null;
        }
        return arrivalTime.isBefore(minStartTime) ? minStartTime : arrivalTime;
    }



    public boolean isServiceFinshedAfterMaxendTime(){
        return arrivalTime != null &&
                arrivalTime.plus(serviceDuration).isAfter(maxEndTime) ;  
    }


    public long getServiceFinishedDelayInMinutes() {
        if (arrivalTime == null) {
            return 0;
        }
        return roundDurationToNextOrEqualMinutes(Duration.between(maxEndTime, arrivalTime.plus(serviceDuration)));
    }



    private static long roundDurationToNextOrEqualMinutes(Duration duration) {
        var remainder = duration.minus(duration.truncatedTo(ChronoUnit.MINUTES));
        var minutes = duration.toMinutes();
        if (remainder.equals(Duration.ZERO)) {
            return minutes;
        }
        return minutes + 1;
    }
}
