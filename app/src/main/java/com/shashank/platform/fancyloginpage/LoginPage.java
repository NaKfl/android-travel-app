package com.shashank.platform.fancyloginpage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginPage extends AppCompatActivity {
    public static String token;
    Boolean isSignIn=true;
    TextView signin,signup,signin_signup_txt,forgot_password;
    CircleImageView circleImageView;
    Button signin_signup_btn;
    EditText emailPhone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        signin_signup_txt = findViewById(R.id.signin_signup_txt);
        forgot_password = findViewById(R.id.forgot_password);
        circleImageView = findViewById(R.id.circleImageView);
        signin_signup_btn = findViewById(R.id.signin_signup_btn);
        emailPhone=(EditText) findViewById(R.id.emailPhone);
        password=(EditText) findViewById(R.id.password);
        final RequestQueue requestQueue= Volley.newRequestQueue(this);

        //Khi bấm vào tag SingIn
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin.setTextColor(Color.parseColor("#FFFFFF"));
                signin.setBackgroundColor(Color.parseColor("#FF2729C3"));
                signup.setTextColor(Color.parseColor("#FF2729C3"));
                signup.setBackgroundResource(R.drawable.bordershape);
                circleImageView.setImageResource(R.drawable.sigin_boy_img);
                signin_signup_txt.setText("Sign In");
                signin_signup_btn.setText("Sign In");
                forgot_password.setVisibility(View.VISIBLE);
                emailPhone.setText("");
                password.setText("");
                isSignIn=true;
            }
        });

        //Khi bấm vào tag SingUp
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setTextColor(Color.parseColor("#FFFFFF"));
                signup.setBackgroundColor(Color.parseColor("#FF2729C3"));
                signin.setTextColor(Color.parseColor("#FF2729C3"));
                signin.setBackgroundResource(R.drawable.bordershape);
                circleImageView.setImageResource(R.drawable.sigup_boy_img);
                signin_signup_txt.setText("Sign Up");
                signin_signup_btn.setText("Sign Up");
                forgot_password.setVisibility(View.INVISIBLE);
                emailPhone.setText("");
                password.setText("");
                isSignIn=false;
            }
        });

        //Khi bấm vào Button SignIn/SignUp
        signin_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isSignIn){
                   String txtEmailPhone=emailPhone.getText().toString().trim();
                   String txtPassword=password.getText().toString().trim();

                   String URL = "http://35.197.153.192:3000/user/login";
                   HashMap<String, String> params = new HashMap<String, String>();
                   params.put("emailPhone", txtEmailPhone);
                   params.put("password", txtPassword);

                   JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                           new Response.Listener<JSONObject>() {
                               @Override
                               public void onResponse(JSONObject response) {
                                   try {
                                       token=response.getString("token");
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                       }
                   });
                   requestQueue.add(request_json);
               }else{
                   Toast.makeText(LoginPage.this, "SignUp", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}
