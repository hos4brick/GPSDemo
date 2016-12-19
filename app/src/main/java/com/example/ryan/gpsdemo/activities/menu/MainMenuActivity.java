package com.example.ryan.gpsdemo.activities.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.ryan.gpsdemo.R;
import com.gps.demo.model.ApplicationModel;
import com.gps.demo.model.event.EventModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {
    String MAIN_MENU_HEADING_PROFILE = "PROFILE";
    String MAIN_MENU_HEADING_MY_EVENTS = "MY EVENTS";
    String MAIN_MENU_HEADING_FIND_EVENTS = "FIND EVENTS";
    String MAIN_MENU_HEADING_SETTINGS = "SETTINGS";

    private static ApplicationModel applicationModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        System.out.println("========== applicationModel: ");
        applicationModel = new ApplicationModel(getFilesDir());

        EventModel jonHouseEvent = new EventModel(new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.jons_house))));
        applicationModel.addEvent(jonHouseEvent);

        List<String> menuHeadings = new LinkedList<>();
        HashMap<String, List<String>> menuContentsMap = new HashMap<>();

        menuHeadings.add(MAIN_MENU_HEADING_PROFILE);
        menuContentsMap.put(MAIN_MENU_HEADING_PROFILE, new LinkedList<String>());
        menuHeadings.add(MAIN_MENU_HEADING_MY_EVENTS);
        ApplicationModel applicationModel = getApplicationModel();
        menuContentsMap.put(MAIN_MENU_HEADING_MY_EVENTS, applicationModel.getEventNames());
        menuHeadings.add(MAIN_MENU_HEADING_FIND_EVENTS);
        menuContentsMap.put(MAIN_MENU_HEADING_FIND_EVENTS, new LinkedList<String>());
        menuHeadings.add(MAIN_MENU_HEADING_SETTINGS);
        menuContentsMap.put(MAIN_MENU_HEADING_SETTINGS, new LinkedList<String>());



        ExpandableListAdapter listAdapter = new MainMenuExpandableListAdapter(this, menuHeadings, menuContentsMap);

        ExpandableListView mainMenuListView = (ExpandableListView) findViewById(R.id.mainMenuExpandableList);
        mainMenuListView.setAdapter(listAdapter);
    }


    public static ApplicationModel getApplicationModel() {
        return applicationModel;
    }
}
