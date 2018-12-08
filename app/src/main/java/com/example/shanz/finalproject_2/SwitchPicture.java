package com.example.shanz.finalproject_2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public class SwitchPicture extends Thread {
    private static final int sleepTime  = 5000;
    public SwitchPicture() {
    }
    public void run() {
        try {
            sleep(sleepTime);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
