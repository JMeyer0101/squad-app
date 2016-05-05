package com.example.mars.httpapp;

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

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        Button loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                makeJsonObjectRequest();}
                catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });

        Button makeacctbutton = (Button) findViewById(R.id.makeacctbutton);
        makeacctbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newAccountLogin();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                //Log.d("log:",getIntent().getStringExtra("email"));
                String email = data.getStringExtra("email");
                EditText emailfield = (EditText) findViewById(R.id.textemail);
                emailfield.setText(email);
            }
        }
    }

    public void newAccountLogin()
    {
        Intent intent = new Intent(this, MakeAccountActivity.class);
        startActivityForResult(intent,1);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void makeJsonObjectRequest() throws JSONException {

        showpDialog();
        EditText pass = (EditText) findViewById(R.id.textpass);
        EditText email = (EditText) findViewById(R.id.textemail);

        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("email", email.getText().toString());
        _jsonOBJ.put("password", pass.getText().toString());
        final JSONObject parent = new JSONObject();
        parent.put("user", _jsonOBJ);



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                "https://studygroupformer.herokuapp.com/login", parent, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response:", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("Firstname") + " " + response.getString("Lastname");



                    //appcontroler logic
                    String uname = response.getString("Username");
                    int id = response.getInt("id");
                    String email = response.getString("email");
                    String first = response.getString("Firstname");
                    String last =  response.getString("Lastname");
                    Boolean isadmin = false;

                    //JSONObject phone = response.getJSONObject("phone");
                    //String home = phone.getString("home");
                    // String mobile = phone.getString("mobile");



                    AppController.getInstance().AppUser = new User(uname, id, email, first, last, isadmin);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
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
