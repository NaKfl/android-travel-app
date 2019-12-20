package anhem1nha.shashank.platform.fancyloginpage;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.apptour.anhem1nha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentCreate extends Fragment {
    EditText tour_name,startDate,endDate,adults,children,mincost,maxcost;
    RadioGroup isPrivate;
    RadioButton privateTour;
    RadioButton publicTour;
    Button btnCreate;
    public static String tourID="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create,container,false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.create_tour_title);

        tour_name = (EditText) rootView.findViewById(R.id.tour_name);
        startDate=(EditText)rootView.findViewById(R.id.startDate);
        startDate.setFocusable(false);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime1();
            }
        });
        endDate= (EditText)rootView.findViewById(R.id.endDate);
        endDate.setFocusable(false);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime2();
            }
        });
        adults = (EditText) rootView.findViewById(R.id.adults);
        children = (EditText) rootView.findViewById(R.id.children);
        mincost = (EditText) rootView.findViewById(R.id.mincost);
        maxcost = (EditText) rootView.findViewById(R.id.maxcost);
        isPrivate = (RadioGroup)rootView.findViewById(R.id.isPrivate);
        privateTour = (RadioButton) rootView.findViewById(R.id.privateTour);
        publicTour = (RadioButton) rootView.findViewById(R.id.publicTour);
        btnCreate = (Button) rootView.findViewById(R.id.btnCreate);
        boolean isLocation = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isLocation = checkUserLocationPermission();
        }

        final RequestQueue requestQueue= Volley.newRequestQueue(getContext());
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
                                    Intent intent = new Intent(getActivity(),MapActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getActivity(), "TourID :"+tourID, Toast.LENGTH_SHORT).show();

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
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", LoginPage.token);
                        return headers;
                    }
                };
                requestQueue.add(request_json);;
            }
        });



        return rootView;
    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);

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
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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