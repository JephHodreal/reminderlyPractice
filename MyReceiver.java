package com.example.casestudyreminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyReceiver extends AppCompatActivity {

    Button pickTimebtn, save;
    TextView timeview;
    Dbhandler dbhandler;
    SQLiteDatabase remDB;
    EditText title, desc;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receiver);
        Dbhandler dbhandler = new Dbhandler(this);
        remDB = dbhandler.getWritableDatabase();
        title = findViewById(R.id.etRemTitle);
        desc = findViewById(R.id.etRemDesc);
        pickTimebtn = findViewById(R.id. pickTimebtn);
        timeview = findViewById(R.id.editTextTime);
        save = findViewById(R.id.btnSave);

        pickTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MyReceiver  .this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeview.setText(hourOfDay + ":" + minute);
                    }
                } , hour, minute, false);
                timePickerDialog.show();
            }
        });
        saveReminder();
    }
    void saveReminder(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().length() == 0){
                    toast = Toast.makeText(getApplicationContext(), "Provide reminder name!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(timeview.getText().toString().trim().length() == 0){
                    toast = Toast.makeText(getApplicationContext(), "Provide reminder time!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ContentValues val = new ContentValues();
                    val.put("name", title.getText().toString());
                    val.put("description", desc.getText().toString());
                    val.put("time", timeview.getText().toString());
                    //finally inserting data from edit text box to sqlite
                    remDB.insert("reminders", null, val);

                    toast = Toast.makeText(getApplicationContext(), "Reminder Set!", Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigning alarm manager object to set alarm
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("title", title.getText().toString());                   //sending data to alarm class to create channel and notification
        intent.putExtra("desc", desc.getText().toString());                     //sending data to alarm class to create channel and notification
        intent.putExtra("time", timeview.getText().toString());                 //sending data to alarm class to create channel and notification

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String oras = time ;
        String timeFormat = "HH:mm";
        DateFormat formatter = new SimpleDateFormat(timeFormat);
        try {
            Date date1 = formatter.parse(oras);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);                //this intent will be called once the setting alarm is complete
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity to mainactivity
    }
}
