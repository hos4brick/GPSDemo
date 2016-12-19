package com.example.ryan.gpsdemo.activities.color;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import callback.ViewCallBackInterface;

/**
 * Created by Jon on 12/16/2016.
 */

public class GpsDemoBroadcastReceiver extends BroadcastReceiver {
    private GpsDemoActivity gpsDemoActivity = null;
    public GpsDemoBroadcastReceiver(GpsDemoActivity gpsDemoActivity) {
        this.gpsDemoActivity = gpsDemoActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        ViewCallBackInterface.CallBackCommand command = ViewCallBackInterface.CallBackCommand.valueOf(action);

        switch (command) {
            case COLOR_UPDATE:
                gpsDemoActivity.locationUpdate();
                break;
        }
    }
}
