package com.ygaps.travelapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.ygaps.travelapp.Adapter.AdapterComment;
import com.ygaps.travelapp.Adapter.AdapterFeedback;
import com.ygaps.travelapp.Adapter.AdapterMember;
import com.ygaps.travelapp.Adapter.AdapterReview;
import com.ygaps.travelapp.Adapter.AdapterStopPoint;
import com.ygaps.travelapp.Modal.Comment;
import com.ygaps.travelapp.Modal.Feedback;
import com.ygaps.travelapp.Modal.Member;
import com.ygaps.travelapp.Modal.Review;
import com.ygaps.travelapp.Modal.StopPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TourDetail extends AppCompatActivity {
    ArrayList<StopPoint> stopPoints=new ArrayList<StopPoint>();
    ArrayList<Comment> comments=new ArrayList<Comment>();
    ArrayList<Member> members=new ArrayList<Member>();
    ArrayList<Member> searchMemberArray=new ArrayList<Member>();
    ArrayList <Review> reviews=new ArrayList<Review>();

    TextView nameOfTour, dateOfTour, peopleOfTour, cashOfTour, isPrivateTour;
    TextView stopPointEmpty, commentEmpty, memberEmpty, memberListEmpty;
    ListView listStopPoint,listComment,listMember,listReview, searchMemberList;
    EditText searchMemberInput;
    ImageView lockIcon;
    private ArrayList<Feedback> feedbacks = new ArrayList<>();

    public static EditText address_stoppoint;
    public static double mNewLat, mNewLong;
    public static String mNewAddress, mNewProvince = "-1";
    private String[] ServiceType = new String[]{
            "Restaurant",
            "Hotel",
            "Rest Station",
            "Other"
    };
    public static String idOfTour;
    String isMyTour, tourName, tourMinCost, tourMaxCost, tourStartDate, tourEndDate, tourAdults, tourChilds, tourIsPrivate;
    int flagGoMap = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tour_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.tour_detail);

        Intent intent = getIntent();
        idOfTour = intent.getStringExtra("tourId");
        isMyTour = intent.getStringExtra("isMyTour");
        getPointOfTour(idOfTour);
        TextView follow_action = (TextView) findViewById(R.id.follow_action);
        follow_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TourDetail.this,FollowActivity.class);
                startActivity(intent);
            }
        });
        nameOfTour=(TextView)findViewById(R.id.tour_detail_destination);
        dateOfTour=(TextView)findViewById(R.id.tour_detail_datetodate);
        peopleOfTour=(TextView)findViewById(R.id.tour_detail_people);
        cashOfTour=(TextView)findViewById(R.id.tour_detail_cash);
        isPrivateTour=(TextView)findViewById(R.id.tour_detail_private);
        listStopPoint=(ListView)findViewById(R.id.list_stop_point);
        listMember=(ListView)findViewById(R.id.list_member);
        lockIcon=(ImageView)findViewById(R.id.tour_detail_lock_icon);
        ImageView rate = (ImageView) findViewById(R.id.rate);
        ImageView addMember = (ImageView) findViewById(R.id.add_member);
        if(isMyTour.equals("0")){
            addMember.setVisibility(View.GONE);
        }

        //đánh giá
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(TourDetail.this);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_review);
                final RatingBar ratingBar = dialog.findViewById(R.id.rating);
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                final EditText reviewText = (EditText) dialog.findViewById(R.id.input_review);
                Button btnReview = (Button) dialog.findViewById(R.id.btn_send_review);
                listReview = (ListView) dialog.findViewById(R.id.list_review);
                setAdapterReview();
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        ratingBar.setRating(rating);
                    }
                });
                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float Rate = ratingBar.getRating();
                        if (Rate < 2) {
                            Rate = 2;
                        }

                        Toast.makeText(TourDetail.this, Rate + "", Toast.LENGTH_SHORT).show();
                        int rate = Math.round(Rate);
                        String review = reviewText.getText().toString();
                        final RequestQueue requestQueue = Volley.newRequestQueue(TourDetail.this);
                        String URL = "http://35.197.153.192:3000/tour/add/review";
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("tourId", idOfTour);
                        params.put("review", review);
                        params.put("point", rate + "");
                        Toast.makeText(TourDetail.this, review + " " + Rate, Toast.LENGTH_SHORT);
                        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(TourDetail.this, "review thành công", Toast.LENGTH_SHORT).show();
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
                                    String statusCode = String.valueOf(networkResponse.statusCode);
                                    switch (statusCode) {
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
                        }) {
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
                dialog.show();
            }
        });

        //Thêm member
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(TourDetail.this);
                dialog.setContentView(R.layout.popup_add_member);
                searchMemberInput=(EditText)dialog.findViewById(R.id.search_member_input);
                searchMemberList=(ListView)dialog.findViewById(R.id.search_member_list);
                memberListEmpty = (TextView)dialog.findViewById(R.id.add_member_empty);
                searchMemberInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String keyword = s.toString().trim();
                            final RequestQueue requestQueueAddMember = Volley.newRequestQueue(TourDetail.this);
                            String URL = "http://35.197.153.192:3000/user/search?searchKey=" + keyword + "&pageIndex=1&pageSize=" + Integer.MAX_VALUE;
                            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String total = response.getString("total");
                                                JSONArray jsonArray = response.getJSONArray("users");
                                                searchMemberArray.clear();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject member = jsonArray.getJSONObject(i);
                                                    String id = member.getString("id");
                                                    String name = member.getString("fullName");
                                                    String phone = member.getString("phone");
                                                    String avatar = member.getString("avatar");
                                                    Member temp = new Member(id, name, phone, avatar, "");
                                                    searchMemberArray.add(temp);
                                                }
                                                if (searchMemberArray.isEmpty()) {
                                                    memberListEmpty.setVisibility(View.VISIBLE);
                                                } else {
                                                    memberListEmpty.setVisibility(View.GONE);
                                                }
                                                AdapterMember adapter = new AdapterMember(TourDetail.this, R.layout.member_single, searchMemberArray);
                                                searchMemberList.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                                searchMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        final RequestQueue requestQueue= Volley.newRequestQueue(TourDetail.this);
                                                        String URL = "http://35.197.153.192:3000/tour/add/member";
                                                        HashMap<String, String> params = new HashMap<String, String>();
                                                        params.put("tourId", idOfTour);
                                                        Toast.makeText(TourDetail.this, idOfTour, Toast.LENGTH_SHORT).show();
                                                        params.put("invitedUserId", searchMemberArray.get(position).getId());
                                                        params.put("isInvited", "false");

                                                        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                                                                new Response.Listener<JSONObject>() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        Toast.makeText(TourDetail.this, "Send invitation success", Toast.LENGTH_SHORT).show();
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
                                                                headers.put("Authorization", LoginPage.token);
                                                                return headers;
                                                            }
                                                        };
                                                        requestQueue.add(request_json);
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(TourDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    HashMap<String, String> headers = new HashMap<String, String>();
                                    headers.put("Authorization", LoginPage.token);
                                    return headers;
                                }
                            };
                            requestQueueAddMember.add(request_json);
                        }
                });
                dialog.show();
            }
        });


        listStopPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog ShowInfor = new Dialog(TourDetail.this);
                ShowInfor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ShowInfor.setContentView(R.layout.popup_stp);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(ShowInfor.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                ShowInfor.getWindow().setAttributes(lp);
                ImageView edit_infor = (ImageView) ShowInfor.findViewById(R.id.edit_showinfo);
                ImageView delete_infor = (ImageView) ShowInfor.findViewById(R.id.delete_showinfor);


                TextView name_stp_detail =(TextView) ShowInfor.findViewById(R.id.name_stp_detail);
                TextView stp_address_detail = (TextView) ShowInfor.findViewById(R.id.stp_address_detail);
                TextView stp_cost_detail = (TextView) ShowInfor.findViewById(R.id.stp_cost_detail);
                TextView time_infor = (TextView) ShowInfor.findViewById(R.id.time_infor);
                TextView name_service_infor = (TextView)ShowInfor.findViewById(R.id.name_service_infor);
                ImageView send_feedback = (ImageView) ShowInfor.findViewById(R.id.send_feedback);

                send_feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog_send = new Dialog(TourDetail.this);
                        dialog_send.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_send.setContentView(R.layout.popup_feedback);

                        final RatingBar ratingBar2 = dialog_send.findViewById(R.id.rating);
                        LayerDrawable stars = (LayerDrawable) ratingBar2.getProgressDrawable();
                        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                        final EditText feedText = (EditText) dialog_send.findViewById(R.id.input_review);
                        Button btnSend = (Button) dialog_send.findViewById(R.id.btn_send_review);

                        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            public void onRatingChanged(RatingBar ratingBar, float rating,
                                                        boolean fromUser) {
                                ratingBar.setRating(rating);
                            }
                        });
                        btnSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                float Ratefeed = ratingBar2.getRating();
                                if (Ratefeed < 2) {
                                    Ratefeed = 2;
                                }
                                int Ratefeedround = Math.round(Ratefeed);
                                String feed = feedText.getText().toString();
                                sendFeedBack(dialog_send,stopPoints.get(position),feed,Ratefeedround);
                                listFeedback(ShowInfor,stopPoints.get(position));
                                listFeedback(ShowInfor,stopPoints.get(position));
                            }
                        });
                        dialog_send.show();
                    }
                });

                name_stp_detail.setText(stopPoints.get(position).getName());
                String serviceType = stopPoints.get(position).getServiceTypeId();

                if (serviceType.equals("1")) {
                    name_service_infor.setText("Restaurant");
                } else if (serviceType.equals("2")) {
                    name_service_infor.setText("Hotel");
                } else if (serviceType.equals("3")) {
                    name_service_infor.setText("Rest Station");
                } else {
                    name_service_infor.setText("Other");
                }
                stp_address_detail.setText(stopPoints.get(position).getAddress());

                stp_cost_detail.setText(stopPoints.get(position).getMinCost() + " - " +stopPoints.get(position).getMinCost());

                time_infor.setText(stopPoints.get(position).getArrivalAt() + " to " + stopPoints.get(position).getLeaveAt());


                final ListView listView = (ListView) ShowInfor.findViewById(R.id.list_stp);

                pointTB(ShowInfor,stopPoints.get(position));
                listFeedback(ShowInfor,stopPoints.get(position));

                delete_infor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestQueue requestQueue = Volley.newRequestQueue(TourDetail.this);
                        String URL = "http://35.197.153.192:3000/tour/remove-stop-point?stopPointId=" + stopPoints.get(position).getId();
                        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(TourDetail.this, "Xoa thanh cong", Toast.LENGTH_LONG).show();
                                        ShowInfor.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(TourDetail.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
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

                edit_infor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog Updatedialog = new Dialog(TourDetail.this);
                        Updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Updatedialog.setContentView(R.layout.popup_detail_stoppoint);
                        ImageView close_dialog = (ImageView) Updatedialog.findViewById(R.id.close);
                        ImageView delete_stop = (ImageView) Updatedialog.findViewById(R.id.delete_stoppoint);
                        close_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Updatedialog.dismiss();
                            }
                        });



                        final Spinner spinService = (Spinner) Updatedialog.findViewById(R.id.up_service_type);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(TourDetail.this, R.layout.spinner_items, ServiceType);
                        spinService.setAdapter(arrayAdapter);

                        final EditText stop_point_name = (EditText) Updatedialog.findViewById(R.id.up_stop_point_name);
                        address_stoppoint = (EditText) Updatedialog.findViewById(R.id.up_address_stoppoint);
                        final EditText mincost_stop = (EditText) Updatedialog.findViewById(R.id.up_mincost_stop);
                        final EditText maxcost_stop = (EditText) Updatedialog.findViewById(R.id.up_maxcost_stop);
                        Button btnUpdate = (Button) Updatedialog.findViewById(R.id.update_btn);

                        final EditText dateArrive = (EditText) Updatedialog.findViewById(R.id.up_dateArrive);
                        final EditText dateLeave = (EditText) Updatedialog.findViewById(R.id.up_dateLeave);


                        stop_point_name.setText(stopPoints.get(position).getName());
                        spinService.setSelection(Integer.parseInt(stopPoints.get(position).getServiceTypeId()) - 1);
                        address_stoppoint.setText(stopPoints.get(position).getAddress());

                        mincost_stop.setText(stopPoints.get(position).getMinCost());
                        maxcost_stop.setText(stopPoints.get(position).getMaxCost());
                        dateArrive.setText(stopPoints.get(position).getArrivalAt());
                        dateLeave.setText(stopPoints.get(position).getLeaveAt());

                        dateArrive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog = new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        calendar.set(i, i1, i2);
                                        dateArrive.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                }, 1999, 01, 01);
                                datePickerDialog.show();
                            }
                        });

                        dateLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog = new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        calendar.set(i, i1, i2);
                                        dateLeave.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                }, 1999, 01, 01);
                                datePickerDialog.show();
                            }
                        });
                        dateArrive.setFocusable(false);
                        dateLeave.setFocusable(false);

                        address_stoppoint.setFocusable(false);
                        delete_stop.setVisibility(View.GONE);


                        address_stoppoint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(TourDetail.this, MapActivity.class);
                                intent.putExtra("isUpdate", "1");
                                flagGoMap=1;
                                startActivity(intent);
                            }
                        });

                        Updatedialog.show();

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String namePoint = stop_point_name.getText().toString();
                                String minCost = mincost_stop.getText().toString().trim();
                                String maxCost = maxcost_stop.getText().toString().trim();
                                int mType;
                                if (spinService.getSelectedItem().toString().equals("Restaurant")) {
                                    mType = 1;
                                } else if (spinService.getSelectedItem().toString().equals("Hotel")) {
                                    mType = 2;
                                } else if (spinService.getSelectedItem().toString().equals("Rest Station")) {
                                    mType = 3;
                                } else {
                                    mType = 4;
                                }


                                long mArrive = 0, mLeave = 0;
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
                                String addressFinal = address_stoppoint.getText().toString();
                                double latPost = Double.parseDouble(stopPoints.get(position).getLat());
                                double longPost = Double.parseDouble(stopPoints.get(position).getLongitude());

                                if (flagGoMap==1)
                                {
                                    latPost=mNewLat;
                                    longPost=mNewLong;
                                    flagGoMap=0;
                                }
                                JSONObject jsonPoint = new JSONObject();
                                try {
                                    jsonPoint.put("id", stopPoints.get(position).getId());
                                    jsonPoint.put("name", namePoint);
                                    jsonPoint.put("address", addressFinal);
                                    jsonPoint.put("serviceTypeId", mType);
                                    jsonPoint.put("lat", latPost + "");
                                    jsonPoint.put("long", longPost + "");
                                    jsonPoint.put("provinceId", mNewProvince);
                                    jsonPoint.put("arrivalAt", mArrive + "");
                                    jsonPoint.put("leaveAt", mLeave + "");
                                    jsonPoint.put("minCost", minCost);
                                    jsonPoint.put("maxCost", maxCost);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray jsonArrayPoint = new JSONArray();
                                jsonArrayPoint.put(jsonPoint);


                                final JSONObject json_post = new JSONObject();
                                try {
                                    json_post.put("tourId", idOfTour);
                                    json_post.put("stopPoints", jsonArrayPoint);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("AAAA", json_post.toString());
                                final String requestBody = json_post.toString();
                                final RequestQueue requestQueue = Volley.newRequestQueue(TourDetail.this);
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
                                        Toast.makeText(TourDetail.this, "" + error, Toast.LENGTH_LONG).show();
                                    }
                                }) {
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
                        // het on click
                    }
                });



                ShowInfor.show();

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/tour/info?tourId=" + idOfTour;

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String tourId = response.getString("id");
                            String tourHostId = response.getString("hostId");
                            String tourStatus = response.getString("status");
                            tourName = response.getString("name");
                            tourMinCost = response.getString("minCost");
                            tourMaxCost = response.getString("maxCost");
                            tourStartDate = response.getString("startDate");
                            tourEndDate = response.getString("endDate");
                            tourAdults = response.getString("adults");
                            tourChilds = response.getString("childs");
                            tourIsPrivate = response.getString("isPrivate");
                            String tourAvatar = response.getString("avatar");

                            try {
                                long miliStartDate = Long.parseLong(tourStartDate);
                                Date startD = new Date(miliStartDate);
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                tourStartDate = dateFormat.format(startD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                long miliEndDate = Long.parseLong(tourEndDate);
                                Date endD = new Date(miliEndDate);
                                DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                tourEndDate = dateFormat1.format(endD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            nameOfTour.setText(tourName);
                            dateOfTour.setText("Start: " + tourStartDate + " - End: " + tourStartDate);
                            peopleOfTour.setText("Adult(s): " + tourAdults + " - Child(s): " + tourChilds);
                            cashOfTour.setText(tourMinCost + " VND - " + tourMaxCost + " VND");

                            if(tourIsPrivate.equals("true")) {
                                isPrivateTour.setText("Private");
                                lockIcon.setImageResource(R.drawable.lock);
                            }
                            else{
                                isPrivateTour.setText("Public");
                                lockIcon.setImageResource(R.drawable.unlock);
                            }

                            JSONArray jsonArrayStopPoint = response.getJSONArray("stopPoints");
                            stopPoints.clear();
                            for (int i = 0; i < jsonArrayStopPoint.length(); i++) {
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
                                try {
                                    long miliStartDate = Long.parseLong(arrivalAt);
                                    Date startD = new Date(miliStartDate);
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    arrivalAt = dateFormat.format(startD);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    long miliEndDate = Long.parseLong(leaveAt);
                                    Date endD = new Date(miliEndDate);
                                    DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                    leaveAt = dateFormat1.format(endD);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                StopPoint temp = new StopPoint(id, serviceId, address, name, arrivalAt, leaveAt, minCost, maxCost, serviceTypeId, avatar, lat, longitude);
                                stopPoints.add(temp);
                            }
                            if (stopPoints.isEmpty()) {
                                stopPointEmpty = (TextView) findViewById(R.id.stop_point_empty);
                                stopPointEmpty.setVisibility(View.VISIBLE);
                            } else {
                                if (stopPoints.size() >= 2) {
                                    listStopPoint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 360));
                                }
                                AdapterStopPoint adapterStopPoint = new AdapterStopPoint(TourDetail.this, R.layout.stop_point_single, stopPoints);
                                listStopPoint.setAdapter(adapterStopPoint);
                                adapterStopPoint.notifyDataSetChanged();
                            }

                            JSONArray jsonArrayMember = response.getJSONArray("members");
                            members.clear();
                            for (int i = 0; i < jsonArrayMember.length(); i++) {
                                JSONObject member = jsonArrayMember.getJSONObject(i);
                                String id = member.getString("id");
                                String name = member.getString("name");
                                String phone = member.getString("phone");
                                String avatar = member.getString("avatar");
                                String isHost = member.getString("isHost");

                                Member temp = new Member(id, name, phone, avatar, isHost);
                                members.add(temp);
                            }
                            if (members.isEmpty()) {
                                memberEmpty = (TextView) findViewById(R.id.member_empty);
                                memberEmpty.setVisibility(View.VISIBLE);
                            } else {
                                if (members.size() >= 2) {
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
        }) {
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
        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
        if (isMyTour.equals("0")) {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        } else {
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
                final EditText startDate = (EditText) dialog.findViewById(R.id.edit_startDate);
                startDate.setFocusable(false);
                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(i, i1, i2);
                                startDate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, 1999, 01, 01);
                        datePickerDialog.show();
                    }
                });
                final EditText endDate = (EditText) dialog.findViewById(R.id.edit_endDate);
                endDate.setFocusable(false);
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TourDetail.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(i, i1, i2);
                                endDate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, 1999, 01, 01);
                        datePickerDialog.show();
                    }
                });
                final EditText adults = (EditText) dialog.findViewById(R.id.edit_adults);
                final EditText children = (EditText) dialog.findViewById(R.id.edit_children);
                final EditText mincost = (EditText) dialog.findViewById(R.id.edit_mincost);
                final EditText maxcost = (EditText) dialog.findViewById(R.id.edit_maxcost);
                RadioGroup isPrivate = (RadioGroup) dialog.findViewById(R.id.edit_isPrivate);
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
                if (tourIsPrivate.equals("false")) {
                    publicTour.setChecked(true);
                } else {
                    privateTour.setChecked(true);
                }

                dialog.show();

                final RequestQueue requestQueue = Volley.newRequestQueue(this);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtTourName = tour_name.getText().toString().trim();
                        String txtStartDate = startDate.getText().toString().trim();
                        String txtEndDate = endDate.getText().toString().trim();
                        String txtAdults = adults.getText().toString().trim();
                        String txtChildren = children.getText().toString().trim();
                        String txtMinCost = mincost.getText().toString().trim();
                        String txtMaxCost = maxcost.getText().toString().trim();
                        long mStartDate = 0, mEndDate = 0;
                        Date startD, endD;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


                        try {
                            startD = sdf.parse(txtStartDate);
                            endD = sdf.parse(txtEndDate);
                            mStartDate = startD.getTime();
                            mEndDate = endD.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        String private_select = "1";

                        if (privateTour.isChecked())
                            private_select = "1";
                        if (publicTour.isChecked())
                            private_select = "0";

                        String URL = "http://35.197.153.192:3000/tour/update-tour";
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("id", idOfTour);
                        params.put("name", txtTourName);
                        params.put("startDate", mStartDate + "");
                        params.put("endDate", mEndDate + "");
                        params.put("isPrivate", private_select);
                        params.put("adults", txtAdults);
                        params.put("childs", txtChildren);
                        params.put("minCost", txtMinCost);
                        params.put("maxCost", txtMaxCost);

                        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(TourDetail.this, "successs" + idOfTour, Toast.LENGTH_SHORT).show();
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
                                    String statusCode = String.valueOf(networkResponse.statusCode);
                                    switch (statusCode) {
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
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                //headers.put("Content-Type", "application/json");
                                headers.put("Authorization", LoginPage.token);
                                return headers;
                            }
                        };
                        requestQueue.add(request_json);
                        ;
                    }
                });
                break;
            case R.id.delete_tour:
                final RequestQueue requestQueue2 = Volley.newRequestQueue(this);
                String URL = "http://35.197.153.192:3000/tour/update-tour";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("status", "-1");
                params.put("id", idOfTour);
                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Intent intent = new Intent(TourDetail.this, Home.class);

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
                            String statusCode = String.valueOf(networkResponse.statusCode);
                            switch (statusCode) {
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
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", LoginPage.token);
                        return headers;
                    }
                };
                requestQueue2.add(request_json);
                break;
        }
        return true;
    }

    public void getPointOfTour(String idOfTour) {
        final float[] pointresult = new float[1];
        final RequestQueue requestQueue3 = Volley.newRequestQueue(TourDetail.this);
        String URL = "http://35.197.153.192:3000/tour/get/review-point-stats?tourId=" + idOfTour;
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int point = 0;
                            int total = 0;
                            JSONArray pointStats = response.getJSONArray("pointStats");
                            for (int i = 0; i < pointStats.length(); i++) {
                                JSONObject tempPoint = pointStats.getJSONObject(i);
                                String Spoint = tempPoint.getString("point");
                                String Stotal = tempPoint.getString("total");
                                int Ipoint = Integer.parseInt(Spoint);
                                int Itotal = Integer.parseInt(Stotal);
                                if (Itotal != 0) {
                                    total = total + Itotal;
                                }

                                point = point + Ipoint * Itotal;

                            }

                            Toast.makeText(TourDetail.this, total + "", Toast.LENGTH_SHORT).show();
                            float rating = point/(total);
                            RatingBar ratingBar = (RatingBar) findViewById(R.id.pointOfTour);
                            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                            ratingBar.setRating(rating);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode = String.valueOf(networkResponse.statusCode);
                    switch (statusCode) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        Toast.makeText(TourDetail.this, pointresult[0] + "", Toast.LENGTH_SHORT).show();
        requestQueue3.add(request_json);
    }

    public void setAdapterReview() {

        final RequestQueue requestQueueReview = Volley.newRequestQueue(TourDetail.this);
        String URL = "http://35.197.153.192:3000/tour/get/review-list?tourId=" + idOfTour + "&pageIndex=1&pageSize=100";
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray reviewJson = response.getJSONArray("reviewList");
                            reviews.clear();
                            for (int i = 0; i < reviewJson.length(); i++) {
                                JSONObject obj = reviewJson.getJSONObject(i);
                                String name = obj.getString("name");
                                String review = obj.getString("review");
                                String createOn = obj.getString("createdOn");
                                Review tempReview = new Review(name, review, createOn);

                                reviews.add(tempReview);
                            }

                            AdapterReview adapterReview = new AdapterReview(TourDetail.this,R.layout.rate_single,reviews);
                            listReview.setAdapter(adapterReview);
                            adapterReview.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode = String.valueOf(networkResponse.statusCode);
                    switch (statusCode) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueueReview.add(request_json);
    }


    public static void SetTextAdress(double lat, double longitute, String provinceID, String address) {
        mNewProvince = provinceID;
        mNewAddress = address;
        mNewLat = lat;
        mNewLong = longitute;
        address_stoppoint.setText(address);
    }
    public void pointTB(final Dialog dialog, StopPoint stopPoint){
        final RequestQueue requestQueue4 = Volley.newRequestQueue(TourDetail.this);
        String URL = "http://35.197.153.192:3000/tour/get/feedback-point-stats?serviceId=" + stopPoint.getServiceId();
        Toast.makeText(TourDetail.this, stopPoint.getId()+"", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int point = 0;
                            int total = 0;
                            JSONArray pointStats = response.getJSONArray("pointStats");
                            for (int i = 0; i < pointStats.length(); i++) {
                                JSONObject tempPoint = pointStats.getJSONObject(i);
                                String Spoint = tempPoint.getString("point");
                                String Stotal = tempPoint.getString("total");
                                int Ipoint = Integer.parseInt(Spoint);
                                int Itotal = Integer.parseInt(Stotal);
                                if (Itotal != 0) {
                                    total = total + Itotal;
                                }

                                point = point + Ipoint * Itotal;

                            }
                            float rating = point/(total);
                            RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.pointOfStp);
                            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellowStart), PorterDuff.Mode.SRC_ATOP);
                            ratingBar.setRating(rating);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode = String.valueOf(networkResponse.statusCode);
                    switch (statusCode) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue4.add(request_json);
    }
    public void listFeedback(Dialog dialog,StopPoint stopPoint){
        String idOfservice = stopPoint.getServiceId();
        final ListView listFback = dialog.findViewById(R.id.list_stp_feedback);
        final RequestQueue requestQueue2 = Volley.newRequestQueue(TourDetail.this);
        String URL = "http://35.197.153.192:3000/tour/get/feedback-service?serviceId="+idOfservice+"&pageIndex=1&pageSize=999";

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("feedbackList");
                            feedbacks.clear();
                            for(int i =0 ;i<jsonArray.length();i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String feedback = jsonObject.getString("feedback");
                                String createdOn = jsonObject.getString("createdOn");
                                String point = jsonObject.getString("point");
                                Feedback temp = new Feedback(name,createdOn,point,feedback);
                                feedbacks.add(temp);
                            }
                            AdapterFeedback adapterFeedback = new AdapterFeedback(TourDetail.this,R.layout.feedback_single,feedbacks);
                            listFback.setAdapter(adapterFeedback);
                            adapterFeedback.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TourDetail.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", LoginPage.token);
                return headers;
            }
        };
        requestQueue2.add(request_json);
    }
    public void sendFeedBack(final Dialog dialog1, StopPoint stopPoint, String feedback, int point)
    {
        final int check=0;
        final RequestQueue requestQueue5 = Volley.newRequestQueue(TourDetail.this);
        String URL = "http://35.197.153.192:3000/tour/add/feedback-service";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("serviceId", stopPoint.getServiceId());
        params.put("feedback", feedback+"");
        params.put("point",point+"");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(TourDetail.this, "Send Thanh cong", Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(TourDetail.this,"ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(TourDetail.this,"ERROR 500", Toast.LENGTH_SHORT).show();
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
        requestQueue5.add(request_json);
    }
}
