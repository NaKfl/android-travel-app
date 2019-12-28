package com.ygaps.travelapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import com.ygaps.travelapp.Adapter.AdapterFeedback;
import com.ygaps.travelapp.Adapter.AdapterStp;
import com.ygaps.travelapp.Adapter.AdapterTour;
import com.ygaps.travelapp.Modal.Feedback;
import com.ygaps.travelapp.Modal.StopPoint;
import com.ygaps.travelapp.Modal.Tour;

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
    private ArrayList<Feedback> feedbacks = new ArrayList<>();
    TextView name, cost, address, total, emptySearch;
    private ArrayList<StopPoint> searchList;
    EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stoppoint, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.list_stoppoint);
        final ListView listView = (ListView) rootView.findViewById(R.id.list_stp);
        searchInput=(EditText)rootView.findViewById(R.id.search_stop_point_input);
        total=(TextView)rootView.findViewById(R.id.total_stop_point);
        emptySearch = (TextView)rootView.findViewById(R.id.list_stp_empty);

        getStoppoint(listView);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword= s.toString().trim();
                searchList=new ArrayList<>();
                for(int i=0; i<stp.size();i++){
                    if(stp.get(i).getName().contains(keyword)){
                        searchList.add(stp.get(i));
                    }
                }

                if(searchList.isEmpty()) {
                    emptySearch.setVisibility(View.VISIBLE);
                }else {
                    emptySearch.setVisibility(View.GONE);
                }
                AdapterStp adapterStpSearch = new AdapterStp(getActivity(), R.layout.list_stp_single, searchList);
                listView.setAdapter(adapterStpSearch);
                adapterStpSearch.notifyDataSetChanged();
                setOnclickItem(listView, searchList);
            }
        });

        setOnclickItem(listView,stp);
        return rootView;
    }

    private void setOnclickItem(ListView listView, final ArrayList<StopPoint> stpArray){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.popup_stp);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(lp);
                //ánh xạ
                mappingDiaglog(dialog,stpArray.get(i));
                // sao trung bình của stp
                pointTB(dialog,stpArray.get(i));
                //list feedback
                listFeedback(dialog,stpArray.get(i));
                ImageView send_feedback = (ImageView) dialog.findViewById(R.id.send_feedback);
                dialog.show();
                send_feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_send = new Dialog(getActivity());
                        dialog_send.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_send.setContentView(R.layout.popup_feedback);

                        final RatingBar ratingBar2 = dialog_send.findViewById(R.id.rating);
                        LayerDrawable stars = (LayerDrawable) ratingBar2.getProgressDrawable();
                        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                        final EditText feedText = (EditText) dialog_send.findViewById(R.id.input_review);
                        Button btnSend = (Button) dialog_send.findViewById(R.id.btn_send_review);

                        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                        boolean fromUser) {
                                ratingBar.setRating(rating);
                            }
                        });
                        btnSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                float Ratefeed = ratingBar2.getRating();
                                if (Ratefeed < 2) {
                                    Ratefeed = 2;
                                }
                                int Ratefeedround = Math.round(Ratefeed);
                                String feed = feedText.getText().toString();
                                sendFeedBack(dialog_send,stpArray.get(i),feed,Ratefeedround);
                                listFeedback(dialog,stpArray.get(i));
                                listFeedback(dialog,stpArray.get(i));
                            }
                        });
                        dialog_send.show();
                    }
                });


            }
        });
    }

    public void mappingDiaglog(Dialog dialog, StopPoint stopPoint){
        name = (TextView) dialog.findViewById(R.id.name_stp_detail);
        cost = (TextView) dialog.findViewById(R.id.stp_cost_detail);
        address = (TextView) dialog.findViewById(R.id.stp_address_detail);

        name.setText(stopPoint.getName());
        cost.setText(stopPoint.getMinCost() + " - " +stopPoint.getMaxCost());
        address.setText(stopPoint.getAddress());
        // set visible
        TextView name_service_infor = (TextView) dialog.findViewById(R.id.name_service_infor);
        name_service_infor.setVisibility(View.GONE);
        TextView time_infor = (TextView) dialog.findViewById(R.id.time_infor);
        time_infor.setVisibility(View.GONE);
    }
    public void pointTB(final Dialog dialog, StopPoint stopPoint){
        final RequestQueue requestQueue4 = Volley.newRequestQueue(getContext());
        String URL = "http://35.197.153.192:3000/tour/get/feedback-point-stats?serviceId=" + stopPoint.getId();
        Toast.makeText(getContext(), stopPoint.getId()+"", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int point = 0;
                            int total = 0;
                            JSONArray pointStats = response.getJSONArray("pointStats");
                            for (int i = 0; i < pointStats.length(); i++) {
                                JSONObject tempPoint = pointStats.getJSONObject(i);
                                String Spoint = tempPoint.getString("point");
                                String Stotal = tempPoint.getString("total");
                                int Ipoint = Integer.parseInt(Spoint);
                                int Itotal = Integer.parseInt(Stotal);
                                if (Itotal != 0) {
                                    total = total + Itotal;
                                }

                                point = point + Ipoint * Itotal;

                            }
                            float rating = point/(total);
                            RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.pointOfStp);
                            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                            ratingBar.setRating(rating);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode = String.valueOf(networkResponse.statusCode);
                    switch (statusCode) {
                        case "400":
                            Toast.makeText(getContext(), "ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(getContext(), "ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue4.add(request_json);
    }
    public void listFeedback(final Dialog dialog, StopPoint stopPoint){
        String idOfservice = stopPoint.getId();
        final ListView listFback = dialog.findViewById(R.id.list_stp_feedback);
        final RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        String URL = "http://35.197.153.192:3000/tour/get/feedback-service?serviceId="+idOfservice+"&pageIndex=1&pageSize=999";

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("feedbackList");
                            feedbacks.clear();
                            for(int i =0 ;i<jsonArray.length();i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String feedback = jsonObject.getString("feedback");
                                String createdOn = jsonObject.getString("createdOn");
                                String point = jsonObject.getString("point");
                                Feedback temp = new Feedback(name,createdOn,point,feedback);
                                feedbacks.add(temp);
                            }
                            AdapterFeedback adapterFeedback = new AdapterFeedback(getContext(),R.layout.feedback_single,feedbacks);
                            listFback.setAdapter(adapterFeedback);
                            listFback.setMinimumHeight(92);
                            adapterFeedback.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),   error+"", Toast.LENGTH_SHORT).show();

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
                            total.setText(stp.size()+ " Destinations");
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
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
    public void sendFeedBack(final Dialog dialog1, StopPoint stopPoint, String feedback, int point)
    {
        final int check=0;
        final RequestQueue requestQueue5 = Volley.newRequestQueue(getActivity());
        String URL = "http://35.197.153.192:3000/tour/add/feedback-service";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("serviceId", stopPoint.getId());
        params.put("feedback", feedback+"");
        params.put("point",point+"");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Send Thanh cong", Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(getActivity(),"ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(getActivity(),"ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue5.add(request_json);
    }
}