package com.gps.demo.model.event.node;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMapTimeColor {
    private int nodeId = -1;
    private long time = 0;
    private String colorHexCode = "";
    private EventMapTimeColor nextTimeColor = null;

    public EventMapTimeColor(int nodeId, long time, String colorHexCode) {
        this.nodeId = nodeId;
        this.time = time;
        this.colorHexCode = colorHexCode;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return String.valueOf(nodeId);
    }

    public long getTime() {
        return time;
    }

    public String getColorHexCode() {
        return colorHexCode;
    }

    public EventMapTimeColor getNextTimeColor() {
        return nextTimeColor;
    }

    public void setNextTimeColor(EventMapTimeColor nextTimeColor) {
        this.nextTimeColor = nextTimeColor;
    }

    public long timeDisplayed() {
        long timeDisplayed = 1;

        if (null != nextTimeColor) {
            timeDisplayed = nextTimeColor.getTime() - getTime();
            timeDisplayed = timeDisplayed <= 0 ? 1 : timeDisplayed;
        }


        return timeDisplayed;
    }
}
