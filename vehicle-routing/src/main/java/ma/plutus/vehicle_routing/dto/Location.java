package ma.plutus.vehicle_routing.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Builder
@AllArgsConstructor
@Schema(description = "Represents a geographical location with latitude and longitude.")
public class Location {

    @Schema(description = "The latitude of the location.", example = "40.605994321126936")
    @NotNull(message = "Latitude must not be null")
    private double latitude;

    @Schema(description = "The longitude of the location.", example = "-75.68106859680056")
    @NotNull(message = "Longitude must not be null")
    private double longitude;

    @JsonIgnore
    private Map<Location, Long> drivingTimeSeconds;

    @JsonCreator
    public Location(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Map<Location, Long> getDrivingTimeSeconds() {
        return drivingTimeSeconds;
    }

    /**
     * Set the driving time map (in seconds).
     *
     * @param drivingTimeSeconds a map containing driving time from here to other locations
     */
    public void setDrivingTimeSeconds(Map<Location, Long> drivingTimeSeconds) {
        this.drivingTimeSeconds = drivingTimeSeconds;
    }

    /**
     * Driving time to the given location in seconds.
     *
     * @param location other location
     * @return driving time in seconds
     */
    public long getDrivingTimeTo(Location location) {
        return drivingTimeSeconds.get(location);
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

}