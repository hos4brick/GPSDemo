package com.gps.demo.model.event;

import com.gps.demo.model.gps.GpsCoordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import callback.ViewCallBackInterface;

/**
 * Created by Jon on 7/11/2016.
 */
public class EventModel {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssz", Locale.US);
    private static final String BOOLEAN_TRUE_STRING_INT = "1";

    private EventThread eventThread = null;

    private String eventName = "";
    private Date eventDate = null;
    private EventMap eventMap = null;

    private double latitude = 0;
    private double longitude = 0;

    private boolean loop = true;
    private long eventLength = 0;

    public EventModel(File eventFile) throws FileNotFoundException {
        this(new BufferedReader(new FileReader(eventFile)));
    }

    public EventModel(BufferedReader eventModelBufferedReader) {

        try {
            String eventMetaDataLine = eventModelBufferedReader.readLine();
            if (null == eventMetaDataLine) {
                throw new NullPointerException("Missing eventMetaDataLine");
            }
            String[] eventInfo = eventMetaDataLine.split(",");
            eventName = eventInfo[0];
            eventDate = dateFormat.parse(eventInfo[1]);
            if (eventInfo.length >= 3)
                loop = BOOLEAN_TRUE_STRING_INT.equals(eventInfo[2]);

            String gpsLocationsLine = eventModelBufferedReader.readLine();
            if (null == gpsLocationsLine) {
                throw new NullPointerException("Missing gpsLocationsLine");
            }

            eventMap = new EventMap();

            // todo write/find a csv library
            String[] gpsLocations = gpsLocationsLine.split(",");
            if ((gpsLocations.length == 0) || (gpsLocations.length % 2 != 0)) {
                throw new Exception("Malformed GPS Location row. Length: " + gpsLocations.length);
            }

            // todo add altitude
            // for each pair of coordinates
            for (int i = 0; i < gpsLocations.length; i = i + 2) {
                double latitude = Double.parseDouble(gpsLocations[i]);
                double longitude = Double.parseDouble(gpsLocations[i + 1]);
                GpsCoordinate gpsCoordinate = new GpsCoordinate(latitude, longitude);
                eventMap.addNode(gpsCoordinate);
            }

            // read in the rest of the lines defining color over time
            String timeLine;
            while ((timeLine = eventModelBufferedReader.readLine()) != null) {
                String[] colors = timeLine.split(",");

                if (colors.length != (eventMap.getNumberOfNodes() + 1)) {
                    throw new Exception("Malformed GPS Color row. Length: " + colors.length);
                }

                String time = colors[0];
                eventLength = Long.parseLong(time);

                for (int i = 1; i < colors.length; i++) {
                    eventMap.addTimeColor(i - 1, time, colors[i]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEventKey() {
        return eventName + dateFormat.format(eventDate);
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getHexColor() {
        return getEventMapTimeColor().getColorHexCode();
    }

    public String getHexColor(double latitude, double longitude) {
        this.setLatLong(latitude, longitude);

        return this.getHexColor();
    }

    private long getCurrentTimeIntoEvent() {
        long startTime = eventDate.getTime();
        long currentTime = (new Date()).getTime();

        long timeIntoEvent = (currentTime - startTime) / 1000;

        if (loop) {
            timeIntoEvent %= eventLength;
        }

        // if the event hasn't started yet return 0, otherwise return the time into the event
        return timeIntoEvent > 0 ? timeIntoEvent : 0;
    }

    public EventMapTimeColor getEventMapTimeColor() {
        long currentTimeIntoEvent = getCurrentTimeIntoEvent();
        EventMapTimeColor emtc = eventMap.getEventMapTimeColor(this.getLatitude(), this.getLongitude(), currentTimeIntoEvent);
        return emtc;
    }

    private synchronized void setLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public synchronized double getLatitude() {
        return latitude;
    }

    public synchronized double getLongitude() {
        return longitude;
    }

    public void start(ViewCallBackInterface callBackInterface) {
        eventThread = new EventThread(this, callBackInterface);

        eventThread.start();

        // TODO need some logic here to wait for the real start time
    }
}
