package com.gps.demo.model.event;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventMapTimeColor {
    private long time = 0;
    private String colorHexCode = "";
    private EventMapTimeColor nextTimeColor = null;

    public EventMapTimeColor(long time, String colorHexCode) {
        this.time = time;
        this.colorHexCode = colorHexCode;
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
}
