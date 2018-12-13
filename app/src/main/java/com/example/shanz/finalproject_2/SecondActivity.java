package com.example.shanz.finalproject_2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import static com.example.shanz.finalproject_2.MainActivity.fileName;
import static com.example.shanz.finalproject_2.MainActivity.myFile;
import static com.example.shanz.finalproject_2.MainActivity.outputStream;

public class SecondActivity extends AppCompatActivity {
    public static String origin = "This is a auto response from my robot. I'm busy right now and cannot reply you. I will rely you as soon as I'm free.";
    public static String toReply = "This is a auto response from my robot. I'm busy right now and cannot reply you. I will rely you as soon as I'm free.";
    Button button;
    Button test;
    Button refresh;
//    public boolean ifEmpty = true;
    private static String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getWindow().setBackgroundDrawableResource(R.drawable.bgactivity2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        try {
            String content = read(fileName);
            if (content != null) {
                toReply = content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText editText =(EditText)findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                content = editText.getText().toString();
                if (content.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your text!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content.length() > 150) {
                    Toast.makeText(getApplicationContext(), "Input Invalid, Exceed length limitation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "Saved successfully!", Toast.LENGTH_SHORT).show();
                writeIntoFile(content);

            }
        });
        test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), toReply,Toast.LENGTH_SHORT).show();
            }
        });
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toReply = origin;
                Toast.makeText(getApplicationContext(), "text is set to default",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void writeIntoFile(String input) {
        if (myFile == null) {
            Toast.makeText(getApplicationContext(), "Storage error", Toast.LENGTH_SHORT).show();
        }
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(input.getBytes());
            outputStream.close();
            try {
                String content = read(fileName);
                if (content != null && content.length() <= 150) {
                    toReply = content;
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid text!",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String read(String filename) throws Exception {
        FileInputStream inputStream = getApplicationContext().openFileInput(filename);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        byte[] data = outputStream.toByteArray();
//        for ()
        return new String(data);
    }
}
