package ma.plutus.vehicle_routing.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    
    private static final double EARTH_RADIUS = 6371;

    private double latitude;
    private double longitude;

    private Map<Location, Long> drivingTimeSeconds;





    /***
     * calculate distance between tow locations .
     */
    public double getDistanceTo(Location location2) {
        
        double dLat = location2.getLatitude() - this.getLatitude();
        double dLon = location2.getLongitude() - this.getLongitude();

        // Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(this.getLatitude()) * Math.cos(location2.getLatitude()) *
                   Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }


    public long getDrivingTimeTo(Location location) {
        return drivingTimeSeconds.get(location);
    }
}
