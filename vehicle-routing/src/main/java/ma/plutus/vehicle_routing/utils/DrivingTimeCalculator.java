package ma.plutus.vehicle_routing.utils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ma.plutus.vehicle_routing.dto.Location;

public interface DrivingTimeCalculator {

    long calculateDrivingTime(Location from, Location to);



    default Map<Location, Map<Location, Long>> calculateBulkDrivingTime(
            Collection<Location> fromLocations,
            Collection<Location> toLocations) {
        return fromLocations.stream().collect(Collectors.toMap(
                Function.identity(),
                from -> toLocations.stream().collect(Collectors.toMap(
                        Function.identity(),
                        to -> calculateDrivingTime(from, to)))));
    }


    default void initDrivingTimeMaps(Collection<Location> locations) {
        Map<Location, Map<Location, Long>> drivingTimeMatrix = calculateBulkDrivingTime(locations, locations);
        locations.forEach(location -> location.setDrivingTimeSeconds(drivingTimeMatrix.get(location)));
    }
}