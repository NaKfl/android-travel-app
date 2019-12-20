package anhem1nha.shashank.platform.fancyloginpage;
        import android.content.Intent;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

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

        import anhem1nha.shashank.platform.fancyloginpage.Adapter.AdapterComment;
        import anhem1nha.shashank.platform.fancyloginpage.Adapter.AdapterMember;
        import anhem1nha.shashank.platform.fancyloginpage.Adapter.AdapterStopPoint;
        import anhem1nha.shashank.platform.fancyloginpage.Modal.Comment;
        import anhem1nha.shashank.platform.fancyloginpage.Modal.Member;
        import anhem1nha.shashank.platform.fancyloginpage.Modal.StopPoint;

public class TourDetail extends AppCompatActivity {
    ArrayList<StopPoint> stopPoints=new ArrayList<StopPoint>();
    ArrayList<Comment> comments=new ArrayList<Comment>();
    ArrayList<Member> members=new ArrayList<Member>();
    TextView nameOfTour, dateOfTour, peopleOfTour, cashOfTour;
    TextView stopPointEmpty, commentEmpty, memberEmpty;
    ListView listStopPoint,listComment,listMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.tour_detail);

        nameOfTour=(TextView)findViewById(R.id.tour_detail_destination);
        dateOfTour=(TextView)findViewById(R.id.tour_detail_datetodate);
        peopleOfTour=(TextView)findViewById(R.id.tour_detail_people);
        cashOfTour=(TextView)findViewById(R.id.tour_detail_cash);
        listStopPoint=(ListView)findViewById(R.id.list_stop_point);
        listComment=(ListView)findViewById(R.id.list_comment);
        listMember=(ListView)findViewById(R.id.list_member);

        Intent intent = getIntent();
        String idOfTour = intent.getStringExtra("tourId");

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/tour/info?tourId="+ idOfTour;

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String tourId=response.getString("id");
                            String tourHostId=response.getString("hostId");
                            String tourStatus=response.getString("status");
                            String tourName=response.getString("name");
                            String tourMinCost=response.getString("minCost");
                            String tourMaxCost=response.getString("maxCost");
                            String tourStartDate=response.getString("startDate");
                            String tourEndDate=response.getString("endDate");
                            String tourAdults=response.getString("adults");
                            String tourChilds=response.getString("childs");
                            String tourIsPrivate=response.getString("isPrivate");
                            String tourAvatar=response.getString("avatar");

                            try{
                                long miliStartDate=Long.parseLong(tourStartDate);
                                Date startD=new Date(miliStartDate);
                                DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                                tourStartDate=dateFormat.format(startD);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            try{
                                long miliEndDate=Long.parseLong(tourEndDate);
                                Date endD=new Date(miliEndDate);
                                DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                                tourEndDate=dateFormat1.format(endD);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            nameOfTour.setText(tourName);
                            dateOfTour.setText("Start: "+tourStartDate+ " - End: "+ tourStartDate);
                            peopleOfTour.setText("Adult(s): "+tourAdults+" - Child(s): "+tourChilds );
                            cashOfTour.setText(tourMinCost+" VND - "+tourMaxCost +" VND");

                            JSONArray jsonArrayStopPoint = response.getJSONArray("stopPoints");
                            stopPoints.clear();
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

                                StopPoint temp = new StopPoint(id, serviceId, address, name, arrivalAt, leaveAt, minCost, maxCost, serviceTypeId, avatar);
                                stopPoints.add(temp);
                            }
                            if(stopPoints.isEmpty()){
                                stopPointEmpty=(TextView)findViewById(R.id.stop_point_empty);
                                stopPointEmpty.setVisibility(View.VISIBLE);
                            }else {
                                if(stopPoints.size()>=2) {
                                    listStopPoint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 360));
                                }
                                AdapterStopPoint adapterStopPoint = new AdapterStopPoint(TourDetail.this, R.layout.stop_point_single, stopPoints);
                                listStopPoint.setAdapter(adapterStopPoint);
                                adapterStopPoint.notifyDataSetChanged();
                            }

                            JSONArray jsonArrayComment = response.getJSONArray("comments");
                            comments.clear();
                            for (int i=0;i<jsonArrayComment.length();i++)
                            {
                                JSONObject comment = jsonArrayComment.getJSONObject(i);
                                String id = comment.getString("id");
                                String name = comment.getString("name");
                                String commentContent = comment.getString("comment");
                                String avatar = comment.getString("avatar");

                                Comment temp = new Comment( id,  name,  commentContent,  avatar);
                                comments.add(temp);
                            }
                            if(comments.isEmpty()){
                                commentEmpty=(TextView)findViewById(R.id.comment_empty);
                                commentEmpty.setVisibility(View.VISIBLE);
                            }else {
                                if(comments.size()>=2){
                                    listComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 410));
                                }
                                AdapterComment adapterComment = new AdapterComment(TourDetail.this, R.layout.comment_single, comments);
                                listComment.setAdapter(adapterComment);
                                adapterComment.notifyDataSetChanged();
                            }


                            JSONArray jsonArrayMember = response.getJSONArray("members");
                            members.clear();
                            for (int i=0;i<jsonArrayMember.length();i++)
                            {
                                JSONObject member = jsonArrayMember.getJSONObject(i);
                                String id = member.getString("id");
                                String name = member.getString("name");
                                String phone = member.getString("phone");
                                String avatar = member.getString("avatar");
                                String isHost = member.getString("isHost");

                                Member temp = new Member( id,  name,  phone,  avatar,  isHost);
                                members.add(temp);
                            }
                            if(members.isEmpty()){
                                memberEmpty=(TextView)findViewById(R.id.member_empty);
                                memberEmpty.setVisibility(View.VISIBLE);
                            }else {
                                if(members.size()>=2) {
                                    listMember.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 410));
                                }
                                AdapterMember adapterMember = new AdapterMember(TourDetail.this, R.layout.member_single, members);
                                listMember.setAdapter(adapterMember);
                                adapterMember.notifyDataSetChanged();
                            }
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
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue.add(request_json);
    }
}
