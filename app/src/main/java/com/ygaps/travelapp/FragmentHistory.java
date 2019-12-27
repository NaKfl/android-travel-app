package com.ygaps.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ygaps.travelapp.Adapter.AdapterTour;
import com.ygaps.travelapp.Modal.Tour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentHistory extends Fragment {
    static ArrayList<Tour> tours=new ArrayList<>();
    public static AdapterTour adapter;
    TextView totalTour,newTour,successTour,failTour,inProcessTour, emptySearch;
    EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history,container,false);

        final ListView listView=(ListView)rootView.findViewById(R.id.list_tour_history);
        totalTour=(TextView)rootView.findViewById(R.id.total_tour);
        newTour=(TextView)rootView.findViewById(R.id.new_tour);
        successTour=(TextView)rootView.findViewById(R.id.success_tour);
        failTour=(TextView)rootView.findViewById(R.id.fail_tour);
        inProcessTour=(TextView)rootView.findViewById(R.id.in_process_tour);
        searchInput=(EditText)rootView.findViewById(R.id.history_search_input);

        //Chức năng search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    FragmentHistory historyFragment = new FragmentHistory();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame, historyFragment);
                    fragmentTransaction.commit();
                } else {
                    String keyword = s.toString().trim();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    String URL = "http://35.197.153.192:3000/tour/search-history-user?searchKey=" + keyword + "&pageIndex=1&pageSize=" + Integer.MAX_VALUE;
                    JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String total = response.getString("total");
                                        JSONArray jsonArray = response.getJSONArray("tours");
                                        tours.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject tour = jsonArray.getJSONObject(i);
                                            String tourId = tour.getString("id");
                                            String nameTour = tour.getString("name");
                                            String minCost = tour.getString("minCost");
                                            String maxCost = tour.getString("maxCost");
                                            String startDate = tour.getString("startDate");
                                            String endDate = tour.getString("endDate");
                                            String adults = tour.getString("adults");
                                            String avatar = tour.getString("avatar");
                                            String status = tour.getString("status");
                                            String timeStart = "0";
                                            String timeEnd = "0";
                                            try {
                                                long miliStartDate = Long.parseLong(startDate);
                                                Date startD = new Date(miliStartDate);
                                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                timeStart = dateFormat.format(startD);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                long miliEndDate = Long.parseLong(endDate);
                                                Date endD = new Date(miliEndDate);
                                                DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                                timeEnd = dateFormat1.format(endD);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if(!status.equals("-1")){
                                                Tour temp = new Tour(tourId,"",nameTour,timeStart+" - "+timeEnd,adults,minCost+" - "+maxCost);
                                                tours.add(temp);
                                            }
                                        }
                                        if (tours.isEmpty()) {
                                            emptySearch = (TextView) rootView.findViewById(R.id.history_empty);
                                            emptySearch.setVisibility(View.VISIBLE);
                                        } else {
                                            emptySearch = (TextView) rootView.findViewById(R.id.history_empty);
                                            emptySearch.setVisibility(View.GONE);
                                        }
                                        adapter = new AdapterTour(getActivity(), R.layout.tour_single, tours);
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", LoginPage.token);
                            return headers;
                        }
                    };
                    requestQueue.add(request_json);

                }
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.history_tour_title);

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String URL_status = "http://35.197.153.192:3000/tour/history-user-by-status";
        JsonObjectRequest request_json_status = new JsonObjectRequest(Request.Method.GET, URL_status,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("totalToursGroupedByStatus");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject tour = jsonArray.getJSONObject(i);
                                String status = tour.getString("status");
                                String total = tour.getString("total");
                                if(status.equals("-1")){
                                    failTour.setText(total);
                                }else if(status.equals("0")){
                                    newTour.setText(total);
                                }else if(status.equals("1")){
                                    successTour.setText(total);
                                }else{//status="2"
                                    inProcessTour.setText(total);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json_status);



//        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String URL = "http://35.197.153.192:3000/tour/history-user?pageIndex=1&pageSize="+Integer.MAX_VALUE;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String total=response.getString("total");
                            totalTour.setText(total+" trips");
                            JSONArray jsonArray = response.getJSONArray("tours");
                            tours.clear();
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject tour = jsonArray.getJSONObject(i);
                                String tourId = tour.getString("id");
                                String nameTour = tour.getString("name");
                                String minCost = tour.getString("minCost");
                                String maxCost = tour.getString("maxCost");
                                String startDate = tour.getString("startDate");
                                String endDate = tour.getString("endDate");
                                String adults = tour.getString("adults");
                                String avatar = tour.getString("avatar");
                                String status = tour.getString("status");
                                String timeStart="0";
                                String timeEnd="0";
                                try{
                                    long miliStartDate=Long.parseLong(startDate);
                                    Date startD=new Date(miliStartDate);
                                    DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                                    timeStart=dateFormat.format(startD);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                try{
                                    long miliEndDate=Long.parseLong(endDate);
                                    Date endD=new Date(miliEndDate);
                                    DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                                    timeEnd=dateFormat1.format(endD);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                if(!status.equals("-1")){
                                    Tour temp = new Tour(tourId,"",nameTour,timeStart+" - "+timeEnd,adults,minCost+" - "+maxCost);
                                    tours.add(temp);
                                }
                            }
                            if(tours.isEmpty()){
                                emptySearch=(TextView)rootView.findViewById(R.id.history_empty);
                                emptySearch.setVisibility(View.VISIBLE);
                            }
                            adapter=new AdapterTour(getActivity(),R.layout.tour_single,tours);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),TourDetail.class);
                intent.putExtra("tourId",tours.get(position).tourId);
                intent.putExtra("isMyTour","1");
                startActivity(intent);

            }
        });

        super.onCreateView(inflater,container,savedInstanceState);
        return rootView;
    }
}
