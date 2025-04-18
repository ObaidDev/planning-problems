package com.trackswiftly.vehicle_routing.dto;

public record Job(VehicleRouteSolution routePlan, Throwable exception) {


    public static Job ofRoutePlan(VehicleRouteSolution routePlan) {
        return new Job(routePlan, null);
    }

    public static Job ofException(Throwable exception) {
        return new Job(null, exception);
    }


    public VehicleRouteSolution getVehicleRouteSolution() {

        return routePlan ;
    }
}
