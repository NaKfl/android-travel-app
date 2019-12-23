package com.ygaps.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    Button via_sms,via_email,submit,submit_recovery;
    EditText input,newpassword,otp;
    String type="phone";
    int check=0;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        via_sms=(Button) findViewById(R.id.via_sms);
        via_email=(Button) findViewById(R.id.via_email);
        submit=(Button) findViewById(R.id.submit_forgot_password);
        input=(EditText) findViewById(R.id.input);

        via_email.setBackgroundResource(R.drawable.buttoshapehint);
        via_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                via_email.setBackgroundResource(R.drawable.buttonshape);
                via_sms.setBackgroundResource(R.drawable.buttoshapehint);
                input.setHint(R.string.input_email);
                type="email";
            }
        });
        via_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                via_sms.setBackgroundResource(R.drawable.buttonshape);
                via_email.setBackgroundResource(R.drawable.buttoshapehint);
                input.setHint(R.string.input_phone);
                type="phone";
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestOTP();
                if(check==1){
                    setContentView(R.layout.password_recovery);
                    newpassword=(EditText) findViewById(R.id.newPW);
                    otp=(EditText) findViewById(R.id.OTP);
                    submit_recovery=(Button) findViewById(R.id.submit_recovery_password);
                    submit_recovery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recovery_passsword();
                            check=0;
                            Toast.makeText(ForgotPassword.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgotPassword.this,LoginPage.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });


    }
    public void recovery_passsword(){
        final RequestQueue requestQueue= Volley.newRequestQueue(ForgotPassword.this);
        String URL="http://35.197.153.192:3000/user/verify-otp-recovery";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", userID);
        params.put("newPassword", newpassword.getText().toString());
        params.put("verifyCode",otp.getText().toString());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        check=1;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "403":
                            Toast.makeText(ForgotPassword.this, "Wrong OTP or your OTP expired", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(request);
    }
    public void requestOTP(){
        final RequestQueue requestQueue= Volley.newRequestQueue(ForgotPassword.this);
        String URL="http://35.197.153.192:3000/user/request-otp-recovery";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        params.put("value", input.getText().toString());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            userID=response.getString("userId");
                            check=1;
                        } catch (JSONException e) {
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
                            Toast.makeText(ForgotPassword.this, "Bad request", Toast.LENGTH_SHORT).show();
                            break;
                        case "404":
                            Toast.makeText(ForgotPassword.this, "EMAIL/PHONE doesn't exist", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ForgotPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        requestQueue.add(request);
    }
}
