package com.example.ryan.gpsdemo.activities.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.ryan.gpsdemo.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {
    String MAIN_MENU_HEADING_PROFILE = "PROFILE";
    String MAIN_MENU_HEADING_MY_EVENTS = "MY EVENTS";
    String MAIN_MENU_HEADING_FIND_EVENTS = "FIND EVENTS";
    String MAIN_MENU_HEADING_SETTINGS = "SETTINGS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        List<String> menuHeadings = new LinkedList<>();
        HashMap<String, List<String>> menuContentsMap = new HashMap<>();

        menuHeadings.add(MAIN_MENU_HEADING_PROFILE);
        menuContentsMap.put(MAIN_MENU_HEADING_PROFILE, new LinkedList<String>());
        menuHeadings.add(MAIN_MENU_HEADING_MY_EVENTS);
        menuContentsMap.put(MAIN_MENU_HEADING_MY_EVENTS, new LinkedList<String>());
        menuHeadings.add(MAIN_MENU_HEADING_FIND_EVENTS);
        menuContentsMap.put(MAIN_MENU_HEADING_FIND_EVENTS, new LinkedList<String>());
        menuHeadings.add(MAIN_MENU_HEADING_SETTINGS);
        menuContentsMap.put(MAIN_MENU_HEADING_SETTINGS, new LinkedList<String>());



        ExpandableListAdapter listAdapter = new MainMenuExpandableListAdapter(this, menuHeadings, menuContentsMap);

        ExpandableListView mainMenuListView = (ExpandableListView) findViewById(R.id.mainMenuExpandableList);
        mainMenuListView.setAdapter(listAdapter);
    }

}
