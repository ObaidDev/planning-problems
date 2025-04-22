package com.trackswiftly.vehicle_routing.utils;

import com.trackswiftly.vehicle_routing.dto.Location;
import com.trackswiftly.vehicle_routing.dto.Visit;

import ai.timefold.solver.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class VisitDistanceMeter implements NearbyDistanceMeter<Visit , Visit>{



    private static final int EARTH_RADIUS_IN_M = 6371000;
    private static final int TWICE_EARTH_RADIUS_IN_M = 2 * EARTH_RADIUS_IN_M;

    @Override
    public double getNearbyDistance(Visit origin, Visit destination) {
        


        CartesianCoordinate originCoordinate = locationToCartesian(origin.getLocation());
        CartesianCoordinate destinationCoordinate= locationToCartesian(destination.getLocation());


        return calculateDistance(originCoordinate, destinationCoordinate);
    }
    





    private long calculateDistance(CartesianCoordinate from, CartesianCoordinate to) {
        if (from.equals(to)) {
            return 0L;
        }

        double dX = from.x - to.x;
        double dY = from.y - to.y;
        double dZ = from.z - to.z;
        double r = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));
        return Math.round(TWICE_EARTH_RADIUS_IN_M * Math.asin(r));
    }



    private CartesianCoordinate locationToCartesian(Location location) {
        double latitudeInRads = Math.toRadians(location.getLatitude());
        double longitudeInRads = Math.toRadians(location.getLongitude());
        // Cartesian coordinates, normalized for a sphere of diameter 1.0
        double cartesianX = 0.5 * Math.cos(latitudeInRads) * Math.sin(longitudeInRads);
        double cartesianY = 0.5 * Math.cos(latitudeInRads) * Math.cos(longitudeInRads);
        double cartesianZ = 0.5 * Math.sin(latitudeInRads);
        return new CartesianCoordinate(cartesianX, cartesianY, cartesianZ);
    }




    private record CartesianCoordinate(double x, double y, double z) {

    }
}
