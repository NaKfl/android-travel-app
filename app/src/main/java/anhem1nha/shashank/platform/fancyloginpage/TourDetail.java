package anhem1nha.shashank.platform.fancyloginpage;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.android.volley.AuthFailureError;
        import com.android.volley.NetworkResponse;
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
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
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
    String idOfTour,isMyTour,tourName,tourMinCost,tourMaxCost,tourStartDate,tourEndDate,tourAdults,tourChilds,tourIsPrivate;
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
        idOfTour = intent.getStringExtra("tourId");
        isMyTour = intent.getStringExtra("isMyTour");

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
                             tourName=response.getString("name");
                             tourMinCost=response.getString("minCost");
                             tourMaxCost=response.getString("maxCost");
                             tourStartDate=response.getString("startDate");
                             tourEndDate=response.getString("endDate");
                             tourAdults=response.getString("adults");
                             tourChilds=response.getString("childs");
                             tourIsPrivate=response.getString("isPrivate");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_delete,menu);
        if (isMyTour.equals("0"))
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        else
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(true);
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.edit_tour:
                final Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_edit);


                final EditText tour_name = (EditText) dialog.findViewById(R.id.edit_tour_name);
                final EditText startDate=(EditText)dialog.findViewById(R.id.edit_startDate);
                startDate.setFocusable(false);
                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog=new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(i,i1,i2);
                                startDate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },1999,01,01);
                        datePickerDialog.show();
                    }
                });
                final EditText endDate= (EditText)dialog.findViewById(R.id.edit_endDate);
                endDate.setFocusable(false);
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog=new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(i,i1,i2);
                                endDate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },1999,01,01);
                        datePickerDialog.show();
                    }
                });
                final EditText adults = (EditText) dialog.findViewById(R.id.edit_adults);
                final EditText children = (EditText) dialog.findViewById(R.id.edit_children);
                final EditText mincost = (EditText) dialog.findViewById(R.id.edit_mincost);
                final EditText maxcost = (EditText) dialog.findViewById(R.id.edit_maxcost);
                RadioGroup isPrivate = (RadioGroup)dialog.findViewById(R.id.edit_isPrivate);
                final RadioButton privateTour = (RadioButton) dialog.findViewById(R.id.edit_privateTour);
                final RadioButton publicTour = (RadioButton) dialog.findViewById(R.id.edit_publicTour);
                Button btnUpdate = (Button) dialog.findViewById(R.id.edit_btnCreate);
                //tourName,tourMinCost,tourMaxCost,tourStartDate,tourEndDate,tourAdults,tourChilds,tourIsPrivate;
                tour_name.setText(tourName);
                mincost.setText(tourMinCost);
                maxcost.setText(tourMaxCost);
                startDate.setText(tourStartDate);
                endDate.setText(tourEndDate);
                adults.setText(tourAdults);
                children.setText(tourChilds);
                if(tourIsPrivate.equals("false")){
                    publicTour.setChecked(true);
                }else{
                    privateTour.setChecked(true);
                }

                dialog.show();

                final RequestQueue requestQueue= Volley.newRequestQueue(this);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtTourName=tour_name.getText().toString().trim();
                        String txtStartDate=startDate.getText().toString().trim();
                        String txtEndDate=endDate.getText().toString().trim();
                        String txtAdults=adults.getText().toString().trim();
                        String txtChildren = children.getText().toString().trim();
                        String txtMinCost = mincost.getText().toString().trim();
                        String txtMaxCost = maxcost.getText().toString().trim();
                        long mStartDate =0,mEndDate =0;
                        Date startD, endD;
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");


                        try {
                            startD = sdf.parse(txtStartDate);
                            endD = sdf.parse(txtEndDate);
                            mStartDate = startD.getTime();
                            mEndDate = endD.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                        String private_select="1";

                        if (privateTour.isChecked())
                            private_select="1";
                        if (publicTour.isChecked())
                            private_select="0";

                        String URL = "http://35.197.153.192:3000/tour/update-tour";
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("id",idOfTour);
                        params.put("name", txtTourName);
                        params.put("startDate", mStartDate+"");
                        params.put("endDate",mEndDate+"");
                        params.put("isPrivate",private_select);
                        params.put("adults",txtAdults);
                        params.put("childs",txtChildren);
                        params.put("minCost",txtMinCost);
                        params.put("maxCost",txtMaxCost);

                        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(TourDetail.this, "successs"+idOfTour, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null) {
                                    String statusCode=String.valueOf(networkResponse.statusCode);
                                    switch(statusCode){
                                        case "400":
                                            Toast.makeText(TourDetail.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "500":
                                            Toast.makeText(TourDetail.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(TourDetail.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                        requestQueue.add(request_json);;
                    }
                });
                break;
            case R.id.delete_tour:
                final RequestQueue requestQueue2= Volley.newRequestQueue(this);
                String URL = "http://35.197.153.192:3000/tour/update-tour";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("status","-1");
                params.put("id",idOfTour);
                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Intent intent = new Intent(TourDetail.this,Home.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            String statusCode=String.valueOf(networkResponse.statusCode);
                            switch(statusCode){
                                case "400":
                                    Toast.makeText(TourDetail.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                                    break;
                                case "500":
                                    Toast.makeText(TourDetail.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(TourDetail.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                break;
        }
        return true;
    }

}
