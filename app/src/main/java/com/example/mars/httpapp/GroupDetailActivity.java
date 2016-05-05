package com.example.mars.httpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
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

public class GroupDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
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
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject group = (JSONObject) response.get(i);

                                //String department = group.getString("department");



                                Log.i("outputmsg", group.getString("user"));
                                Log.i("outputmsg", group.getString("comment"));

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
                        final ListView listView = (ListView) findViewById(R.id.list2);
                        //use list data to populate the listview with studygroups
                        GroupAdapter adapter2 = new GroupAdapter(getApplicationContext(),
                                AppController.getInstance().FullGroupList);
                        // Assign adapter to ListView

                        listView.setAdapter(adapter2);
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
