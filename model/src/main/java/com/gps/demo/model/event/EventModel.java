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

/**
 * Created by Jon on 7/11/2016.
 */
public class EventModel {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    private String eventName = "";
    private Date eventDate = null;
    private EventMap eventMap = null;

    public EventModel(File eventFile) {

        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(eventFile));

            String eventMetaDataLine = buffReader.readLine();
            if (null == eventMetaDataLine) {
                throw new NullPointerException("Missing eventMetaDataLine");
            }
            String[] eventInfo = eventMetaDataLine.split(",");
            eventName = eventInfo[0];
            eventDate = dateFormat.parse(eventInfo[1]);

            String gpsLocationsLine = buffReader.readLine();
            if (null == gpsLocationsLine) {
                throw new NullPointerException("Missing gpsLocationsLine");
            }

            eventMap = new EventMap();

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
                eventMap.addCoordinate(gpsCoordinate);
            }

            // read in the rest of the lines defining color over time
            String timeLine;
            while ((timeLine = buffReader.readLine()) != null) {

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
}
