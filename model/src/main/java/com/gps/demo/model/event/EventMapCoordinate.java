package com.gps.demo.model.event;

import com.gps.demo.model.gps.GpsCoordinate;

import java.util.LinkedList;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMapCoordinate {
    private GpsCoordinate coordinate = null;
    private LinkedList<EventMapTimeColor> colors = new LinkedList<>();

    private EventMapTimeColor lastTimeColor = null;
    private EventMapTimeColor nextTimeColor = null;

    public EventMapCoordinate(GpsCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void addTimeColor(EventMapTimeColor timeColor) {
        colors.add(timeColor);
    }
}
