package com.example.casestudyreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationMessages extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_messages);
        textView = findViewById(R.id.message);
        Bundle bundle = getIntent().getExtras();        //call the data which is passed by another intent
        textView.setText(bundle.getString("message"));
    }
}