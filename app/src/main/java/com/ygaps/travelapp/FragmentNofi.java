package com.ygaps.travelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ygaps.travelapp.Modal.record;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentNofi extends Fragment {
    private Button start,play,stop;
    private MediaRecorder myAudioRecorder;
    public String outputFile;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private record record;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nofi,container,false);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        start = (Button)rootView.findViewById(R.id.record);
        stop = (Button) rootView.findViewById(R.id.stop);
        play = (Button)rootView.findViewById(R.id.play);
        record=new record(play,start,myAudioRecorder,outputFile);
        record.setRecord(getContext());
        record.playRE(getContext());
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVoice();
            }
        });
        return rootView;
    }
    //yêu cầu quyền ghi tập tin và quyền ghi âm
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();
        if (!permissionToWriteAccepted ) getActivity().finish();
    }
    //send file
    public void sendVoice(){
        String idOfTour = "4500";
        final RequestQueue requestQueue2= Volley.newRequestQueue(getContext());
        String URL = "http://35.197.153.192:3000/tour/comment";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tourId",idOfTour);
        params.put("userId","55");
        params.put("comment","Bảo đẹp trai");
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Toast.makeText(getContext(),"ok", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
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
        requestQueue2.add(request_json);;
    }
}
