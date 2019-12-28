package com.ygaps.travelapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ygaps.travelapp.Adapter.AdapterComment;
import com.ygaps.travelapp.Modal.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation extends AppCompatActivity {
    private ArrayList <Comment> comments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
        ListView listView = findViewById(R.id.list_comment);

        setAdapter(listView,"2424");
    }
    public void setAdapter(final ListView listView ,String idOfTour){
        final RequestQueue requestQueue = Volley.newRequestQueue(Conversation.this);
        String URL = "http://35.197.153.192:3000/tour/comment-list?tourId="+idOfTour+"&pageIndex=1&pageSize=999";

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("commentList");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String id = jsonObject.getString("id");
                                String comment = jsonObject.getString("comment");
                                Comment temp = new Comment(id,name,comment,null);
                                comments.add(temp);
                            }
                            AdapterComment adapterComment = new AdapterComment(Conversation.this,R.layout.comment_single,comments,"74");
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
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }
}
