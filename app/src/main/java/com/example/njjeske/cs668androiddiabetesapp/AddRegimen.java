package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddRegimen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_type;
    private TextView value;
    private ImageView img;
    private EditText time_editText, description_editText;
    private int mHour, mMinute;
    Calendar c;
    private Button submitbutton;
    private DatabaseHelper db;
    private String userInfo;
    private String correctMinute;
    private String correctHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_regimen);

        db = new DatabaseHelper(this);

        spinner_type = (Spinner) findViewById(R.id.spinner);
        spinner_type.setOnItemSelectedListener(this);

        value = (TextView) findViewById(R.id.AddRegimen_Value_label);
        img = (ImageView) findViewById(R.id.AddRegimen_image);
        description_editText = (EditText) findViewById(R.id.AddRegimen_editText_value);
        time_editText = (EditText) findViewById(R.id.AddRegimen_editText_time);
        submitbutton = (Button) findViewById(R.id.AddRegimen_Submit);
        submitbutton.setOnClickListener(submitOnClickListener);

        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        time_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddRegimen.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        correctMinute = selectedMinute + "";
                        correctHour = selectedHour + "";
                        if (selectedMinute < 10) {
                            correctMinute = "0" + selectedMinute;
                        }
                        if (selectedHour < 10) {
                            correctHour = "0" + selectedHour;
                        }
                        time_editText.setText(correctHour + ":" + correctMinute);

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        if (getIntent().getStringExtra("SPINNER_SELECT") != null) {
            String spinner_select = getIntent().getStringExtra("SPINNER_SELECT");
            Log.v("AddRegimen", "SPINNER_SELECT: " + spinner_select);
            switch (spinner_select) {
                case "BGL": // blood glucose
                    spinner_type.setSelection(0);
                    break;
                case "Food": // food
                    spinner_type.setSelection(1);
                    break;
                case "Exercise": // exercise
                    spinner_type.setSelection(2);
                    break;
                case "Medicine": // medication
                    spinner_type.setSelection(3);
                    break;
            }
            saveSharedPreferences();
        } else {
            Log.v("AddRegimen", "SPINNER_SELECT is NULL");
        }

        showSharedPreferences();
    }

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //DatabaseHelper.add(BGL.class,submitString);
            if (!description_editText.getText().toString().isEmpty() &&
                    !time_editText.getText().toString().isEmpty()) {
                String submitString;
                submitString = String.format("%s at %s", description_editText.getText().toString(), time_editText.getText().toString());

                switch (spinner_type.getSelectedItemPosition()) {
                    case 0: // blood glucose
                        db.insertRegimen("Blood Glucose", time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("AddRegimen", String.format("Regimen (BGL): %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("Regimen (BGL): %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                    case 1: // food

                        db.insertRegimen("Diet", time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("AddRegimen", String.format("Regimen (Food): %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("Regimen (Food): %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                    case 2: // exercise
                        db.insertRegimen("Exercise", time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("AddRegimen", String.format("Regimen (Exercise): %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("Regimen (Exercise): %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                    case 3: // medication

                        db.insertRegimen("Medication", time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("AddRegimen", String.format("Regimen (Medicine):%s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("Regimen (Medicine): %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                }

                // check if data is actually in db (delete later)
                ArrayList<DB_Object> cursor = db.getAllRegimen();
                for (int i = 0; i < cursor.size(); i++) {
                    Log.v("AddRegimen", "DB DATA: " + cursor.get(i).toString());
                }
                if (cursor.size() == 0) {
                    Log.v("AddRegimen", "CURSOR EMPTY");
                }

                userInfo = getIntent().getStringExtra("userInfo");
                Intent intentToPassVarToAlarmReceiver = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                intentToPassVarToAlarmReceiver.putExtra("userInfo", userInfo);
                sendBroadcast(intentToPassVarToAlarmReceiver);
                sendNotification();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("user", userInfo);
                edit.commit();

            } else {
                Toast.makeText(getApplicationContext(), "Cannot submit, empty values in text fields.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void clearText() {
        time_editText.setText(null);
        description_editText.setText(null);
    }

    /**
     * Performing Spinner action onItemSelected to change image & label description depending on activity type
     *
     * @param arg0
     * @param arg1
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        String[] activityNames = {"Blood Glucose","Food","Exercise","Medicine"};
//        Toast.makeText(getApplicationContext(), activityNames[position], Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0: // blood glucose
                img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                break;
            case 1: // food
                img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                break;
            case 2: // exercise
                img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                break;
            case 3: // medication
                img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /**
     * Save SharedPreferences as extra precaution
     */
    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("spinnerIndex", spinner_type.getSelectedItemPosition());
        editor.putString("time", time_editText.getText().toString());
        editor.putString("description", description_editText.getText().toString());
        editor.commit();
    }

    /**
     * Show SharedPreferences: use inside onCreate()
     */
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            spinner_type.setSelection(sp.getInt("spinnerIndex", 0));
            time_editText.setText(sp.getString("time", ""));
            description_editText.setText(sp.getString("description", ""));
        }
    }

    /**
     * Send Notification by Time Picked once added
     */

    public void sendNotification() {

        Calendar cal = Calendar.getInstance();

        //Set Time Here for Notification
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(correctHour));
        cal.set(Calendar.MINUTE, Integer.parseInt(correctMinute));
        cal.set(Calendar.SECOND, 0);

        Intent notifyIntent = new Intent(getApplicationContext(), NotificationReceiver.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 100, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);


    }

    /**
     * Clear SharedPreferences upon submit
     */
    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
