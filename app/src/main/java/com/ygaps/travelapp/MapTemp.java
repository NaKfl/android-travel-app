package com.ygaps.travelapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapTemp extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap map;
    private Marker stopPointTemp;
    private Marker currentUserLocationMarker;
    private FusedLocationProviderClient client;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker markerSelect;
    public static final int Request_User_Location_Code=99;
    private Geocoder geocoder;

    private String fullAddress="HCM";
    private String provinceGet="HCM";
    private String[] arrayProvince={"Hồ Chí Minh","Hà Nội","Đà Nẵng","Bình Dương","Đồng Nai","Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk","Cần Thơ",
            "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai", "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang",
            "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên", "Lào Cai","Nam Định","Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long",
            " Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam", "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái",
            "Tuyên Quang", "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_temp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTemp);
        mapFragment.getMapAsync(MapTemp.this);
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions temp = new MarkerOptions();
                String address_click = "";
                temp.position(latLng);
                List<Address> addressList;
                geocoder = new Geocoder(MapTemp.this, Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    address_click = addressList.get(0).getAddressLine(0);

                    Toast.makeText(MapTemp.this, address_click, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp.title(address_click);
                temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                stopPointTemp = map.addMarker(temp);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng mLatLng = marker.getPosition();
                List<Address> addressList;
                geocoder = new Geocoder(MapTemp.this, Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(mLatLng.latitude,mLatLng.longitude,1);
                    String address = addressList.get(0).getAddressLine(0);
                    provinceGet = addressList.get(0).getAdminArea();
                    fullAddress = address;
                    Toast.makeText(MapTemp.this, fullAddress, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                double mNewLat = mLatLng.latitude;
                double mNewLong = mLatLng.longitude;
                String mNewAddress=marker.getTitle();

                int proID = provinceID(provinceGet,arrayProvince);
                finish();
                MapActivity.SetTextAdress(mNewLat,mNewLong,proID+"",mNewAddress);
                return true;
            }
        });
    }
}
