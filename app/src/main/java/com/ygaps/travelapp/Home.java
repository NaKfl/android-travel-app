package com.ygaps.travelapp;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    BottomNavigationView mainNav;
    FrameLayout mainFrame;
    FragmentHome homeFragment;
    FragmentHistory historyFragment;
    FragmentCreate createFragment;
    FragmentNofi nofiFragment;
    FragmentSetting settingFragment;
    boolean isLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainNav= findViewById(R.id.nav);
        mainFrame= findViewById(R.id.main_frame);
        homeFragment = new FragmentHome();
        setFragment(homeFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isLocation=checkUserLocationPermission();

        }
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        homeFragment = new FragmentHome();
                        setFragment(homeFragment);
                        break;
                    case R.id.nav_history:
                        historyFragment = new FragmentHistory();
                        setFragment(historyFragment);
                        break;
                    case R.id.nav_create:
                        createFragment = new FragmentCreate();
                        setFragment(createFragment);
                        break;
                    case R.id.nav_alert:
                        nofiFragment = new FragmentNofi();
                        setFragment(nofiFragment);
                        break;
                    case R.id.nav_setting:
                        settingFragment = new FragmentSetting();
                        setFragment(settingFragment);
                        break;
                }
                return true;
            }
        });
        sendTokenToServer(FirebaseInstanceId.getInstance().getToken());
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    private void sendTokenToServer(String tokenFirebase){
        String deviceId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/user/notification/put-token";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("deviceId", deviceId);
        params.put("fcmToken", tokenFirebase);
        params.put("platform","1");
        params.put("appVersion","1.0");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(Home.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(Home.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
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
    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MapActivity.Request_User_Location_Code);

            }
            return  false;
        }
        else
        {
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 99:
                isLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isLocation ) this.finish();
    }
}
