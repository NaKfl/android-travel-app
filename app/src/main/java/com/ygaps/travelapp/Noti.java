package com.ygaps.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ygaps.travelapp.Adapter.AdapterTour;
import com.ygaps.travelapp.Modal.Tour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Noti extends AppCompatActivity {
    public static final String ACCEPT_ACTION = "Accept";
    public static final String DECLINE_ACTION = "Decline";
    public static final String SHOW_ACTION = "Show";

    private Intent intent;
    private TextView name,date,people,cash,success, fail,invitation;
    private Button btnAccept;
    private Button btnDecline;
    private String tourId;
    private ImageView avatar;
    RelativeLayout tourInfo;
    CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notification Detail");

        mapping();
        getTourInfo();
        addAction();
    }

    private void addAction() {
        String action=intent.getAction();
        if(action==null){
            return;
        }

        switch (action)
        {
            case ACCEPT_ACTION:
                acceptAction();
                break;
            case DECLINE_ACTION:
                declineAction();
                break;
            case SHOW_ACTION:
                showAction();
                break;
            default:
                finish();
                break;
        }
    }

    private void mapping() {
        intent = getIntent();
        btnAccept = (Button)findViewById(R.id.noti_accept);
        btnDecline =(Button) findViewById(R.id.noti_decline);
        avatar= (ImageView)findViewById(R.id.noti_avatar);
        name=(TextView) findViewById(R.id.noti_destination);
        date=(TextView) findViewById(R.id.noti_datetodate);
        people=(TextView) findViewById(R.id.noti_people);
        cash=(TextView) findViewById(R.id.noti_cash);
        success=(TextView) findViewById(R.id.noti_success);
        fail=(TextView) findViewById(R.id.noti_fail);
        tourInfo=(RelativeLayout)findViewById(R.id.noti_tour_info);
        invitation=(TextView)findViewById(R.id.noti_invitation);
        img=(CircleImageView)findViewById(R.id.noti_img);

        Bundle extras = getIntent().getExtras();
        tourId = extras.getString("tourId");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptAction();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineAction();
            }
        });
    }

    private void showAction() {
    }

    private void acceptAction() {
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/tour/response/invitation";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tourId", tourId);
        params.put("isAccepted", "true");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tourInfo.setVisibility(View.GONE);
                        btnAccept.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                        invitation.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.happy);
                        success.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(Noti.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(Noti.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Noti.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }

    private void declineAction() {
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/tour/response/invitation";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tourId", tourId);
        params.put("isAccepted", "false");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tourInfo.setVisibility(View.GONE);
                        btnAccept.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                        invitation.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.crying);
                        fail.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(Noti.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(Noti.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Noti.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }

    private void getTourInfo(){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/tour/info?tourId="+tourId;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nameTour = response.getString("name");
                            String minCost = response.getString("minCost");
                            String maxCost = response.getString("maxCost");
                            String startDate = response.getString("startDate");
                            String endDate = response.getString("endDate");
                            String adults = response.getString("adults");
                            String childs = response.getString("childs");
                            String avatar = response.getString("avatar");
                            String status = response.getString("status");
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

                            name.setText(nameTour);
                            date.setText(timeStart+" - "+timeEnd);
                            int numOfPeople=Integer.parseInt(childs)+Integer.parseInt(adults);
                            people.setText(String.valueOf(numOfPeople)+" people");
                            cash.setText(minCost + " VND - "+maxCost+" VND");
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
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }
}
