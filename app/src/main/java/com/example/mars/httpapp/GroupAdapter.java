package com.example.mars.httpapp;

/**
 * Created by Mars on 3/23/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;




/**
 * Created by Mars on 2/18/16.
 */
public class GroupAdapter extends BaseAdapter {

    private static final String LOG_TAG = GroupAdapter.class.getSimpleName();

    private Context context_;
    private HashMap<String, Drawable> quizitems;
    ArrayList<StudyGroup> groupslist = new ArrayList<StudyGroup>();
    //ArrayList<Drawable> icons = new ArrayList<Drawable>();

    public GroupAdapter(Context context, ArrayList<StudyGroup> groupsarray) {
        this.context_ = context;
        this.groupslist = groupsarray;
        //for(Drawable icon : quizitems.values()) {
          //  icons.add(icon);
        //}
    }

    @Override
    public int getCount() {
        return groupslist.size();
    }

    @Override
    public StudyGroup getItem(int position) {
        return groupslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context_.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //use group_list_item.xml
            convertView = mInflater.inflate(R.layout.group_list_item, null);

        }

        TextView groupdept = (TextView) convertView.findViewById(R.id.GroupDept);
        TextView groupclassno = (TextView) convertView.findViewById(R.id.GroupClassNo);
        //ImageView questionicon = (ImageView) convertView.findViewById(R.id.questionicon);


        String dept = groupslist.get(position).department;
        String classno = String.valueOf(groupslist.get(position).classnumber);
        //Drawable icon = icons.get(position);




       groupdept.setText(dept);
        groupclassno.setText(classno);
        //questionicon.setImageDrawable(icon);

        return convertView;
    }
}
