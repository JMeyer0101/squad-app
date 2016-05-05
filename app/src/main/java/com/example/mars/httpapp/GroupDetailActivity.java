package com.example.mars.httpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class GroupDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        StudyGroup currentGroup = (StudyGroup) getIntent().getParcelableExtra("selectedGroup");
        //debug log group description
        Log.d("data", currentGroup.description);

        TextView classTextView = (TextView) findViewById(R.id.classTextView);
        TextView classNumberTextView = (TextView) findViewById(R.id.classNumberTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        TextView dtextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);

        classTextView.setText(currentGroup.department);
        classNumberTextView.setText("" +currentGroup.classnumber);
        dateTextView.setText(currentGroup.date);
        dtextView.setText(currentGroup.description);
        timeTextView.setText(currentGroup.time);
    }

}
