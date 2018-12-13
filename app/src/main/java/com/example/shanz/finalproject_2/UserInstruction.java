package com.example.shanz.finalproject_2;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
//import android.widget.Button;

public class UserInstruction extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_instruction);
        getWindow().setBackgroundDrawableResource(R.drawable.instruction_bg);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
