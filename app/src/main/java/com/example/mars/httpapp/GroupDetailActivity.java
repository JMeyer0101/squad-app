package com.example.mars.httpapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();
        final StudyGroup currentGroup = (StudyGroup) getIntent().getParcelableExtra("selectedGroup");
        for (Iterator<StudyGroup> iterator = AppController.getInstance().AppUserGroups.iterator(); iterator.hasNext(); ) {
            StudyGroup sg = iterator.next();
            if(sg.id == currentGroup.id){
                try {
                    makeCommentRequest(currentGroup.id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        final int groupID = currentGroup.id;
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

        final Button joinbutton = (Button) findViewById(R.id.joingroupbutton);
        final Button leavebutton = (Button) findViewById(R.id.leavegroupbutton);
        final Button commentbutton = (Button) findViewById(R.id.sendcommentbutton);

        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makeJoinRequest(groupID);
                    AppController.getInstance().AppUserGroups.add(currentGroup);
                    //joinbutton.setVisibility(View.GONE);
                    //leavebutton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
        leavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<StudyGroup> temp = AppController.getInstance().AppUserGroups;
                try {
                    makeLeaveRequest(groupID);
                    for (Iterator<StudyGroup> iterator = AppController.getInstance().AppUserGroups.iterator(); iterator.hasNext(); ) {
                        StudyGroup sg = iterator.next();
                        if(sg.id == currentGroup.id){
                            iterator.remove();
                        }
                    }
                    AppController.getInstance().FullGroupList.add(currentGroup);

                }catch(JSONException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postMessageRequest(groupID);
                    //AppController.getInstance().AppUserGroups.add(currentGroup);
                    //joinbutton.setVisibility(View.GONE);
                    //leavebutton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    makeCommentRequest(groupID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //show/hide join or leave buttons as necessary
        Boolean joined = false;
        for(StudyGroup object :AppController.getInstance().AppUserGroups) {
            if(object.id == currentGroup.id) {
                joinbutton.setVisibility(View.GONE);
                joined = true;
                break;
            }
        }
        if(!joined){
            leavebutton.setVisibility(View.GONE);
            RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
            rlayout.setVisibility(View.GONE);
        }



    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private String urlJsonJoin = "https://studygroupformer.herokuapp.com/studygroups_users";

    /**
     * JOIN GROUP METHOD {
     *
     * @param groupID*/
    private void makeJoinRequest(int groupID) throws JSONException {

        showpDialog();
        final JSONObject studygroups_user = new JSONObject();
        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("studygroup_id", groupID);
        _jsonOBJ.put("user_id", AppController.getInstance().AppUser.id);
        try {
            studygroups_user.put("studygroups_user", _jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                urlJsonJoin, studygroups_user, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response:", response.toString());


                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    //end join


    private String urlJsonLeave = "https://studygroupformer.herokuapp.com/seekdestroy";

    /**
     * LEAVE GROUP METHOD {
     *
     * @param groupID*/
    private void makeLeaveRequest(int groupID) throws JSONException {

        showpDialog();
        final JSONObject studygroups_user = new JSONObject();
        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("studygroup_id", groupID);
        _jsonOBJ.put("user_id", AppController.getInstance().AppUser.id);
        try {
            studygroups_user.put("studygroups_user", _jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                urlJsonLeave, studygroups_user, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response:", response.toString());


                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("Error: ", error.getMessage());
                //Toast.makeText(getApplicationContext(),
                    //    error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    //end getjsonobj


    private String urlJsonChats = "https://studygroupformer.herokuapp.com/groupchats";

    /**
     * GET COMMENTS METHOD [
     * */
    private void makeCommentRequest(int groupID) throws JSONException{

        showpDialog();
        final JSONObject groupchats = new JSONObject();
        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("studygroup_id", groupID);
        //_jsonOBJ.put("studygroup_id", groupID;
        try {
            groupchats.put("group_comment", _jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final TextView commentsview = (TextView) findViewById(R.id.commentsview);
        //AppController.getInstance().FullGroupList.clear();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, urlJsonChats, groupchats,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response:", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            //jsonResponse = "";
                            commentsview.setText(null);
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject group = (JSONObject) response.get(i);

                                //String department = group.getString("department");


                                commentsview.append(group.getString("user") + ": " + group.getString("comment") + "\n");
                               // Log.i("outputmsg", group.getString("user"));
                               // Log.i("outputmsg", group.getString("comment"));

                                //jsonResponse += "Mobile: " + mobile + "\n\n\n";


                                //appcontroller logic
                                // AppController.getInstance().FullGroupList.add(new StudyGroup(id, department, classno, time, description, date));

                            }

//                            txtResponse.setText(jsonResponse);
                            //AppController.getInstance().grouplist = jsonResponse;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                        //final ListView listView = (ListView) findViewById(R.id.list2);
                        //use list data to populate the listview with studygroups
                        //GroupAdapter adapter2 = new GroupAdapter(getApplicationContext(),
                        //        AppController.getInstance().FullGroupList);
                        // Assign adapter to ListView

                     //   listView.setAdapter(adapter2);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private String urlPostMSG = "https://studygroupformer.herokuapp.com/group_comments";

    /**
     * POST COMMENT METHOD [
     * */
    private void postMessageRequest(final int groupID) throws JSONException{
        final TextView comment = (TextView) findViewById(R.id.sendcomment);
        showpDialog();
        final JSONObject message = new JSONObject();
        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("studygroup_id", groupID);
        _jsonOBJ.put("comment", comment.getText().toString());
        _jsonOBJ.put("user", AppController.getInstance().AppUser.username);

        //_jsonOBJ.put("studygroup_id", groupID;
        try {
            message.put("group_comment", _jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final TextView commentsview = (TextView) findViewById(R.id.commentsview);
        //AppController.getInstance().FullGroupList.clear();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urlPostMSG, message,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d("response:", response.toString());
                        comment.setText("");





                                //String department = group.getString("department");


                        // commentsview.append(AppController.getInstance().AppUser.username + ": " + comment.getText().toString() + "\n");
                                // Log.i("outputmsg", group.getString("user"));
                                // Log.i("outputmsg", group.getString("comment"));

                                //jsonResponse += "Mobile: " + mobile + "\n\n\n";


                                //appcontroller logic
                                // AppController.getInstance().FullGroupList.add(new StudyGroup(id, department, classno, time, description, date));

                            //}

//                            txtResponse.setText(jsonResponse);
                            //AppController.getInstance().grouplist = jsonResponse;



                        hidepDialog();
                        //final ListView listView = (ListView) findViewById(R.id.list2);
                        //use list data to populate the listview with studygroups
                        //GroupAdapter adapter2 = new GroupAdapter(getApplicationContext(),
                        //        AppController.getInstance().FullGroupList);
                        // Assign adapter to ListView

                        //   listView.setAdapter(adapter2);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

}
