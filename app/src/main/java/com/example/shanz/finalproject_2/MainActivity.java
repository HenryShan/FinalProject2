package com.example.shanz.finalproject_2;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import android.os.Handler;

import java.io.File;
import java.util.Random;
import java.util.Set;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
import static com.example.shanz.finalproject_2.MainActivity.countTwo;
import static com.example.shanz.finalproject_2.MainActivity.mcontext;
import static com.example.shanz.finalproject_2.MainActivity.totalCount;
import static com.example.shanz.finalproject_2.SecondActivity.toReply;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }
    Button button;
    Button settings;
    public static int totalCount;
    public static int countTwo;
    static ImageButton start = null;
    private static int count;
    public static boolean doNotDisturb = false;
    protected static File myFile;
    private final static Random random = new Random();
    public static List<Integer> pictures = new ArrayList<>();
    protected static Context mcontext;
    public static String fileName = "data";
    public static FileOutputStream outputStream;
    public Animation animation;
    public static TelephonyManager callManager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getWindow().setBackgroundDrawableResource(pictures.get(random.nextInt(10)));
        final Handler handler = new Handler();
        Runnable changeImageRunnable = new Runnable() {
            public void run() {
                Integer tmp = pictures.get(random.nextInt(10));
                getWindow().setBackgroundDrawableResource(tmp);
                handler.postDelayed(this, 15000);
            }
        };
        handler.postDelayed(changeImageRunnable, 15000);
        setContentView(R.layout.activity_main);
        if (myFile == null) {
            myFile = new File(getApplicationContext().getFilesDir(), fileName);
        }
        mcontext = getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent ("com.litreily.UserInstruction"));
            }
        });

        settings = findViewById(R.id.button2);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("com.litreily.SecondActivity"));
            }
        });
        final NotificationListenerService nls = new NotificationMonitor();
        callManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        final PhoneCallListener pcl = new PhoneCallListener();
        final ImageButton start = findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.READ_CONTACTS}, 1);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
                if (!listeningPermission()) {
                    getNotificationListener();
                }
                if (!listeningPermission() || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Pleas open all permission needed!", Toast.LENGTH_SHORT).show();
                    return;
                }
                count++;
                if (count % 2 != 0) {
                    rebindNotificationListenerService();
                    start.setBackgroundResource(R.drawable.pause);
                    setToSilent();
                    doNotDisturb = true;
                    callManager.listen(pcl , PhoneStateListener.LISTEN_CALL_STATE);
                } else {
                    start.setBackgroundResource(R.drawable.button);
                    setToNormal();
                    doNotDisturb = false;
                    callManager.listen(pcl, PhoneStateListener.LISTEN_NONE);
                }
            }
        });

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
//    private void getDoNotDisturb(){
//        NotificationManager notificationManager =
//                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (!notificationManager.isNotificationPolicyAccessGranted()) {
//            Intent intent = new Intent(
//                    android.provider.Settings
//                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//            startActivity(intent);
//        }
//    }
    private void rebindNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationMonitor.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, NotificationMonitor.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }
//    private void rebindCallListener() {
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(new ComponentName(this, PhoneStateListener.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(new ComponentName(this, PhoneStateListener.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//    }
//    //this part..
//    private boolean checkDisturbPermission() {
//        NotificationManager notificationManager =
//                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
//                && notificationManager.isNotificationPolicyAccessGranted()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    private void getNotificationListener() {
        Intent intent = new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }
    private boolean listeningPermission() {
        Set<String> packageNames
                = NotificationManagerCompat.getEnabledListenerPackages(getApplicationContext());
        if (packageNames.contains(getApplicationContext().getPackageName())) {
            return true;
        }
        return false;
    }
}
 class PhoneCallListener extends PhoneStateListener {
    @Override
     public void onCallStateChanged(int state, String phoneNumber) {
        try {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    sendSms(phoneNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private void sendSms(String phoneNumber) {
         SmsManager smsMgr = SmsManager.getDefault();
         try {
             smsMgr.sendTextMessage(phoneNumber, null, toReply, null, null);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
