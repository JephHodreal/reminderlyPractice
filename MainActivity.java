package com.example.casestudyreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton createReminder;
    String dbname = "ReminderDB";
    ScrollView scrollView;
    LinearLayout lv;
    SQLiteDatabase db;
    Cursor cursor;
    int id, hour, min;
    String title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createReminder = findViewById(R.id.create_reminder);
        scrollView = findViewById(R.id.sc);
        lv = findViewById(R.id.lv);
        addOnclick();

        db = openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("Select * from reminders ORDER BY id DESC", null);

        if(cursor.getCount() != 0){
            doQuery();
        }
    }
    public void addOnclick(){
        createReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
                startActivity(intent);
            }
        });
    }
    void doQuery() {
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
            title = cursor.getString(1);
            desc = cursor.getString(2);
            String[] timeval =cursor.getString(3).split(":");
            int[] time =new int[timeval.length];

            for (int a = 0; a < timeval.length; a++){
                time[a] = Integer.parseInt(timeval[a]);
            }
            min = time[1];
            TextView tv = new TextView(this );
            if(time[0] >= 12){
                if (time[0] > 12){
                    hour = time[0] - 12;
                }else{
                    hour = time[0];
                }
                tv.setText(title + "\t" + "-" + hour + ":" + String.format("%02d", min) + "PM"+"\n" +desc);
            } else {
                hour = time[0];
                tv.setText(title + "\t" + "-" + hour + ":" + String.format("%02d", min) + "AM"+"\n" +desc);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 20, 10 , 20);
            tv.setId((int) id);
            tv.setPadding(30, 20, 30, 20);
            tv.setTextSize(26);
            tv.setLayoutParams(params);
            tv.setBackgroundColor(Color.parseColor("#E4D1FF")); //DFDFDF // DAC1FF
            //adding the texview to the scroll view
            lv.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditReminder.class);
                    intent.putExtra("id",v.getId());
                    startActivity(intent);
                }
            });


        }
    }
}