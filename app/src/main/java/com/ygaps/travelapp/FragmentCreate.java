package com.ygaps.travelapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.ygaps.travelapp.Adapter.AdapterStp;
import com.ygaps.travelapp.Modal.StopPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCreate extends Fragment {
    private ArrayList<StopPoint> stp = new ArrayList<>();
    TextView name, cost, address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stoppoint, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.list_stoppoint);
        final ListView listView = (ListView) rootView.findViewById(R.id.list_stp);
        getStoppoint(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_stp);
                mappingDiaglog(dialog,stp.get(i));
                listFeedback(dialog,stp.get(i));
                dialog.show();
            }
        });
        return rootView;
    }
    public void mappingDiaglog(Dialog dialog, StopPoint stopPoint){
        name = (TextView) dialog.findViewById(R.id.name_stp_detail);
        cost = (TextView) dialog.findViewById(R.id.stp_cost_detail);
        address = (TextView) dialog.findViewById(R.id.stp_address_detail);

        name.setText(stopPoint.getName());
        cost.setText(stopPoint.getMinCost() + " - " +stopPoint.getMaxCost());
        address.setText(stopPoint.getAddress());
    }
    public void listFeedback(Dialog dialog,StopPoint stopPoint){
        String idOfservice = stopPoint.getId();
        ListView listFback = dialog.findViewById(R.id.list_stp_feedback);
        final RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        String URL = "http://35.197.153.192:3000/tour/get/feedback-service?serviceId="+idOfservice+"&pageIndex=1&pageSize=999";

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("feedbackList");
                            Log.d("hihe", jsonArray.length() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue2.add(request_json);
    }
    public void getStoppoint(final ListView listView) {
        String URL = "http://35.197.153.192:3000/tour/suggested-destination-list";
        JSONObject coordinate1 = new JSONObject();
        try {
            coordinate1.put("lat", -32.681328);
            coordinate1.put("long", 22.896155);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject coordinate2 = new JSONObject();
        try {
            coordinate2.put("lat", 58.547988);
            coordinate2.put("long", 28.171851);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject coordinate3 = new JSONObject();
        try {
            coordinate3.put("lat", 61.614044);
            coordinate3.put("long", 172.664038);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject coordinate4 = new JSONObject();
        try {
            coordinate4.put("lat", -43.185440);
            coordinate4.put("long", 146.999976);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray coordinateSet1 = new JSONArray();
        coordinateSet1.put(coordinate1);
        coordinateSet1.put(coordinate2);

        JSONArray coordinateSet2 = new JSONArray();
        coordinateSet2.put(coordinate3);
        coordinateSet2.put(coordinate4);
        final JSONObject coordinateSet1_O = new JSONObject();
        try {
            coordinateSet1_O.put("coordinateSet", coordinateSet1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject coordinateSet2_O = new JSONObject();
        try {
            coordinateSet2_O.put("coordinateSet", coordinateSet2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray coordList = new JSONArray();
        coordList.put(coordinateSet1_O);
        coordList.put(coordinateSet2_O);


        final JSONObject json_post = new JSONObject();
        try {
            json_post.put("hasOneCoordinate", false);
            json_post.put("coordList", coordList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AAAA", json_post.toString());
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, json_post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("stopPoints");
                            stp.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject tour = jsonArray.getJSONObject(i);
                                String id = tour.getString("id");
                                String name = tour.getString("name");
                                String address = tour.getString("address");
                                String provinceId = tour.getString("provinceId");
                                String lat = tour.getString("lat");
                                String longi = tour.getString("long");
                                String minCost = tour.getString("minCost");
                                String maxCost = tour.getString("maxCost");
                                String serviceTypeId = tour.getString("serviceTypeId");
                                String avatar = "0";
                                String landingTimesOfUser = "0";
                                StopPoint temp = new StopPoint(id, "", address, name, "", "", minCost, maxCost, serviceTypeId, avatar, lat, longi);
                                stp.add(temp);
                            }
                            Log.d("hihi", stp.size() + "");
                            AdapterStp adapterStp = new AdapterStp(getActivity(), R.layout.list_stp_single, stp);
                            listView.setAdapter(adapterStp);
                            adapterStp.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }
}