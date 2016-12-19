package com.gps.demo.model.event;

import callback.ViewCallBackInterface;
import callback.ViewCallBackInterface.CallBackCommand;

/**
 * Created by Jon on 12/14/2016.
 */

public class EventThread extends Thread {

    private EventModel eventModel = null;
    private ViewCallBackInterface callBackInterface = null;
    private boolean eventInProgress = true;
    private Object waiter = new Object();

    public EventThread(EventModel eventModel, ViewCallBackInterface callBackInterface) {
        this.eventModel = eventModel;
        this.callBackInterface = callBackInterface;
    }

    @Override
    public void run() {

        while (eventInProgress) {

            EventMapTimeColor emtc = eventModel.getEventMapTimeColor();
            String hexColor = emtc.getColorHexCode();

            // send the hex code
            this.callBackInterface.update(CallBackCommand.COLOR_UPDATE);

            if (emtc.getNextTimeColor() != null) {
                long delay = emtc.getTime() * 1000;

                try {
                    synchronized (waiter) {
                        waiter.wait(delay);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public void setEventInProgress(boolean eventInProgress) {
        this.eventInProgress = eventInProgress;
        // if the event is no longer in progress
        if (!this.eventInProgress) {
            // wake the waiter
            this.update();
        }
    }

    public void update() {
        synchronized (waiter) {
            this.waiter.notify();
        }
    }
}
