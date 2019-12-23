package com.ygaps.travelapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap map;
    private FusedLocationProviderClient client;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private Marker stopPointTemp;
    private Marker markerSelect;
    private Marker myLocation;
    public static final int Request_User_Location_Code=99;
    private Geocoder geocoder;
    private String fullAddress="HCM";
    public static List<Marker> markerList = new ArrayList<Marker>();
    private String provinceGet="HCM";
    public static LatLng myLatLngLocation = new LatLng(10.7625216,106.6801375);
    private LocationManager locationManager;

    private double latitude_marker_add,longtitude_marker_add;
    private String[] ServiceType=new String[]{
            "Restaurant",
            "Hotel",
            "Rest Station",
            "Other"
    };
    private String[] arrayProvince={"Hồ Chí Minh","Hà Nội","Đà Nẵng","Bình Dương","Đồng Nai","Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk","Cần Thơ",
            "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai", "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang",
            "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên", "Lào Cai","Nam Định","Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long",
            " Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam", "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái",
            "Tuyên Quang", "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);




    }

    private int provinceID(String str,String[] array)
    {
        for (int i=0;i<array.length;i++)
        {
            if (array[i].equals(str)==true)
            {
                return i+1;
            }
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
       for (int i=0;i<markerList.size();i++)
       {
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(markerList.get(i).getPosition());
           markerOptions.title(markerList.get(i).getTitle());
           markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
           map.addMarker(markerOptions);
       }
       if (markerList.size()>0)
       {
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(markerList.size()-1).getPosition(),10));
       }
       else{
       }



        if (true)
        {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions temp = new MarkerOptions();
                    temp.position(latLng);
                    temp.title("Clicked Location");
                    temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    stopPointTemp = map.addMarker(temp);
                }
            });
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                // Popup information
                final Dialog dialog = new Dialog(MapActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_select_stop_point);
                dialog.show();

                LatLng temp = marker.getPosition();
                double latitude_marker,longtitude_marker;
                latitude_marker = temp.latitude;
                longtitude_marker = temp.longitude;
                latitude_marker_add = latitude_marker;
                longtitude_marker_add = longtitude_marker;


                markerSelect=marker;

                List<Address> addressList;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(latitude_marker,longtitude_marker,1);
                    String address = addressList.get(0).getAddressLine(0);
                    provinceGet = addressList.get(0).getAdminArea();
                    fullAddress = address;
                    Toast.makeText(MapActivity.this, fullAddress, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }




                TextView tvAdd = (TextView) dialog.findViewById(R.id.stop_point_information);
                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Popup Add Point

                        final Dialog dialogStopPoint = new Dialog(MapActivity.this);
                        dialogStopPoint.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogStopPoint.setContentView(R.layout.add_stop_point);
                        final Spinner spinService=(Spinner) dialogStopPoint.findViewById(R.id.service_type);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MapActivity.this,R.layout.spinner_items,ServiceType);
                        spinService.setAdapter(arrayAdapter);

                        final EditText stop_point_name = (EditText) dialogStopPoint.findViewById(R.id.stop_point_name);
                        EditText address_stoppoint = (EditText) dialogStopPoint.findViewById(R.id.address_stoppoint);
                        final EditText province = (EditText) dialogStopPoint.findViewById(R.id.province);
                        final EditText mincost_stop = (EditText) dialogStopPoint.findViewById(R.id.mincost_stop);
                        final EditText maxcost_stop = (EditText) dialogStopPoint.findViewById(R.id.maxcost_stop);
                        Button btnAddStopPoint = (Button) dialogStopPoint.findViewById(R.id.btnAddStopPoint);
                        final EditText timeArrive = (EditText) dialogStopPoint.findViewById(R.id.timeArrive);
                        final EditText timeLeave = (EditText) dialogStopPoint.findViewById(R.id.timeLeave);
                        final EditText dateArrive = (EditText) dialogStopPoint.findViewById(R.id.dateArrive);
                        final EditText dateLeave = (EditText) dialogStopPoint.findViewById(R.id.dateLeave);
                        province.setText(provinceGet);
                        address_stoppoint.setText(fullAddress);

                        dateArrive.setFocusable(false);
                        dateLeave.setFocusable(false);

                        timeLeave.setFocusable(false);
                        timeArrive.setFocusable(false);
                        timeArrive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                TimePickerDialog timePickerDialog=new TimePickerDialog(MapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                                        calendar.set(0,0,0,i,i1);
                                        timeArrive.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },00,00,false);
                                timePickerDialog.show();
                            }
                        });

                        timeLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                TimePickerDialog timePickerDialog=new TimePickerDialog(MapActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                                        calendar.set(0,0,0,i,i1);
                                        timeLeave.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },00,00,false);
                                timePickerDialog.show();
                            }
                        });

                        dateArrive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog=new DatePickerDialog(MapActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        calendar.set(i,i1,i2);
                                        dateArrive.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },1999,01,01);
                                datePickerDialog.show();
                            }
                        });

                        dateLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog=new DatePickerDialog(MapActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        calendar.set(i,i1,i2);
                                        dateLeave.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },1999,01,01);
                                datePickerDialog.show();
                            }
                        });

                        dialogStopPoint.show();

                        btnAddStopPoint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String namePoint = stop_point_name.getText().toString();
                                String minCost = mincost_stop.getText().toString().trim();
                                String maxCost = maxcost_stop.getText().toString().trim();
                                int mType;
                                if (spinService.getSelectedItem().toString().equals("Restaurant"))
                                {
                                    mType=1;
                                }
                                else if(spinService.getSelectedItem().toString().equals("Hotel"))
                                {
                                    mType=2;
                                }
                                else if (spinService.getSelectedItem().toString().equals("Rest Station"))
                                {
                                    mType=3;
                                }
                                else
                                {
                                    mType=4;
                                }

                                long mArrive =0,mLeave =0;
                                Date dArrive, dLeave;
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                try {
                                    dArrive = sdf.parse(dateArrive.getText().toString()+" "+timeArrive.getText().toString());
                                    mArrive = dArrive.getTime();

                                    dLeave = sdf.parse(dateArrive.getText().toString()+" "+timeArrive.getText().toString());
                                    mLeave = dLeave.getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                int proID = provinceID(province.getText().toString(),arrayProvince);


                                String URL = "http://35.197.153.192:3000/tour/set-stop-points";
                                JSONObject jsonPoint= new JSONObject();
                                try {
                                    jsonPoint.put("name",namePoint);
                                    jsonPoint.put("address",fullAddress);
                                    jsonPoint.put("provinceId",proID);
                                    jsonPoint.put("serviceTypeId",mType);
                                    jsonPoint.put("lat",latitude_marker_add);
                                    jsonPoint.put("long",longtitude_marker_add);
                                    jsonPoint.put("arrivalAt",mArrive);
                                    jsonPoint.put("leaveAt",mLeave);
                                    jsonPoint.put("minCost",minCost);
                                    jsonPoint.put("maxCost",maxCost);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray jsonArrayPoint = new JSONArray();
                                jsonArrayPoint.put(jsonPoint);


                                final JSONObject json_post= new JSONObject();
                                try {
                                    json_post.put("tourId",FragmentCreate.tourID);
                                    json_post.put("stopPoints",jsonArrayPoint);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("AAAA",json_post.toString());
                                final String requestBody = json_post.toString();
                                final RequestQueue requestQueue= Volley.newRequestQueue(MapActivity.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                markerSelect.setTitle(fullAddress);
                                                markerList.add(markerSelect);
                                                Toast.makeText(MapActivity.this, "Thành công !!!" + markerList.size(), Toast.LENGTH_LONG).show();
                                                dialogStopPoint.dismiss();
                                                dialog.dismiss();
                                                onMapReady(map);
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MapActivity.this, ""+error, Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8";
                                    }

                                    @Override
                                    public byte[] getBody() throws AuthFailureError {
                                        try {
                                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                            return null;
                                        }
                                    }

                                    @Override
                                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                        String responseString = "";
                                        if (response != null) {
                                            responseString = String.valueOf(response.statusCode);
                                            // can get more details such as response.headers
                                        }
                                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                    }
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap<String, String> headers = new HashMap<String, String>();
                                        //headers.put("Content-Type", "application/json");
                                        headers.put("Authorization", LoginPage.token);
                                        return headers;
                                    }
                                };
                                requestQueue.add(stringRequest);



//                                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, json_post,
//                                        new Response.Listener<JSONObject>() {
//                                            @Override
//                                            public void onResponse(JSONObject response) {
//                                                try {
//                                                    String a =response.getString("id");
//                                                    Toast.makeText(MapActivity.this, "OKKKKKKKKK", Toast.LENGTH_SHORT).show();
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(MapActivity.this, "" + error, Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }){
//                                    @Override
//                                    public Map<String, String> getHeaders() throws AuthFailureError {
//                                        HashMap<String, String> headers = new HashMap<String, String>();
//                                        headers.put("Content-Type", "application/json");
//                                        headers.put("Authorization", LoginPage.token);
//                                        return headers;
//                                    }
//                                };
//                                requestQueue.add(request_json);


                            }
                        });


                    }
                });





                return false;
            }
        });

    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }
            return  false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        map.setBuildingsEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(this, "Permission Denied !!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        if (currentUserLocationMarker!=null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        currentUserLocationMarker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        if (markerList.size()>0)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(markerList.size()-1).getPosition(),10));
        }

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(map != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            map.clear();
            // add markers from database to the map
        }
    }

}
