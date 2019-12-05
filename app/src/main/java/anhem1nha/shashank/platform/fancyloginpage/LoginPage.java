package anhem1nha.shashank.platform.fancyloginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginPage extends AppCompatActivity {
    public static String token="";
    public static String avatar="",fullName="";
    Boolean isSignIn=true;
    TextView signin,signup,signin_signup_txt,forgot_password;
    CircleImageView circleImageView;
    Button signin_signup_btn;
    EditText emailPhone, password;

    //Login facebook
    String fbName,fbEmail,fbAvatar;
    LoginButton loginButtonFacebook;
    CallbackManager callbackManager;
    // Login GG
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    SignInButton signInButton;
    SignInButton signInButtonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        token=sharedPreferences.getString("token","");

        if(!token.isEmpty()) {
            Intent intent = new Intent(LoginPage.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        signin_signup_txt = findViewById(R.id.signin_signup_txt);
        forgot_password = findViewById(R.id.forgot_password);
        circleImageView = findViewById(R.id.circleImageView);
        signin_signup_btn = findViewById(R.id.signin_signup_btn);
        emailPhone= findViewById(R.id.emailPhone);
        password= findViewById(R.id.password);
        loginButtonFacebook= findViewById(R.id.login_Button_Facebook);
        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        emailPhone.setHint("Email or Phone");
        password.setHint("Password");
        emailPhone.setText("0944026115");
        password.setText("369258");

        signInButtonGoogle = findViewById(R.id.sign_in_button);
        TextView googleTitle = (TextView) signInButtonGoogle.getChildAt(0);
        googleTitle.setText(R.string.google_login_title);

        if(token==""){
            LoginManager.getInstance().logOut();
        }
        callbackManager=CallbackManager.Factory.create();
        loginButtonFacebook.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("30016777562-qk7umu04d9k3m1kk27drr5ajqa029bfi.apps.googleusercontent.com")
                .requestServerAuthCode("30016777562-qk7umu04d9k3m1kk27drr5ajqa029bfi.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //quên mật khẩu
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
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
                emailPhone.setHint("Email or Phone");
                forgot_password.setVisibility(View.VISIBLE);
                emailPhone.setText("");
                password.setText("");
                loginButtonFacebook.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
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
                signin_signup_btn.setText("Next");
                emailPhone.setHint("Email");
                forgot_password.setVisibility(View.INVISIBLE);
                loginButtonFacebook.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
                emailPhone.setText("");
                password.setText("");
                isSignIn=false;
            }
        });

        //Khi bấm vào Button SignIn/SignUp
        signin_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmailPhone=emailPhone.getText().toString().trim();

                String txtPassword=password.getText().toString().trim();
                if(isSignIn){
                    String URL = "http://35.197.153.192:3000/user/login";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("emailPhone", txtEmailPhone);
                    params.put("password", txtPassword);

                    JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        LoginPage.token=response.getString("token");

                                        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("token",LoginPage.token);
                                        editor.apply();

                                        Intent intent = new Intent(LoginPage.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            Toast.makeText(LoginPage.this,"abc",Toast.LENGTH_SHORT).show();
                            if (networkResponse != null) {
                                String statusCode=String.valueOf(networkResponse.statusCode);
                                switch(statusCode){
                                    case "400":
                                        Toast.makeText(LoginPage.this, "Missing email/phone or password", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "404":
                                        Toast.makeText(LoginPage.this, "Wrong email/phone or password", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    });
                    requestQueue.add(request_json);
                }else{
                    if (!TextUtils.isEmpty(txtEmailPhone) && !TextUtils.isEmpty(txtPassword))
                    {
                        if (isValidEmailId(txtEmailPhone)) {
                            Intent intent = new Intent(LoginPage.this,RegisterPage.class);
                            intent.putExtra("email",txtEmailPhone);
                            intent.putExtra("password",txtPassword);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginPage.this,"Invalid Email",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(LoginPage.this,"Fill empty",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
            callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            String idToken = account.getIdToken();
            // Signed in successfully, show authenticated UI.
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            String URL = "https://www.googleapis.com/oauth2/v4/token";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", "30016777562-qk7umu04d9k3m1kk27drr5ajqa029bfi.apps.googleusercontent.com");
            params.put("client_secret","BOyRgPD-5XwSiZTwnkXA5gOX");
            params.put("redirect_uri","");
            params.put("code",authCode);
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                token=response.getString("access_token");
                                Intent intent = new Intent(LoginPage.this,Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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
                                Toast.makeText(LoginPage.this, "Phone/Email already registered", Toast.LENGTH_SHORT).show();
                                break;
                            case "503":
                                Toast.makeText(LoginPage.this, "Server error on creating user", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            requestQueue.add(request_json);

        } catch (ApiException e) {
            Log.w("AA", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    AccessTokenTracker tokenTracker= new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                fbName ="";
                fbEmail="";
                Toast.makeText(LoginPage.this, "User Logged out", Toast.LENGTH_SHORT).show();
            }else{
                token=currentAccessToken.getToken();
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",token);
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(final AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");
                    String image_url="https://graph.facebook.com/"+id+"/picture?type=normal";

                    fbEmail=email;
                    fbName=first_name+" "+last_name;
                    fbAvatar=image_url;
                    avatar=fbAvatar;
                    fullName=fbName;

                    RequestQueue requestQueue= Volley.newRequestQueue(LoginPage.this);

                    String URL = "http://35.197.153.192:3000/user/login/by-facebook";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("accessToken", newAccessToken.getToken());
                    JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        LoginPage.token=response.getString("token");

                                        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("token",LoginPage.token);
                                        editor.apply();

                                        Intent intent = new Intent(LoginPage.this,Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
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
                                        Toast.makeText(LoginPage.this, "Missing email/phone or password", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "404":
                                        Toast.makeText(LoginPage.this, "Wrong email/phone or password", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    requestQueue.add(request_json);

//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.dontAnomate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
