package anhem1nha.shashank.platform.fancyloginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apptour.anhem1nha.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentSetting extends Fragment {
    Button loginOut;
    ImageView profileAvatar;
    TextView profileName,editProfile;
    FragmentDetailProfile fragmentDetailProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        Spinner spinner=(Spinner) rootView.findViewById(R.id.language);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
        //     R.array.languages, android.R.layout.simple_spinner_dropdown_item);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.settings_title);

        loginOut = (Button) rootView.findViewById(R.id.logout_button);
        profileAvatar=(ImageView) rootView.findViewById(R.id.profile_image);
        profileName=(TextView) rootView.findViewById(R.id.profile_name);
        editProfile=(TextView)rootView.findViewById(R.id.edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDetailProfile = new FragmentDetailProfile();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,fragmentDetailProfile);
                fragmentTransaction.commit();
            }
        });

        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDetailProfile = new FragmentDetailProfile();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame,fragmentDetailProfile);
                fragmentTransaction.commit();
            }
        });

        if(!LoginPage.avatar.isEmpty())
            Picasso.get().load(LoginPage.avatar).into(profileAvatar);
        if(!LoginPage.fullName.isEmpty()) {
            profileName.setText(LoginPage.fullName);
        }else{
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
            String URL = "http://35.197.153.192:3000/user/info";
            JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.GET, URL,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String fullName=response.getString("fullName");
                                profileName.setText(fullName);
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
                    //headers.put("Content-Type", "application/json");
                    headers.put("Authorization", LoginPage.token);
                    return headers;
                }
            };
            requestQueue.add(request_json);
        }

        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginPage.token="";
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",LoginPage.token);
                editor.apply();

                LoginPage.avatar="";
                LoginPage.fullName="";

                Intent intent = new Intent(getActivity(), LoginPage.class);
                startActivity(intent);
            }
        });
        String[] a=new String[]{
                "Vietnamese",
                "English"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                rootView.getContext(),R.layout.spinner_items,a
        );
        spinner.setAdapter(adapter);
        return rootView;
    }
}
