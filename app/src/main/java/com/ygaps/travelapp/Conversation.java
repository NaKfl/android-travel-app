package com.ygaps.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ygaps.travelapp.Adapter.AdapterComment;
import com.ygaps.travelapp.Modal.Comment;
import com.ygaps.travelapp.Modal.record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation extends AppCompatActivity {
    private String tourId="";
    private String userId="";
    private ArrayList <Comment> comments = new ArrayList<>();
    EditText input;
    Button sendButton;
    ListView listView;
    AdapterComment adapterComment;
    Handler ha;
    Runnable runnable;
    private Button start,play;
    private MediaRecorder myAudioRecorder;
    public String outputFile;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private com.ygaps.travelapp.Modal.record record;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        start = (Button)findViewById(R.id.convervation_record);
        play = (Button)findViewById(R.id.convervation_play);
        record=new record(play,start,myAudioRecorder,outputFile);
        record.setRecord(this);
        record.playRE(this);

        Intent intent = getIntent();
        tourId = intent.getStringExtra("tourId");
        userId = intent.getStringExtra("userId");

        Toast.makeText(this, tourId+" - "+userId, Toast.LENGTH_SHORT).show();
        mapping();
        setAdapter(listView,tourId);
        ha=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                setAdapter(listView,tourId);
                ha.postDelayed(this, 1000);
            }
        };
        ha.postDelayed(runnable,1000);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess=input.getText().toString().trim();
                if(mess.isEmpty()){
                    Toast.makeText(Conversation.this, "Please fill out the input form", Toast.LENGTH_SHORT).show();
                }else{
                    sendMess(mess);
                    input.setText("");
                    input.forceLayout();
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) this.finish();
        if (!permissionToWriteAccepted ) this.finish();
    }

    private void mapping(){
        listView = findViewById(R.id.list_comment);
        input=findViewById(R.id.convervation_input);
        sendButton=findViewById(R.id.convervation_button);
    }

    public void sendMess(String messContent){
        String URL = "http://35.197.153.192:3000/tour/notification";
        JSONObject jsonMess= new JSONObject();
        try {
            jsonMess.put("tourId",tourId);
            jsonMess.put("noti",messContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final RequestQueue requestQueue= Volley.newRequestQueue(Conversation.this);
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, jsonMess,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Conversation.this, "Success", Toast.LENGTH_SHORT).show();
                        setAdapter(listView,tourId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Conversation.this,  error.toString(), Toast.LENGTH_SHORT).show();

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

    public void setAdapter(final ListView listView ,String idOfTour){
        final RequestQueue requestQueue = Volley.newRequestQueue(Conversation.this);
        String URL = "http://35.197.153.192:3000/tour/notification-list?tourId="+tourId+"&pageIndex=1&pageSize="+Integer.MAX_VALUE;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("notiList");
                            comments.clear();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String id = jsonObject.getString("userId");
                                String content = jsonObject.getString("notification");
                                Comment temp = new Comment(id,name,content,"");
                                comments.add(temp);
                            }
                            adapterComment = new AdapterComment(Conversation.this,R.layout.comment_single,comments,userId);
                            listView.setAdapter(adapterComment);
                            adapterComment.notifyDataSetChanged();
                        }catch (Exception e){
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
                            Toast.makeText(Conversation.this,"ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(Conversation.this,"ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Conversation.this, "ERROR", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        ha.removeCallbacks(runnable);
    }
}
