package com.example.mars.httpapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MakeAccountActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Button submitbutton = (Button) findViewById(R.id.createaccount);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makeAccountRequest();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });


    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void foobar(){
        Intent intent = new Intent();
        intent.putExtra("email", "pendingEMAIL");
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText passconfirm = (EditText) findViewById(R.id.passconfirm);
        if(pass.getText().toString() != passconfirm.getText().toString()){
            Log.d("acd","notequals");
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void makeAccountRequest() throws JSONException {

        showpDialog();
        EditText firstname = (EditText) findViewById(R.id.first);
        EditText lastname = (EditText) findViewById(R.id.last);
        final EditText email = (EditText) findViewById(R.id.email);
        EditText username = (EditText) findViewById(R.id.uname);
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText passconfirm = (EditText) findViewById(R.id.passconfirm);

        String one = pass.getText().toString();
        String two = passconfirm.getText().toString();

        if(!one.equals(two)){
            Toast.makeText(getApplicationContext(),
                    "Passwords must match",
                    Toast.LENGTH_LONG).show();
            hidepDialog();
                return;

        }


        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("Firstname", firstname.getText().toString());
        _jsonOBJ.put("Lastname", lastname.getText().toString());
        _jsonOBJ.put("Username", username.getText().toString());
        _jsonOBJ.put("email", email.getText().toString());
        _jsonOBJ.put("password", pass.getText().toString());
        final JSONObject newuser = new JSONObject();
        newuser.put("user", _jsonOBJ);



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                "https://studygroupformer.herokuapp.com/users", newuser, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response:", response.toString());


                    // Parsing json object response
                    // response will be a json object
                    //String name = response.getString("Firstname") + " " + response.getString("Lastname");



                    //appcontroler logic
                    Intent intent = new Intent();
                    intent.putExtra("email", email.getText().toString());

                    setResult(Activity.RESULT_OK, intent);
                    finish();



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

}
