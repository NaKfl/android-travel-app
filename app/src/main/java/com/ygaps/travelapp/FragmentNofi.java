package com.ygaps.travelapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ygaps.travelapp.Adapter.AdapterNoti;
import com.ygaps.travelapp.Adapter.AdapterTour;
import com.ygaps.travelapp.Modal.Noti;
import com.ygaps.travelapp.Modal.Tour;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentNofi extends Fragment {
    ArrayList<Noti> notis=new ArrayList<>();
    ListView listView;
    TextView notFound;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nofi,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        listView=(ListView)rootView.findViewById(R.id.list_noti);
        notFound=(TextView)rootView.findViewById(R.id.noti_empty);
        getListNoti();
        return rootView;
    }

    private void getListNoti(){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String URL = "http://35.197.153.192:3000/tour/get/invitation?pageIndex=1&pageSize="+ Integer.MAX_VALUE;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("tours");
                            notis.clear();
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject tour = jsonArray.getJSONObject(i);
                                String tourId = tour.getString("id");
                                String nameTour = tour.getString("name");
                                String hostName = tour.getString("hostName");
                                String date = tour.getString("createdOn");

                                Noti noti = new Noti("Invitation",hostName+" has invited you to join "+"\""+nameTour+"\"", date);
                                notis.add(noti);
                            }
                            if(notis.isEmpty()){
                                notFound.setVisibility(View.VISIBLE);
                            }
                            Collections.reverse(notis);
                            AdapterNoti adapterNoti =new AdapterNoti(getActivity(),R.layout.tour_single,notis);
                            listView.setAdapter(adapterNoti);
                            adapterNoti.notifyDataSetChanged();
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
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }
}
