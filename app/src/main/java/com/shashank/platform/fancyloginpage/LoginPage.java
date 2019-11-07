package com.shashank.platform.fancyloginpage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginPage extends AppCompatActivity {

    TextView signin,signup,signin_signup_txt,forgot_password;
    CircleImageView circleImageView;
    Button signin_signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signin_signup_txt = findViewById(R.id.signin_signup_txt);
        signin_signup_btn = findViewById(R.id.signin_signup_btn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin.setTextColor(Color.parseColor("#FFFFFF"));
                signin.setBackgroundColor(Color.parseColor("#FF2729C3"));
                signup.setTextColor(Color.parseColor("#FF2729C3"));
                signup.setBackgroundResource(R.drawable.bordershape);
                signin_signup_txt.setText("Sign In");
                signin_signup_btn.setText("Sign In");
            }
        });
    }
}
