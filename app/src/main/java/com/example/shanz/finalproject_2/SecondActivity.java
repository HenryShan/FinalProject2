package com.example.shanz.finalproject_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    public static String toReply = "This is a auto response from my robot. I'm busy right now and cannot reply you. I will rely you immediately as soon as I'm free.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
