package com.ygaps.travelapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.ygaps.travelapp.Adapter.AdapterComment;
import com.ygaps.travelapp.Adapter.AdapterMember;
import com.ygaps.travelapp.Adapter.AdapterStopPoint;
import com.ygaps.travelapp.Adapter.AdapterTour;
import com.ygaps.travelapp.Modal.Comment;
import com.ygaps.travelapp.Modal.Member;
import com.ygaps.travelapp.Modal.StopPoint;
import com.ygaps.travelapp.Modal.Tour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
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
    public static List<Marker> markerList = new ArrayList<Marker>();
    private String fullAddress="HCM";
    private String provinceGet="HCM";
    public static LatLng myLatLngLocation = new LatLng(10.7625216,106.6801375);
    private LocationManager locationManager;
    ArrayList<StopPoint> listGetStopPoint = new ArrayList<>();
    private double latitude_marker_add,longtitude_marker_add;
    ImageView btn_ShowStoppoint;
    ImageView btn_ShowList;
    int status=-1;
    String isUpdate="";
    ArrayList<StopPoint> ViewstopPoints=new ArrayList<StopPoint>();
    ListView listShowStopPoint ;
    public static EditText address_stoppoint;
    public static double tNewLat,tNewLong;
    public static String tNewAddress,tNewProvince="-1";
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
        Intent intent = getIntent();
        isUpdate = intent.getStringExtra("isUpdate");
        btn_ShowStoppoint = (ImageView) findViewById(R.id.on_off);
        btn_ShowList = (ImageView) findViewById(R.id.showList);
        if (isUpdate.equals("1"))
        {
            btn_ShowList.setVisibility(View.GONE);
            btn_ShowStoppoint.setVisibility(View.GONE);
        }
        else{
            btn_ShowList.setVisibility(View.VISIBLE);
            btn_ShowStoppoint.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        String URL = "http://35.197.153.192:3000/tour/suggested-destination-list";
        JSONObject coordinate1= new JSONObject();
        try {
            coordinate1.put("lat",23.349891);
            coordinate1.put("long",108.383738);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject coordinate2= new JSONObject();
        try {
            coordinate2.put("lat",22.778090);
            coordinate2.put("long",101.858193);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject coordinate3= new JSONObject();
        try {
            coordinate3.put("lat",8.515921);
            coordinate3.put("long",103.874700);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject coordinate4= new JSONObject();
        try {
            coordinate4.put("lat",8.238501);
            coordinate4.put("long",110.330731);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray coordinateSet1 = new JSONArray();
        coordinateSet1.put(coordinate1);
        coordinateSet1.put(coordinate2);

        JSONArray coordinateSet2 = new JSONArray();
        coordinateSet2.put(coordinate3);
        coordinateSet2.put(coordinate4);
        final JSONObject coordinateSet1_O= new JSONObject();
        try {
            coordinateSet1_O.put("coordinateSet",coordinateSet1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject coordinateSet2_O= new JSONObject();
        try {
            coordinateSet2_O.put("coordinateSet",coordinateSet2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray coordList = new JSONArray();
        coordList.put(coordinateSet1_O);
        coordList.put(coordinateSet2_O);


        final JSONObject json_post= new JSONObject();
        try {
            json_post.put("hasOneCoordinate",false);
            json_post.put("coordList",coordList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AAAA",json_post.toString());
        RequestQueue requestQueue= Volley.newRequestQueue(MapActivity.this);
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, json_post,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("stopPoints");
                            for (int i=0;i<jsonArray.length();i++)
                            {
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
                                String avatar="0";
                                String landingTimesOfUser="0";
                                StopPoint temp = new StopPoint(id,"",address,name,"","",minCost,maxCost,serviceTypeId,avatar,lat,longi);
                                listGetStopPoint.add(temp);
                            }
                            Toast.makeText(MapActivity.this, listGetStopPoint.size()+"BBBBBB" + listGetStopPoint.get(1222).getName(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapActivity.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);



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
    private void Clear(int k){
        if (k==-1)
        {
            map.clear();
            for (int i=0;i<listGetStopPoint.size();i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng temp = new LatLng(Double.parseDouble(listGetStopPoint.get(i).getLat()),Double.parseDouble(listGetStopPoint.get(i).getLongitude()));
                markerOptions.position(temp);
                markerOptions.title(listGetStopPoint.get(i).getAddress());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                map.addMarker(markerOptions);
            }
        }
        else
        {
            map.clear();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
        btn_ShowStoppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear(status);
                status=-status;
            }
        });
        btn_ShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue= Volley.newRequestQueue(MapActivity.this);
                String URL = "http://35.197.153.192:3000/tour/info?tourId="+ CreateTour.tourID;
                final Dialog dialog = new Dialog(MapActivity.this);
                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArrayStopPoint = response.getJSONArray("stopPoints");
                                    ViewstopPoints.clear();
                                    for (int i=0;i<jsonArrayStopPoint.length();i++)
                                    {
                                        JSONObject stopPoint = jsonArrayStopPoint.getJSONObject(i);
                                        String id = stopPoint.getString("id");
                                        String serviceId = stopPoint.getString("serviceId");
                                        String address = stopPoint.getString("address");
                                        String provinceId = stopPoint.getString("provinceId");
                                        String name = stopPoint.getString("name");
                                        String lat = stopPoint.getString("lat");
                                        String longitude = stopPoint.getString("long");
                                        String arrivalAt = stopPoint.getString("arrivalAt");
                                        String leaveAt = stopPoint.getString("leaveAt");
                                        String minCost = stopPoint.getString("minCost");
                                        String maxCost = stopPoint.getString("maxCost");
                                        String serviceTypeId = stopPoint.getString("serviceTypeId");
                                        String avatar = stopPoint.getString("avatar");
                                        String index = stopPoint.getString("index");
                                        try{
                                            long miliStartDate=Long.parseLong(arrivalAt);
                                            Date startD=new Date(miliStartDate);
                                            DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                                            arrivalAt=dateFormat.format(startD);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        try{
                                            long miliEndDate=Long.parseLong(leaveAt);
                                            Date endD=new Date(miliEndDate);
                                            DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                                            leaveAt=dateFormat1.format(endD);
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        StopPoint temp = new StopPoint(id, serviceId, address, name, arrivalAt, leaveAt, minCost, maxCost, serviceTypeId, avatar,lat,longitude);
                                        ViewstopPoints.add(temp);
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setContentView(R.layout.liststoppoint_create);
                                listShowStopPoint = (ListView) dialog.findViewById(R.id.list_show_stoppoint);
                                AdapterStopPoint adapterStopPoint = new AdapterStopPoint(MapActivity.this, R.layout.stop_point_single, ViewstopPoints);
                                listShowStopPoint.setAdapter(adapterStopPoint);
                                adapterStopPoint.notifyDataSetChanged();
                                dialog.show();
                                listShowStopPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                        final Dialog Updatedialog = new Dialog(MapActivity.this);
                                        Updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        Updatedialog.setContentView(R.layout.popup_detail_stoppoint);
                                        ImageView delete_stoppoint = (ImageView) Updatedialog.findViewById(R.id.delete_stoppoint);
                                        ImageView close_dialog = (ImageView) Updatedialog.findViewById(R.id.close);
                                        close_dialog.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Updatedialog.dismiss();
                                            }
                                        });
                                        delete_stoppoint.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                RequestQueue requestQueue= Volley.newRequestQueue(MapActivity.this);
                                                String URL = "http://35.197.153.192:3000/tour/remove-stop-point?stopPointId="+ ViewstopPoints.get(i).getId();
                                                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                Toast.makeText(MapActivity.this,"Xoa thanh cong",Toast.LENGTH_LONG).show();
                                                                Updatedialog.dismiss();
                                                                finish();
                                                                overridePendingTransition(0, 0);
                                                                startActivity(getIntent());
                                                                overridePendingTransition(0, 0);
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(MapActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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
                                            }
                                        });

                                        //
                                        final Spinner spinService=(Spinner) Updatedialog.findViewById(R.id.up_service_type);
                                        ArrayAdapter arrayAdapter = new ArrayAdapter(MapActivity.this,R.layout.spinner_items,ServiceType);
                                        spinService.setAdapter(arrayAdapter);

                                        final EditText stop_point_name = (EditText) Updatedialog.findViewById(R.id.up_stop_point_name);
                                        address_stoppoint = (EditText) Updatedialog.findViewById(R.id.up_address_stoppoint);
                                        final EditText mincost_stop = (EditText) Updatedialog.findViewById(R.id.up_mincost_stop);
                                        final EditText maxcost_stop = (EditText) Updatedialog.findViewById(R.id.up_maxcost_stop);
                                        Button btnUpdate = (Button) Updatedialog.findViewById(R.id.update_btn);

                                        final EditText dateArrive = (EditText) Updatedialog.findViewById(R.id.up_dateArrive);
                                        final EditText dateLeave = (EditText) Updatedialog.findViewById(R.id.up_dateLeave);


                                        stop_point_name.setText(ViewstopPoints.get(i).getName());
                                        spinService.setSelection(Integer.parseInt(ViewstopPoints.get(i).getServiceTypeId())-1);
                                        address_stoppoint.setText(ViewstopPoints.get(i).getAddress());

                                        mincost_stop.setText(ViewstopPoints.get(i).getMinCost());
                                        maxcost_stop.setText(ViewstopPoints.get(i).getMaxCost());
                                        dateArrive.setText(ViewstopPoints.get(i).getArrivalAt());
                                        dateLeave.setText(ViewstopPoints.get(i).getLeaveAt());

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
                                        dateArrive.setFocusable(false);
                                        dateLeave.setFocusable(false);
                                        address_stoppoint.setFocusable(false);
                                        address_stoppoint.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent=new Intent(MapActivity.this,MapTemp.class);
                                                startActivity(intent);
                                            }
                                        });
                                        //
                                        Updatedialog.show();
                                        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                                                try {
                                                    dArrive = sdf.parse(dateArrive.getText().toString());
                                                    mArrive = dArrive.getTime();

                                                    dLeave = sdf.parse(dateLeave.getText().toString());
                                                    mLeave = dLeave.getTime();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                String URL = "http://35.197.153.192:3000/tour/set-stop-points";
                                                String addressFinal =address_stoppoint.getText().toString();
                                                JSONObject jsonPoint= new JSONObject();
                                                try {
                                                    jsonPoint.put("id",ViewstopPoints.get(i).getId());
                                                    jsonPoint.put("name",namePoint);
                                                    jsonPoint.put("address",addressFinal);
                                                    jsonPoint.put("serviceTypeId",mType);
                                                    jsonPoint.put("lat",tNewLat+"");
                                                    jsonPoint.put("long",tNewLong+"");
                                                    jsonPoint.put("provinceId",tNewProvince);
                                                    jsonPoint.put("arrivalAt",mArrive+"");
                                                    jsonPoint.put("leaveAt",mLeave+"");
                                                    jsonPoint.put("minCost",minCost);
                                                    jsonPoint.put("maxCost",maxCost);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                JSONArray jsonArrayPoint = new JSONArray();
                                                jsonArrayPoint.put(jsonPoint);


                                                final JSONObject json_post= new JSONObject();
                                                try {
                                                    json_post.put("tourId",CreateTour.tourID);
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
                                                                Updatedialog.dismiss();
                                                                finish();
                                                                overridePendingTransition(0, 0);
                                                                startActivity(getIntent());
                                                                overridePendingTransition(0, 0);
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



                                            }
                                        });
                                    }
                                });
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

                //

            }
        });
       /////
        ////


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (status==-1)
                    {
                        MarkerOptions temp = new MarkerOptions();
                        String address_click = "";
                        temp.position(latLng);
                        List<Address> addressList;
                        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                        try {
                            addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                            address_click = addressList.get(0).getAddressLine(0);

                            Toast.makeText(MapActivity.this, address_click, Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        temp.title(address_click);
                        temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        stopPointTemp = map.addMarker(temp);
                    }
                    else{

                    }

                }
            });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (isUpdate.equals("1"))
                {
                    LatLng mLatLng = marker.getPosition();
                    List<Address> addressList;
                    geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                    try {
                        addressList = geocoder.getFromLocation(mLatLng.latitude,mLatLng.longitude,1);
                        String address = addressList.get(0).getAddressLine(0);
                        provinceGet = addressList.get(0).getAdminArea();
                        fullAddress = address;
                        Toast.makeText(MapActivity.this, fullAddress, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    double mNewLat = mLatLng.latitude;
                    double mNewLong = mLatLng.longitude;
                    String mNewAddress=marker.getTitle();

                    int proID = provinceID(provinceGet,arrayProvince);
                    finish();
                    TourDetail.SetTextAdress(mNewLat,mNewLong,proID+"",mNewAddress);
                    return true;
                }
                else {
                    // Popup information
                    Toast.makeText(MapActivity.this, marker.getId().substring(1)+"AAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(MapActivity.this, fullAddress, Toast.LENGTH_LONG).show();

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
                            province.setFocusable(false);
                            address_stoppoint.setFocusable(false);
                            Toast.makeText(MapActivity.this, status+"aaaaaaaaa", Toast.LENGTH_SHORT).show();
                            if (status==1)
                            {
                                String position = marker.getId().substring(1);
                                int pos = Integer.parseInt(position);
                                stop_point_name.setText(listGetStopPoint.get(pos).getName());
                                mincost_stop.setText(listGetStopPoint.get(pos).getMinCost());
                                maxcost_stop.setText(listGetStopPoint.get(pos).getMaxCost());
                                spinService.setSelection(Integer.parseInt(listGetStopPoint.get(pos).getServiceTypeId())-1);
                            }
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
                                        json_post.put("tourId",CreateTour.tourID);
                                        json_post.put("stopPoints",jsonArrayPoint);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("AAAA",json_post.toString());
                                    final String requestBody = json_post.toString();
                                    final RequestQueue requestQueue= Volley.newRequestQueue(MapActivity.this);
//                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    markerSelect.setTitle(fullAddress);
//                                                    markerList.add(markerSelect);
//                                                    Toast.makeText(MapActivity.this, "Thành công !!!" + markerList.size(), Toast.LENGTH_LONG).show();
//                                                    dialogStopPoint.dismiss();
//                                                    dialog.dismiss();
//                                                    onMapReady(map);
//                                                }
//                                            }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(MapActivity.this, ""+error, Toast.LENGTH_LONG).show();
//                                        }
//                                    }){
//                                        @Override
//                                        public String getBodyContentType() {
//                                            return "application/json; charset=utf-8";
//                                        }
//
//                                        @Override
//                                        public byte[] getBody() throws AuthFailureError {
//                                            try {
//                                                return requestBody == null ? null : requestBody.getBytes("utf-8");
//                                            } catch (UnsupportedEncodingException uee) {
//                                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                                                return null;
//                                            }
//                                        }
//
//                                        @Override
//                                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                                            String responseString = "";
//                                            if (response != null) {
//                                                responseString = String.valueOf(response.statusCode);
//                                                // can get more details such as response.headers
//                                            }
//                                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                                        }
//                                        @Override
//                                        public Map<String, String> getHeaders() throws AuthFailureError {
//                                            HashMap<String, String> headers = new HashMap<String, String>();
//                                            //headers.put("Content-Type", "application/json");
//                                            headers.put("Authorization", LoginPage.token);
//                                            return headers;
//                                        }
//                                    };
//                                    requestQueue.add(stringRequest);



                                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, json_post,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                    markerSelect.setTitle(fullAddress);
                                                    markerList.add(markerSelect);
                                                    Toast.makeText(MapActivity.this, "Thành công !!!" + markerList.size(), Toast.LENGTH_LONG).show();
                                                    dialogStopPoint.dismiss();
                                                    dialog.dismiss();
                                                    finish();
                                                    startActivity(getIntent());
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MapActivity.this, "" + error, Toast.LENGTH_SHORT).show();

                                    }
                                }){
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
                            });


                        }
                    });
                    return false;
                }
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
    }
    public static void SetTextAdress(double lat,double longitute,String provinceID,String address){
        tNewProvince=provinceID;
        tNewAddress=address;
        tNewLat=lat;
        tNewLong=longitute;
        address_stoppoint.setText(address);
    }
}
