package com.ygaps.travelapp;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    public static final String URL="http://35.197.153.192:3000/";
    private String body="";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map data=remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (data.isEmpty()) { // message type is notification.
            Log.d("data","isNull");
            showNotification("Invite to join a Tour",remoteMessage.getNotification().getBody());
        } else { // message type is data.

            StringBuilder noti=new StringBuilder();

            noti.append(data.get("hostName")).append(" invites you to Tour: ").append(data.get("name"));

            body= noti.toString();

            showNotification("Invite to join a Tour",body);

        }

    }

    private Intent createIntent(String actionName,int notificationID,String messaggeBody)
    {
        Intent intent=new Intent(this, Noti.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(actionName);
        Bundle guiThongbao=new Bundle();
        guiThongbao.putInt("NOTIFICATION_ID",notificationID);
        guiThongbao.putString("MESSAGE_BODY",messaggeBody);

        intent.putExtras(guiThongbao);

        return intent;
    }

    private void showNotification(String title,String messageBody) {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANEL_ID="dulich";
        int notificationID=new Random().nextInt();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("DoThanhDat");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent acceptIntent=createIntent(Noti.ACCEPT_ACTION,notificationID,messageBody);
        Intent decline=createIntent(Noti.DECLINE_ACTION,notificationID,messageBody);
        Intent show=createIntent(Noti.SHOW_ACTION,notificationID,messageBody);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,show,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuildder=new NotificationCompat.Builder(this,NOTIFICATION_CHANEL_ID);
        notificationBuildder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.invitation)
                .setContentIntent(pendingIntent)
                .setContentText(messageBody)
                .setContentTitle(title)
                .setContentInfo("Info")
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .addAction(new NotificationCompat.Action(android.R.drawable.sym_call_outgoing,
                        "Accept",
                        PendingIntent.getActivity(this,0,acceptIntent,PendingIntent.FLAG_CANCEL_CURRENT)))
                .addAction(new NotificationCompat.Action(android.R.drawable.sym_call_missed,
                        "Decline",
                        PendingIntent.getActivity(this,0,decline,PendingIntent.FLAG_CANCEL_CURRENT)));
        notificationManager.notify(notificationID,notificationBuildder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        String deviceId= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final RequestQueue requestQueue= Volley.newRequestQueue(this);
        String URL = "http://35.197.153.192:3000/user/notification/put-token";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("deviceId", deviceId);
        params.put("fcmToken", s+"");
        params.put("platform","1");
        params.put("appVersion","1.0");

        JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            Toast.makeText(MyFirebaseService.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String statusCode=String.valueOf(networkResponse.statusCode);
                    switch(statusCode){
                        case "400":
                            Toast.makeText(MyFirebaseService.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(MyFirebaseService.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MyFirebaseService.this, "ERROR", Toast.LENGTH_SHORT).show();
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

//        Gson gson=new GsonBuilder().serializeNulls().create();
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
//
//
//        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        SharedPreferences sharedPreferences = getSharedPreferences("com.ygaps.travel", Context.MODE_PRIVATE);
//        String token_user = sharedPreferences.getString("token_user","");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("Authorization",token_user);
//
//        SendTokenFirebaseToSever_Data sendTokenFirebaseToSever_data=new SendTokenFirebaseToSever_Data(s,device_id,1,"1.0");
//
//        Call<SendTokenFirebaseToServer_Result> call=jsonPlaceHolderApi.sendTokenToServer(map,sendTokenFirebaseToSever_data);
//        call.enqueue(new Callback<SendTokenFirebaseToServer_Result>() {
//            @Override
//            public void onResponse(Call<SendTokenFirebaseToServer_Result> call, Response<SendTokenFirebaseToServer_Result> response) {
//                if (!response.isSuccessful())
//                {
//                    Toast.makeText(getApplicationContext(),"Send khong thanh cong",Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Send thanh cong",Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<SendTokenFirebaseToServer_Result> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//        Log.d(TAG, s);
    }
}