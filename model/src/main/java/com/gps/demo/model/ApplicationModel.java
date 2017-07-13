package com.gps.demo.model;

import com.gps.demo.model.event.EventModel;
import com.gps.demo.model.event.TimeDetectorThread;
import com.wirthless.utilities.OrderedHashTable;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class ApplicationModel {
    private File fileDirectory = null;
    private OrderedHashTable<String, EventModel> events = new OrderedHashTable<>();

    private TimeDetectorThread timeDetectorThread = null;


    public ApplicationModel(File fileDirectory) {
        System.out.println("========== CONSTRUCTOR: " + fileDirectory);

        timeDetectorThread = new TimeDetectorThread();
        timeDetectorThread.start();

        this.fileDirectory = fileDirectory;

        refreshEventFiles();
    }

    public void refreshEventFiles() {
        File[] allFiles = fileDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".csv");
            }
        });

        events = new OrderedHashTable<>();

        for (File eventFile : allFiles) {
            System.out.println("========== FILE: " + eventFile.getAbsolutePath());
            EventModel eventModel = null;
            try {
                eventModel = new EventModel(eventFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            addEvent(eventModel);
        }
    }

    public OrderedHashTable<String, EventModel> getEvents() {
        return new OrderedHashTable<>(events);
    }

    public List<String> getEventNames() {
        List<String> eventNames = new LinkedList<>();

        for (EventModel eventModel : events.getValues()) {
            eventNames.add(eventModel.getEventName());
        }
        return eventNames;
    }

    public void addEvent(EventModel eventModel) {
        events.put(eventModel.getEventKey(), eventModel);
    }

    public EventModel getEvent(int index) {
        return events.getValueAt(index);
    }

    public EventModel getEvent(String key) {
        return events.get(key);
    }
}
