package com.example.ryan.gpsdemo.activities.menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ryan.gpsdemo.R;
import com.example.ryan.gpsdemo.activities.color.GpsDemoActivity;
import com.gps.demo.model.event.EventModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jon on 7/12/2016.
 */
public class MainMenuExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener{
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

        TextView headlineTextView = (TextView) convertView.findViewById(R.id.main_menu_list_group_text_view);
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

        convertView.setTag(R.string.GROUP_POSITION_TAG, groupPosition);
        convertView.setTag(R.string.CHILD_POSITION_TAG, childPosition);
        convertView.setOnClickListener(this);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int groupPosition = (int) v.getTag(R.string.GROUP_POSITION_TAG);
        int childPosition = (int) v.getTag(R.string.CHILD_POSITION_TAG);

        EventModel event = MainMenuActivity.getApplicationModel().getEvent(childPosition);

        Intent intent = new Intent(context, GpsDemoActivity.class);
        intent.putExtra(GpsDemoActivity.EVENT_MODEL_KEY, event.getEventKey());

        context.startActivity(intent);
    }
}
