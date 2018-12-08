package com.example.shanz.finalproject_2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button button;
    private static int count;
    private static boolean disturbPermission;
    private static File file;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        List<Integer> pictures = new ArrayList<>();
        pictures.add(R.drawable.bg1);
        pictures.add(R.drawable.bg2);
        pictures.add(R.drawable.bg3);
        pictures.add(R.drawable.bg4);
        pictures.add(R.drawable.bg5);
        pictures.add(R.drawable.bg6);
        pictures.add(R.drawable.bg7);
        pictures.add(R.drawable.bg8);
        pictures.add(R.drawable.bg9);
        pictures.add(R.drawable.bg10);
//        if (this.file == null)
//            file = new File(this.getFilesDir(), "file");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.bg1);

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String times = String.valueOf(count);
                Toast.makeText(MainActivity.this, times, Toast.LENGTH_SHORT).show();
            }
        });
        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.litreily.SecondActivity"));
            }
        });
        final ImageButton start = (ImageButton) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkDisturbPermission()) {
                    getDoNotDisturb();
                    return;
                }
                count++;
                if (count % 2 != 0) {
                    start.setBackgroundResource(R.drawable.pause);
                    setToSilent();
                } else {
                    start.setBackgroundResource(R.drawable.button);
                    setToNormal();
                }
            }
        });


//        SwitchPicture[] qiehuan = new SwitchPicture[1];
//        for (int i = 0; i < qiehuan.length; i++) {
//            qiehuan[i] = new SwitchPicture();
//            qiehuan[i].start();
//            try {
//                qiehuan[i].join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        SwitchPicture qie = new SwitchPicture();
        qie.start();
        /*try {
            qie.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
            getWindow().setBackgroundDrawableResource(pictures.get(1));

    }
    private File contactsGet() {

        return null;
    }
    //this part to...
    private void setToSilent() {
        AudioManager mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(mode != null){
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mode.getStreamVolume(AudioManager.STREAM_RING);
        }
    }
    private void setToNormal() {
        AudioManager mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if(mode != null){
            mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mode.getStreamVolume(AudioManager.STREAM_RING);
        }
    }
    private void getDoNotDisturb(){
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }
    //this part. Need reference.
    private boolean checkDisturbPermission() {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && notificationManager.isNotificationPolicyAccessGranted()) {
            return true;
        } else {
            return false;
        }
    }
}
