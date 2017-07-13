package com.gps.demo.model.event.node;

import com.gps.demo.model.gps.GpsCoordinate;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMapNode {
    private static int nodeIdRoot = 0;
    private int nodeId = -1;
    private GpsCoordinate gpsCoordinate = null;
    private TreeMap<Long, EventMapTimeColor> colors = new TreeMap();

    private int currentIndex = 0;

    private EventMapTimeColor currentTimeColor = null;
    private EventMapTimeColor nextTimeColor = null;

    public EventMapNode(GpsCoordinate gpsCoordinate) {
        this.nodeId = nodeIdRoot++;
        this.gpsCoordinate = gpsCoordinate;
    }

    public void addTimeColor(EventMapTimeColor timeColor) {
        if (!colors.isEmpty()) {
            // loop setting the next time color to the first
            timeColor.setNextTimeColor(getFirstTimeColor());
            // update the current end of the list to point to the new node
            getLastTimeColor().setNextTimeColor(timeColor);
        } else {
            // if it's the only entry, loop to itself
            timeColor.setNextTimeColor(timeColor);
        }
        // add this time color to the list
        colors.put(timeColor.getTime(), timeColor);

        if (currentTimeColor == null) {
            currentTimeColor = timeColor;
        } else if (nextTimeColor == null) {
            nextTimeColor = timeColor;
        }
    }

    public double getDistanceTo(EventMapNode eventMapNode) {
        return getDistanceTo(eventMapNode.getGpsCoordinate());
    }

    public double getDistanceTo(GpsCoordinate gpsCoordinate) {
        return this.gpsCoordinate.getDistanceTo(gpsCoordinate);
    }

    public GpsCoordinate getGpsCoordinate() {
        return gpsCoordinate;
    }

    public String getColorAtTimeElapsed(long timeElapsed) {
        return getEventMapTimeColorAtTimeElapsed(timeElapsed).getColorHexCode();
    }

    public EventMapTimeColor getEventMapTimeColorAtTimeElapsed(long timeElapsed) {
        Map.Entry<Long, EventMapTimeColor> floorEntry = colors.floorEntry(timeElapsed);
        EventMapTimeColor floorTimeColor = floorEntry.getValue();

        return floorTimeColor;
    }

    public EventMapTimeColor getFirstTimeColor() {
        Map.Entry<Long, EventMapTimeColor> firstEntry = colors.firstEntry();

        if (firstEntry == null) {
            return null;
        }

        return firstEntry.getValue();
    }

    public EventMapTimeColor getLastTimeColor() {
        Map.Entry<Long, EventMapTimeColor> firstEntry = colors.lastEntry();

        if (firstEntry == null) {
            return null;
        }

        return firstEntry.getValue();
    }

    public int getNodeId() {
        return nodeId;
    }
}
