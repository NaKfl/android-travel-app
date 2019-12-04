package anhem1nha.shashank.platform.fancyloginpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apptour.anhem1nha.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentHistory extends Fragment {
    static ArrayList<tour> tours=new ArrayList<>();
    public static AdapterTour adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);

        final ListView listView=(ListView)rootView.findViewById(R.id.list_tour_history);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.history_tour_title);

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String URL = "http://35.197.153.192:3000/tour/list?rowPerPage=5"+"&pageNum=1";
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("tours");
                            tours.clear();
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject tour = jsonArray.getJSONObject(i);
                                String nameTour = tour.getString("name");
                                String minCost = tour.getString("minCost");
                                String maxCost = tour.getString("maxCost");
                                String startDate = tour.getString("startDate");
                                String endDate = tour.getString("endDate");
                                String adults = tour.getString("adults");
                                String avatar = tour.getString("avatar");
                                String timeStart="0";
                                String timeEnd="0";
                                try{
                                    long miliStartDate=Long.parseLong(startDate);
                                    Date startD=new Date(miliStartDate);
                                    DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                                    timeStart=dateFormat.format(startD);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                try{
                                    long miliEndDate=Long.parseLong(endDate);
                                    Date endD=new Date(miliEndDate);
                                    DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                                    timeEnd=dateFormat1.format(endD);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }


                                tour temp = new tour("",nameTour,timeStart+" - "+timeEnd,adults,minCost+" - "+maxCost);
                                tours.add(temp);
                            }
                            adapter=new AdapterTour(getActivity(),R.layout.tour_single,tours);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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


        super.onCreateView(inflater,container,savedInstanceState);

        return rootView;
    }
}
