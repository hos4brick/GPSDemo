package com.gps.demo.model.event;

import com.gps.demo.model.gps.GpsCoordinate;

import java.util.LinkedList;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMap {
    private LinkedList<EventMapCoordinate> coordinates = new LinkedList<>();

    public void addCoordinate(EventMapCoordinate eventMapCoordinate) {
        coordinates.add(eventMapCoordinate);
    }

    public void addCoordinate(GpsCoordinate gpsCoordinate) {
        coordinates.add(new EventMapCoordinate(gpsCoordinate));
    }
}
