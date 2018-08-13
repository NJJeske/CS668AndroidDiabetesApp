package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Toast;
import android.database.Cursor;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private TextView header;
    private EditText description;
    private EditText date;
    private EditText time;
    private Button submit;
    private DatabaseHelper db;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int value;
    private DB_Object object;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        header = (TextView) findViewById(R.id.Activity_heading);
        description = (EditText) findViewById(R.id.Edit_editText_name);
        date = (EditText) findViewById(R.id.Edit_editText_amount);
        time = (EditText) findViewById(R.id.Edit_editText_time);
        submit = (Button) findViewById(R.id.Activity_Edit);
        db = new DatabaseHelper(this);


        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            if (selectedHour < 10) {
                                time.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else {
                                time.setText(selectedHour + ":0" + selectedMinute);
                            }
                        } else {
                            if (selectedHour < 10) {
                                time.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                time.setText(selectedHour + ":" + selectedMinute);
                            }
                        }

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(EditActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            value = extra.getInt("Data");
            //The key argument here must match that used in the other activity
            object = getDB_Object(value);


            header.setText(object.activityType);
            description.setText(object.description);
            date.setText(object.date);
            time.setText(object.time);
        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }

        });
    }

    private void submit(View v) {

        if (!description.getText().toString().isEmpty() &&
                !date.getText().toString().isEmpty() &&
                !time.getText().toString().isEmpty()) {


            db.updateActivity(value, object);
            Log.v("EDITACTIVITY", String.format(header.getText().toString() + ": %s was added.", ""));
            Toast.makeText(getApplicationContext(), String.format(header.getText().toString() + " %s was added.", ""),
                    Toast.LENGTH_SHORT).show();
            clearSharedPreferences();


            // check if data is actually in db (delete later)
            Cursor cursor = db.getAllData();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Log.v("EDITACTIVITY", "DB DATA: " + String.format("%s, %s, %s, %s", cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            } else {
                Log.v("EDITACTIVITY", "CURSOR EMPTY");
            }

        } else {
            Toast.makeText(getApplicationContext(), "Cannot submit, empty values in text fields.",
                    Toast.LENGTH_SHORT).show();
        }

        clear();
    }


    private void clear() {

        Intent intent = new Intent(EditActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

    }

    private DB_Object getDB_Object(int position) {
        ArrayList<DB_Object> arrayOfActivities = db.getAllActivity();
        DB_Object object = new DB_Object();

        for (int i = 0; i < arrayOfActivities.size(); i++) {
            if (position == i) {
                object = arrayOfActivities.get(i);
            }
        }

        return object;
    }

    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}