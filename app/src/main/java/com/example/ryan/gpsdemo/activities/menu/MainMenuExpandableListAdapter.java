package com.example.ryan.gpsdemo.activities.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ryan.gpsdemo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jon on 7/12/2016.
 */
public class MainMenuExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context = null;
    private List<String> menuHeadings = null;
    private HashMap<String, List<String>> menuContentsMap = null;

    public MainMenuExpandableListAdapter(Context context, List<String> menuHeadings, HashMap<String, List<String>> menuContentsMap) {
        this.context = context;
        this.menuHeadings = menuHeadings;
        this.menuContentsMap = menuContentsMap;
    }

    @Override
    public int getGroupCount() {
        return menuHeadings.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menuContentsMap.get(menuHeadings.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menuContentsMap.get(menuHeadings.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menuContentsMap.get(menuHeadings.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.main_menu_list_group, null);
        }

        TextView headlineTextView = (TextView) convertView.findViewById(R.id.mainMenuListGroupTextView);
        headlineTextView.setText(menuHeadings.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.main_menu_list_item, null);
        }

        String childText = getChild(groupPosition, childPosition).toString();

        TextView childTextView = (TextView) convertView.findViewById(R.id.mainMenuListItemTextView);
        childTextView.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
