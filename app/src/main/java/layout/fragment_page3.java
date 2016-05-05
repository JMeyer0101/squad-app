package layout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mars.httpapp.AppController;
import com.example.mars.httpapp.GroupAdapter;
import com.example.mars.httpapp.R;
import com.example.mars.httpapp.StudyGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class fragment_page3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PAGE = "ARG_PAGE";

    // json object response url
    private String urlJsonObj = "https://studygroupformer.herokuapp.com/1";

    // json array response url
    private String urlJsonArry = "https://studygroupformer.herokuapp.com/studygroups";

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;
    private Button btnMakeArrayRequest;

    // TODO: Rename and change types of parameters
    private int mPage;
    //private String mParam2;



    public fragment_page3() {
        // Required empty public constructor
    }


    public static fragment_page3 newInstance(int page) {
        fragment_page3 fragment = new fragment_page3();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //check for precached data in singleton
       // String recoverydata= AppController.getInstance().grouplist;
       // if (recoverydata != null) {
            // Restore last state for checked position.
        //    txtResponse.setText(recoverydata);
      //  }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            //mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_page3, container, false);
        //RelativeLayout frameLayout = (RelativeLayout) view;
        //textView.setText("Fragment3 #" + mPage);
        btnMakeArrayRequest = (Button) view.findViewById(R.id.createbutton);
        //ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        //txtResponse = (TextView) view.findViewById(R.id.JsonDataView);
        /*txtResponse.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtResponse.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        //txtResponse.setMovementMethod(new ScrollingMovementMethod());

        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json array request
                try {
                    createGroupRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
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
    private void createGroupRequest() throws JSONException{



        showpDialog();
        Spinner sel = (Spinner) getActivity().findViewById(R.id.spinnercreate);
        String searchquerydept = sel.getSelectedItem().toString();
        EditText classno = (EditText) getActivity().findViewById(R.id.classnotxt);
        EditText date = (EditText) getActivity().findViewById(R.id.date);
        EditText time = (EditText) getActivity().findViewById(R.id.editTexttime);
        EditText description = (EditText) getActivity().findViewById(R.id.descriptiontext);


        //final JSONArray user = new JSONArray();
            final JSONObject _jsonOBJ = new JSONObject();
            _jsonOBJ.put("class_number", Integer.valueOf(classno.getText().toString()));
        _jsonOBJ.put("department", searchquerydept);

        _jsonOBJ.put("date", date.toString());
        _jsonOBJ.put("description", description.toString());
        _jsonOBJ.put("time", time.toString());

        JSONObject parentData = new JSONObject();
        parentData.put("studygroup", _jsonOBJ);


        //user.put(_jsonOBJ);

            JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, "https://studygroupformer.herokuapp.com/studygroups", parentData,
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
                                        AppController.getInstance().FullGroupList.add(new StudyGroup(id, department, classno, time, description, date));

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
