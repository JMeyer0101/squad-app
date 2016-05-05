package com.example.mars.httpapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mars on 2/16/16.
 */
public class ListAdapter extends ArrayAdapter<HashMap<String,String>> {
    private ArrayList<HashMap<String,String>> studygroups;

    public ListAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String,String>> studygroups) {
        super(context, textViewResourceId, studygroups);
        this.studygroups = studygroups;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }
        HashMap<String,String> group = studygroups.get(position);
        if (group != null) {
            TextView classname = (TextView) v.findViewById(R.id.classname);
            TextView date = (TextView) v.findViewById(R.id.date);
            if (classname != null) {
                classname.setText(group.get("department"));
            }
            if(date != null) {
                date.setText("date: " + group.get("date") );
            }
        }
        return v;
    }
}
