package com.ygaps.travelapp;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ygaps.travelapp.Modal.record;

public class FragmentNofi extends Fragment {
    private Button start,play,stop;
    private MediaRecorder myAudioRecorder;
    public String outputFile;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private record record;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nofi,container,false);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        start = (Button)rootView.findViewById(R.id.record);
        stop = (Button) rootView.findViewById(R.id.stop);
        play = (Button)rootView.findViewById(R.id.play);
        record=new record(play,start,myAudioRecorder,outputFile);
        record.setRecord(getContext());
        record.playRE(getContext());
        return rootView;

    }
    //yêu cầu quyền ghi tập tin và quyền ghi âm
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();
        if (!permissionToWriteAccepted ) getActivity().finish();
    }
    //s
}
