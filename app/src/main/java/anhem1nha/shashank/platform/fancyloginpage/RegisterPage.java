package anhem1nha.shashank.platform.fancyloginpage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apptour.anhem1nha.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    EditText name,phone,address,dayofbirth;
    TextView text_gender;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        address= findViewById(R.id.address);
        dayofbirth= findViewById(R.id.dayofbirth);
        text_gender= findViewById(R.id.text_gender);
        gender= findViewById(R.id.gender);
        male= findViewById(R.id.male);
        female= findViewById(R.id.female);
        signup= findViewById(R.id.signup_btn);
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        text_gender.setFocusable(false);
        dayofbirth.setFocusable(false);
        dayofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime();
            }
        });

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName=name.getText().toString().trim();
                String txtPhone=phone.getText().toString().trim();
                String txtAddress=address.getText().toString().trim();
                String txtDate=dayofbirth.getText().toString().trim();
                String txtEmail = getIntent().getStringExtra("email");
                String txtPassword = getIntent().getStringExtra("password");

                String gender_select="1";

                if (male.isChecked())
                    gender_select="1";
                if (female.isChecked())
                    gender_select="0";

                String URL = "http://35.197.153.192:3000/user/register";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", txtEmail);
                params.put("password", txtPassword);
                params.put("fullName",txtName);
                params.put("phone",txtPhone);
                params.put("address",txtAddress);
                params.put("dob",txtDate);
                params.put("gender",gender_select);

                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(RegisterPage.this,LoginPage.class);
                                startActivity(intent);
                                Toast.makeText(RegisterPage.this, "OK", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            String statusCode=String.valueOf(networkResponse.statusCode);
                            switch(statusCode){
                                case "400":
                                    Toast.makeText(RegisterPage.this, "Phone/Email already registered", Toast.LENGTH_SHORT).show();
                                    break;
                                case "503":
                                    Toast.makeText(RegisterPage.this, "Server error on creating user", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                requestQueue.add(request_json);
            }
        });
    }
    private void PickTime()
    {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(i,i1,i2);
                dayofbirth.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },1999,01,01);
        datePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
