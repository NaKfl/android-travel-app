package com.ygaps.travelapp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentDetailProfile extends Fragment {
    ImageView profileAvatar;
    TextView profileName,profileEmail,profilePhone,profileDob;
    RadioGroup profileGender;
    RadioButton profileMale;
    RadioButton profileFemale;
    Button profileUpdate, profileCancel;
    RequestQueue requestQueue;
    FragmentSetting fragmentSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_profile,container,false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.settings_title);

        profileAvatar=(ImageView) rootView.findViewById(R.id.detail_profile_avatar);
        profileName=(TextView) rootView.findViewById(R.id.detail_profile_name);
        profileEmail=(TextView) rootView.findViewById(R.id.detail_profile_email);
        profilePhone=(TextView) rootView.findViewById(R.id.detail_profile_phone);
        profileGender=(RadioGroup) rootView.findViewById(R.id.detail_profile_gender);
        profileMale=(RadioButton) rootView.findViewById(R.id.detail_profile_male);
        profileFemale=(RadioButton) rootView.findViewById(R.id.detail_profile_female);
        profileDob=(TextView) rootView.findViewById(R.id.detail_profile_dob);
        profileUpdate=(Button)rootView.findViewById(R.id.detail_profile_update);
        profileCancel=(Button)rootView.findViewById(R.id.detail_profile_cancel);

        profilePhone.setEnabled(false);
        profileEmail.setEnabled(false);
        profileDob.setFocusable(false);
        requestQueue= Volley.newRequestQueue(getActivity());

        profileDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(i,i1,i2);
                        profileDob.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },1999,01,01);
                datePickerDialog.show();
            }
        });
        profileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentSetting = new FragmentSetting();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,fragmentSetting);
                fragmentTransaction.commit();
            }
        });

            String URL = "http://35.197.153.192:3000/user/info";
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String fullName=response.getString("fullName");
                                String phone=response.getString("phone");
                                String gender=response.getString("gender");
                                String dob=response.getString("dob");
                                String email=response.getString("email");
                                String avatar=response.getString("avatar");

                                profileName.setText(fullName);
                                profileEmail.setText(email);
                                profilePhone.setText(phone);

                                if(avatar!="null")
                                    Picasso.get().load(LoginPage.avatar).into(profileAvatar);
                                if(gender.equals("1")){
                                    profileMale.setChecked(true);
                                }else if(gender.equals("0")){
                                    profileFemale.setChecked(true);
                                }

                                if(dob!="null"){
//                                    SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//                                    Date dt = sd1.parse(dob);
//                                    SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd");
//                                    String newDate = sd2.format(dt);
//                                    profileDob.setText(newDate);
                                    profileDob.setText(dob.replace("T00:00:00.000Z",""));
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
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


        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName=profileName.getText().toString().trim();
                String txtPhone=profilePhone.getText().toString().trim();
                String txtDate=profileDob.getText().toString().trim();
                String txtEmail = profileEmail.getText().toString().trim();

                String gender_select="-1";
                if (profileMale.isChecked())
                    gender_select="1";
                if (profileFemale.isChecked())
                    gender_select="0";

                String URL = "http://35.197.153.192:3000/user/edit-info";
                HashMap<String, String> params = new HashMap<String, String>();
                if(!txtName.isEmpty())
                params.put("fullName",txtName);
//                if(!txtEmail.isEmpty())
//                    params.put("email", txtEmail);
//                if(!txtPhone.isEmpty())
//                params.put("phone",txtPhone);
                if(!txtDate.isEmpty())
                params.put("dob",txtDate);
                if(gender_select!="-1")
                params.put("gender",gender_select);

                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getActivity(),"Update Success",Toast.LENGTH_SHORT).show();
                                fragmentSetting = new FragmentSetting();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.main_frame,fragmentSetting);
                                fragmentTransaction.commit();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            String statusCode=String.valueOf(networkResponse.statusCode);
                            switch(statusCode){
                                case "400":
                                    Toast.makeText(getContext(), "Phone/Email already registered", Toast.LENGTH_SHORT).show();
                                    break;
                                case "503":
                                    Toast.makeText(getContext(), "Server error on creating user", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
        });


        return rootView;

    }
}
