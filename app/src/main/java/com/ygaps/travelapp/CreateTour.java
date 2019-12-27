package com.ygaps.travelapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateTour extends AppCompatActivity {

    EditText tour_name,startDate,endDate,adults,children,mincost,maxcost;
    RadioGroup isPrivate;
    RadioButton privateTour;
    RadioButton publicTour;
    Button btnCreate;
    public static String tourID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create);

        ((AppCompatActivity)CreateTour.this).getSupportActionBar().setTitle(R.string.create_tour_title);

        tour_name = (EditText) findViewById(R.id.tour_name);
        startDate=(EditText)findViewById(R.id.startDate);
        startDate.setFocusable(false);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime1();
            }
        });
        endDate= (EditText)findViewById(R.id.endDate);
        endDate.setFocusable(false);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime2();
            }
        });
        adults = (EditText) findViewById(R.id.adults);
        children = (EditText) findViewById(R.id.children);
        mincost = (EditText) findViewById(R.id.mincost);
        maxcost = (EditText) findViewById(R.id.maxcost);
        isPrivate = (RadioGroup)findViewById(R.id.isPrivate);
        privateTour = (RadioButton) findViewById(R.id.privateTour);
        publicTour = (RadioButton) findViewById(R.id.publicTour);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        boolean isLocation = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isLocation = checkUserLocationPermission();
        }

        final RequestQueue requestQueue= Volley.newRequestQueue(CreateTour.this);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTourName=tour_name.getText().toString().trim();
                String txtStartDate=startDate.getText().toString().trim();
                String txtEndDate=endDate.getText().toString().trim();
                String txtAdults=adults.getText().toString().trim();
                String txtChildren = children.getText().toString().trim();
                String txtMinCost = mincost.getText().toString().trim();
                String txtMaxCost = maxcost.getText().toString().trim();
                long mStartDate =0,mEndDate =0;
                Date startD, endD;
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");


                try {
                    startD = sdf.parse(txtStartDate);
                    endD = sdf.parse(txtEndDate);
                    mStartDate = startD.getTime();
                    mEndDate = endD.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                String private_select="1";

                if (privateTour.isChecked())
                    private_select="1";
                if (publicTour.isChecked())
                    private_select="0";

                String URL = "http://35.197.153.192:3000/tour/create";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", txtTourName);
                params.put("startDate", mStartDate+"");
                params.put("endDate",mEndDate+"");
                params.put("isPrivate",private_select);
                params.put("adults",txtAdults);
                params.put("childs",txtChildren);
                params.put("minCost",txtMinCost);
                params.put("maxCost",txtMaxCost);

                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    tourID =response.getString("id");
                                    MapActivity.markerList.clear();
                                    Intent intent = new Intent(CreateTour.this,MapActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(CreateTour.this, "TourID :"+tourID, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            String statusCode=String.valueOf(networkResponse.statusCode);
                            switch(statusCode){
                                case "400":
                                    Toast.makeText(CreateTour.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                                    break;
                                case "500":
                                    Toast.makeText(CreateTour.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CreateTour.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                requestQueue.add(request_json);
            }
        });
    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(CreateTour.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateTour.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(CreateTour.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(CreateTour.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);

            }
            return  false;
        }
        else
        {
            return true;
        }
    }
    private void PickTime1()
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(CreateTour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(i,i1,i2);
                startDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },1999,01,01);
        datePickerDialog.show();
    }
    private void PickTime2()
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(CreateTour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(i,i1,i2);
                endDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },1999,01,01);
        datePickerDialog.show();
    }
}
