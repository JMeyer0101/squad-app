package com.example.mars.httpapp;

import android.app.Application;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mars on 2/14/16.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private ProgressDialog pDialog;
    public String grouplist;
    public HashMap<String,String> user = new HashMap<String,String>();
    public User AppUser;
    public ArrayList<StudyGroup> AppUserGroups = new ArrayList<StudyGroup>();
    public ArrayList<StudyGroup> FullGroupList = new ArrayList<StudyGroup>();

    private RequestQueue mRequestQueue;



    public ArrayList<HashMap<String,String>> usergroups = new ArrayList<HashMap<String,String>>();





    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    //begin json methods

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
            /* Method to make json object request where json response starts wtih {
        * */
       /* private void makeJsonObjectRequest() {

            showpDialog();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    urlJsonObj, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response:", response.toString());

                    try {
                        // Parsing json object response
                        // response will be a json object
                        String name = response.getString("Firstname") + " " + response.getString("Lastname");
                        String email = response.getString("email");
                        String id = response.getString("id");
                        //JSONObject phone = response.getJSONObject("phone");
                        //String home = phone.getString("home");
                        // String mobile = phone.getString("mobile");

                        jsonResponse = "";
                        jsonResponse +=  name + "\n\n";
                        jsonResponse +=  email + "\n\n";

                        //jsonResponse += "Mobile: " + mobile + "\n\n";

                        userinfo.setText(jsonResponse);
                        AppController.getInstance().user.put("name", name);
                        AppController.getInstance().user.put("email", email);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    hidepDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error: ", error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    hidepDialog();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }*/
}


