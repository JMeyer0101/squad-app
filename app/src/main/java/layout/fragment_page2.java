package layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mars.httpapp.AppController;
import com.example.mars.httpapp.GroupAdapter;
import com.example.mars.httpapp.GroupDetailActivity;
import com.example.mars.httpapp.R;
import com.example.mars.httpapp.StudyGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class fragment_page2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PAGE = "ARG_PAGE";

    // json object response url
    //private String urlJsonObj = "https://studygroupformer.herokuapp.com/users/1";

    // json array response url
    private String urlJsonArry = "https://studygroupformer.herokuapp.com/studygroups";



    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;


    // temporary string to show the parsed response
    private String jsonResponse;
    private Button btnMakeArrayRequest;
    private Button btnMakeObjectRequest;
    // TODO: Rename and change types of parameters
    private int mPage;
    //private String mParam2;




    public fragment_page2() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_page2 newInstance(int page) {
        fragment_page2 fragment = new fragment_page2();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppController.getInstance().FullGroupList.size() > 0)
        {
            AppController.getInstance().FullGroupList.clear();
            makeAllGroupsRequest();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                    makeAllGroupsRequest();


            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_page2, container, false);
       // FrameLayout frameLayout = (FrameLayout) view;
       // textView.setText("Fragment2 #" + mPage);
        //http related
        btnMakeObjectRequest = (Button) view.findViewById(R.id.groupsearch);
        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArrayUpdate();
            }
        });
        Button refreshbtn = (Button) view.findViewById(R.id.reloadbutton);
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAllGroupsRequest();
            }
        });
        final ListView listView = (ListView) view.findViewById(R.id.list2);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //end http related


        if(AppController.getInstance().FullGroupList.size() > 0)
        {
            AppController.getInstance().FullGroupList.clear();
            makeAllGroupsRequest();
        }
        else {
            //makeJsonObjectRequest();

                makeAllGroupsRequest();


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
                startActivityForResult(intent, 1);


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
     * Method to make json array request where response starts with [
     * */
    private void makeAllGroupsRequest() {

        showpDialog();
        AppController.getInstance().FullGroupList.clear();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response:", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject group = (JSONObject) response.get(i);

                                String department = group.getString("department");
                                int classno = group.getInt("class_number");
                                String date = group.getString("date");
                                String time = group.getString("time");
                                String description = group.getString("description");
                                int id = group.getInt("id");


                                //Log.i("outputmsg", AppController.getInstance().usergroups.get(0).get("description"));
                                //jsonResponse += "Mobile: " + mobile + "\n\n\n";


                                //appcontroller logic
                                AppController.getInstance().FullGroupList.add(new StudyGroup(id, department, classno, time, description, date));
                                for (Iterator<StudyGroup> iterator = AppController.getInstance().FullGroupList.iterator(); iterator.hasNext(); ) {
                                    StudyGroup sg = iterator.next();
                                    for (StudyGroup usergroup: AppController.getInstance().AppUserGroups ) {

                                        if(sg.id == usergroup.id){
                                            iterator.remove();
                                        }
                                    }

                                }
                            }

//                            txtResponse.setText(jsonResponse);
                            AppController.getInstance().grouplist = jsonResponse;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                        final ListView listView = (ListView) getActivity().findViewById(R.id.list2);
                        //use list data to populate the listview with studygroups
                        GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                                AppController.getInstance().FullGroupList);
                        // Assign adapter to ListView

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

   public void searchArrayUpdate(){
        Spinner sel = (Spinner) getActivity().findViewById(R.id.spinner);
        String searchquerydept = sel.getSelectedItem().toString();
        ArrayList<StudyGroup> temp = new ArrayList<StudyGroup>();
        for(int i = 0; i <  AppController.getInstance().FullGroupList.size(); i++){
            StudyGroup searchitem = AppController.getInstance().FullGroupList.get(i);
            if (searchitem.department.equalsIgnoreCase(searchquerydept)){
                temp.add(searchitem);
            }
        }
        //use list data to populate the listview with studygroups
        GroupAdapter adapter2 = new GroupAdapter(getActivity().getApplicationContext(),
                temp);
       final ListView listView = (ListView) getActivity().findViewById(R.id.list2);
        // Assign adapter to ListView
        listView.setAdapter(adapter2);
    }
}
