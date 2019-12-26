package com.ygaps.travelapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Noti extends AppCompatActivity {

    public static final String ACCEPT_ACTION = "Accept";
    public static final String DECLINE_ACTION = "Decline";
    public static final String SHOW_ACTION = "Show";

    private Intent intent;
    private TextView tvMessage;
    private TextView tvResponseStatus;
    private Button btnAccept;
    private Button btnDecline;
    private LinearLayout layoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        
        mapping();
        hideNotification();
        addAction();
    }

    private void addAction() {
        String action=intent.getAction();
        if(action==null){
            return;
        }

        switch (action)
        {
            case ACCEPT_ACTION:
                acceptAction();
                break;
            case DECLINE_ACTION:
                declineAction();
                break;
            case SHOW_ACTION:
                showAction();
                break;
            default:
                finish();
                break;
        }
    }

    private void showAction() {
        layoutButton.setVisibility(LinearLayout.VISIBLE);
        tvResponseStatus.setText("Waiting response...");
        tvResponseStatus.setTextColor(Color.MAGENTA);
    }

    private void hideNotification() {
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra("NOTIFICATION_ID",-1));
    }

    private void mapping() {
        intent=getIntent();
        tvMessage=findViewById(R.id.tvMessageContent);
        tvResponseStatus=findViewById(R.id.tvResponseStatus);
        btnAccept=findViewById(R.id.btnAccept);
        btnDecline=findViewById(R.id.btnDecline);
        layoutButton=findViewById(R.id.layoutButton);
        layoutButton.setVisibility(LinearLayout.INVISIBLE);

        Bundle layThongBao=new Bundle();
        layThongBao=intent.getExtras();
        tvMessage.setText(layThongBao.getString("MESSAGE_BODY"));

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButton.setVisibility(LinearLayout.INVISIBLE);
                acceptAction();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButton.setVisibility(LinearLayout.INVISIBLE);
                declineAction();
            }
        });
    }



    private void declineAction() {
        tvResponseStatus.setText("Declined!");
        tvResponseStatus.setTextColor(Color.RED);
        Toast.makeText(this,"You have been declined this invitation", Toast.LENGTH_SHORT);
        // xu li tu choi o day
    }

    private void acceptAction() {
        tvResponseStatus.setText("Accepted!");
        tvResponseStatus.setTextColor(Color.GREEN);
        Toast.makeText(this,"You have been accepted this invitation", Toast.LENGTH_SHORT);
        // xu li dong y o day
    }
}
