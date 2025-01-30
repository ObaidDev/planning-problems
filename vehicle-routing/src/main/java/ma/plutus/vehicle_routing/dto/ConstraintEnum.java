package ma.plutus.vehicle_routing.dto;

public enum ConstraintEnum {
    
    
    // Hard Constraints

    /**
     * 
     * vehicleCapacity
     */
    VEHICLE_CAPACITY ,

    /**
     * 
     * serviceFinishedAfterMaxEndTime
     */
    SERVICE_FINISHED_AFTER_MAX_END_TIME ,


    // Soft Constraints
    
    /**
     * 
     * minimizeTravelTime
     */

    MINIMIZE_TRAVEL_TIME
}
