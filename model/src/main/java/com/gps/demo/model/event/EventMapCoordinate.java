package com.gps.demo.model.event;

import java.util.LinkedList;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMapCoordinate {
    private double latitude = 0;
    private double longitude = 0;
    private LinkedList<EventMapTimeColor> colors = new LinkedList<>();

    private EventMapTimeColor lastTimeColor = null;
    private EventMapTimeColor nextTimeColor = null;
}
