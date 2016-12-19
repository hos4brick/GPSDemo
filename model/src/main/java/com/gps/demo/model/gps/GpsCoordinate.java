package com.gps.demo.model.gps;

/**
 * Created by Jon on 7/20/2016.
 */
public class GpsCoordinate {
    private double latitude = 0;
    private double longitude = 0;

    public GpsCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistanceTo(GpsCoordinate gpsCoordinate) {
        double latitudeDistance = latitude - gpsCoordinate.getLatitude();
        double longitudeDistance = longitude - gpsCoordinate.getLongitude();

        double distance = Math.sqrt(Math.pow(latitudeDistance, 2) + Math.pow(longitudeDistance, 2));

        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
