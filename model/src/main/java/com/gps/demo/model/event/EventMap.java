package com.gps.demo.model.event;

import com.gps.demo.model.gps.GpsCoordinate;
import com.gps.demo.model.event.node.EventMapNode;
import com.gps.demo.model.event.node.EventMapTimeColor;

import java.util.LinkedList;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMap {
    private LinkedList<EventMapNode> eventMapNodes = new LinkedList<>();

    public void addNode(EventMapNode eventMapNode) {
        eventMapNodes.add(eventMapNode);
    }

    public void addNode(GpsCoordinate gpsCoordinate) {
        eventMapNodes.add(new EventMapNode(gpsCoordinate));
    }

    public int getNumberOfNodes() {
        return eventMapNodes.size();
    }

    public void addTimeColor(int index, long time, String color) {
        EventMapNode mapNode = eventMapNodes.get(index);
        EventMapTimeColor timeColor = new EventMapTimeColor(mapNode.getNodeId(), time, color);

        eventMapNodes.get(index).addTimeColor(timeColor);
    }

    public void addTimeColor(int index, String time, String color) {
        Long dTime = Long.parseLong(time);
        addTimeColor(index, dTime, color);
    }

    public String getHexColor(double latitude, double longitude, long timeElapsed) {
        String hexColor = getEventMapTimeColor(latitude, longitude, timeElapsed).getColorHexCode();
        return hexColor;
    }

    public EventMapTimeColor getEventMapTimeColor(double latitude, double longitude, long timeElapsed) {
        EventMapNode closestNode = getClosestNode(latitude, longitude);

        return closestNode.getEventMapTimeColorAtTimeElapsed(timeElapsed);
    }

    public EventMapNode getClosestNode(double latitude, double longitude) {
        double closestDistance = Double.MAX_VALUE;
        EventMapNode closestNode = null;

        GpsCoordinate gpsCoordinate = new GpsCoordinate(latitude, longitude);

        for (EventMapNode mapNode : eventMapNodes) {
            double distance = gpsCoordinate.getDistanceTo(mapNode.getGpsCoordinate());

            if (distance < closestDistance) {
                closestDistance = distance;
                closestNode = mapNode;
            }
        }

        return closestNode;
    }
}
