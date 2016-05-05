package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mars.httpapp.AppController;

import com.example.mars.httpapp.GroupAdapter;
import com.example.mars.httpapp.GroupDetailActivity;
import com.example.mars.httpapp.R;
import com.example.mars.httpapp.StudyGroup;
import com.example.mars.httpapp.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_page extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PAGE = "ARG_PAGE";

    // json object response url
    private String urlJsonObj = "https://studygroupformer.herokuapp.com/users/1";

    private int mPage;
    //private String mParam2;

    // Progress dialogs
    private ProgressDialog pDialog;

    private TextView userinfo;


    // temporary string to show the parsed response
    private String jsonResponse;
    //private JSONArray usergroups;



    public fragment_page() {
        // Required empty public constructor
    }


    public static fragment_page newInstance(int page) {
        fragment_page fragment = new fragment_page();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppController.getInstance().AppUserGroups.size() >0) {
            GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                    AppController.getInstance().AppUserGroups);
            // Assign adapter to ListView
            final ListView listView = (ListView) getActivity().findViewById(R.id.list);

            listView.setAdapter(adapter2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_page, container, false);
        //TextView textView = (TextView) view.findViewById(R.id.textview);
        //textView.setText("Fragment #" + mPage);
        // Get ListView object from xml
        final ListView listView = (ListView) view.findViewById(R.id.list);
        userinfo = (TextView) view.findViewById(R.id.userinfo);






            userinfo.setText(AppController.getInstance().AppUser.username + "\n\n" +
                    AppController.getInstance().AppUser.email);

        if(AppController.getInstance().AppUserGroups.size() > 0)
        {
            GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                    AppController.getInstance().AppUserGroups);
            // Assign adapter to ListView
            listView.setAdapter(adapter2);
        }
        else {



            //makeJsonObjectRequest();
            try {
                makeJsonArrayRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }

           /* GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                    AppController.getInstance().AppUserGroups);
            // Assign adapter to ListView
            listView.setAdapter(adapter2);*/
        }


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                //String itemValue = (String) listView.getItemAtPosition(position);
                StudyGroup selectedGroup = (StudyGroup) parent.getAdapter().getItem(itemPosition);
                // Show Alert
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra("selectedGroup", selectedGroup);
                startActivity(intent);

                // Show Alert


            }

        });
        return view;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    /*private void makeJsonObjectRequest() {

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

                    jsonResponse = "";
                    jsonResponse +=  name + "\n\n";
                    jsonResponse +=  email + "\n\n";

                    //jsonResponse += "Mobile: " + mobile + "\n\n";

                    userinfo.setText(jsonResponse);
                    AppController.getInstance().user.put("name", name);
                    AppController.getInstance().user.put("email", email);
                    AppController.getInstance().AppUser = new User(uname, id, email, first, last, isadmin);


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
    //end getjsonobj


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() throws JSONException {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        showpDialog();

        final JSONObject user = new JSONObject();
        final JSONObject _jsonOBJ = new JSONObject();
        _jsonOBJ.put("email", AppController.getInstance().AppUser.email);
        user.put("user",_jsonOBJ);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, "https://studygroupformer.herokuapp.com/mygroups", user,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response:", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; (i < response.length()); i++) {

                                if(response.getJSONObject(i) != null ) {
                                    JSONObject group = (JSONObject) response.getJSONObject(i);


                                    String department = group.getString("department");
                                    int classno = group.getInt("class_number");
                                    String date = group.getString("date");
                                    String time = group.getString("time");
                                    String description = group.getString("description");
                                    int id = group.getInt("id");


                                    //Log.i("outputmsg", AppController.getInstance().usergroups.get(0).get("description"));
                                    //jsonResponse += "Mobile: " + mobile + "\n\n\n";


                                    //appcontroller logic
                                    AppController.getInstance().AppUserGroups.add(new StudyGroup(id, department, classno, time, description, date));

                                }
                            }

                            //txtResponse.setText(jsonResponse);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                        GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                                AppController.getInstance().AppUserGroups);
                        // Assign adapter to ListView
                        final ListView listView = (ListView) getActivity().findViewById(R.id.list);

                        listView.setAdapter(adapter2);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}
