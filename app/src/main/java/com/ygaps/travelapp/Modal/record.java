package com.ygaps.travelapp.Modal;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class record {
    private long mLastClickTime = 0;
    private Button play, record;
    public int check =1;
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    public record(Button play, Button record, MediaRecorder myAudioRecorder, String outputFile) {
        this.play = play;
        this.record = record;
        this.myAudioRecorder = myAudioRecorder;
        this.outputFile = outputFile;
        config();
    }

    public void config(){
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }
    public void setRecord(final Context context){
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    Toast.makeText(context, "Nhấp quá nhanh", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(check==1){
                        config();
                        startRecord();
                        Toast.makeText(context, "Đang ghi âm", Toast.LENGTH_SHORT).show();
                    }else if(check==0){
                        stopRecord();
                        Toast.makeText(context, "Ngừng ghi âm", Toast.LENGTH_SHORT).show();
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime();

            }
        });
    }
    public void startRecord(){
        try{
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }
        catch (IllegalStateException | IOException e){
            e.printStackTrace();
        }
        check=0;
    }
    public void stopRecord(){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        check=1;
    }
    public void playRE(final Context context) {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    // make something
                }
                Toast.makeText(context, outputFile, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getDuration(Context context){
        Uri uri = Uri.parse(outputFile);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
    }
}
