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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class FragmentHome extends Fragment {
    static ArrayList<Tour> tours=new ArrayList<>();
    public static AdapterTour adapter;
    EditText numPage;
    Button btnShow,btnBack,btnNext;
    FragmentHome homeFragment;
    TextView emptySearch;
    EditText searchInput;
    ImageView createTour;

    public static String numTour,page="1";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        final ListView listView= rootView.findViewById(R.id.list_tour_home);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.list_tour_title);

        numPage = (EditText) rootView.findViewById(R.id.numPage);
        btnShow = (Button) rootView.findViewById(R.id.btnShow);
        btnBack = (Button) rootView.findViewById(R.id.home_back_button);
        btnNext = (Button) rootView.findViewById(R.id.home_next_button);
        searchInput=(EditText)rootView.findViewById(R.id.home_search_input);
        createTour = (ImageView) rootView.findViewById(R.id.create_button);

        createTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CreateTour.class);
                startActivity(intent);

            }
        });

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
                if(s.toString().isEmpty()){
                    FragmentHome homeFragment = new FragmentHome();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_frame,homeFragment);
                    fragmentTransaction.commit();
                }else{
                    String keyword= s.toString().trim();
                    ArrayList<Tour> searchListTour=new ArrayList<>();
                    for(int i=0; i<tours.size();i++){
                        if(tours.get(i).getDestination().contains(keyword)){
                            searchListTour.add(tours.get(i));
                        }
                    }

                    if(searchListTour.isEmpty()) {
                        emptySearch = (TextView) rootView.findViewById(R.id.home_empty);
                        emptySearch.setVisibility(View.VISIBLE);
                    }else{
                        emptySearch = (TextView) rootView.findViewById(R.id.home_empty);
                        emptySearch.setVisibility(View.GONE);
                    }

                    adapter=new AdapterTour(getActivity(),R.layout.tour_single,searchListTour);
                    listView.setAdapter(adapter);
                    setOnItemClick(listView,searchListTour);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //Chức năng chuyển trang
        numPage.setText(page);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = numPage.getText().toString();
                homeFragment = new FragmentHome();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,homeFragment);
                fragmentTransaction.commit();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = numPage.getText().toString();
                int pageInt=Integer.parseInt(page);
                if(pageInt-1>=1){
                    pageInt=pageInt-1;
                }
                page=String.valueOf(pageInt);
                homeFragment = new FragmentHome();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,homeFragment);
                fragmentTransaction.commit();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = numPage.getText().toString();
                int pageInt=Integer.parseInt(page);
                pageInt=pageInt+1;
                page=String.valueOf(pageInt);
                homeFragment = new FragmentHome();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,homeFragment);
                fragmentTransaction.commit();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String URL = "http://35.197.153.192:3000/tour/list?rowPerPage=100"+"&pageNum="+ page;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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
                                emptySearch=(TextView)rootView.findViewById(R.id.home_empty);
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
        
        setOnItemClick(listView,tours);

        return rootView;
    }
    private void setOnItemClick(ListView listView, final ArrayList<Tour>tourArray){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),TourDetail.class);
                intent.putExtra("tourId",tourArray.get(position).tourId);
                intent.putExtra("isMyTour","0");
                startActivity(intent);
            }
        });
    }
}
