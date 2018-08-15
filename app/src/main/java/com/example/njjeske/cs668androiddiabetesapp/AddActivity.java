package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_type;
    private TextView value;
    private ImageView img;
    private EditText time_editText, date_editText, description_editText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;
    private Button submitbutton;
    private DatabaseHelper db;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_activity);

        db = new DatabaseHelper(this);

        spinner_type = (Spinner) findViewById(R.id.spinner);
        spinner_type.setOnItemSelectedListener(this);

        value = (TextView) findViewById(R.id.AddActivity_Value_label);
        img = (ImageView) findViewById(R.id.AddActivity_image);
        description_editText = (EditText) findViewById(R.id.AddActivity_editText_value);
        date_editText = (EditText) findViewById(R.id.AddActivity_editText_date);
        time_editText = (EditText) findViewById(R.id.AddActivity_editText_time);
        submitbutton = (Button) findViewById(R.id.AddActivity_Submit);
        submitbutton.setOnClickListener(submitOnClickListener);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        time_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            if (selectedHour < 10) {
                                time_editText.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else {
                                time_editText.setText(selectedHour + ":0" + selectedMinute);
                            }
                        } else {
                            if (selectedHour < 10) {
                                time_editText.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                time_editText.setText(selectedHour + ":" + selectedMinute);
                            }
                        }

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_editText.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(AddActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });


        if (getIntent().getStringExtra("SPINNER_SELECT") != null) {
            String spinner_select = getIntent().getStringExtra("SPINNER_SELECT");
            Log.v("ADDACTIVITY", "SPINNER_SELECT: " + spinner_select);
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
            Log.v("ADDACTIVITY", "SPINNER_SELECT is NULL");
        }

        showSharedPreferences();
    }

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO add to database method in DatabaseHelper
            //DatabaseHelper.add(BGL.class,submitString);
            if (!description_editText.getText().toString().isEmpty() &&
                    !date_editText.getText().toString().isEmpty() &&
                    !time_editText.getText().toString().isEmpty()) {
                String submitString;
                submitString = String.format("%s at %s, %s", description_editText.getText().toString(), date_editText.getText().toString(), time_editText.getText().toString());

                switch (spinner_type.getSelectedItemPosition()) {
                    case 0: // blood glucose
                        if (!(Double.parseDouble(description_editText.getText().toString()) < 40.0 ||
                                Double.parseDouble(description_editText.getText().toString()) > 600.0)) {

                            db.insertData("Blood Glucose", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                            Log.v("ADDACTIVITY", String.format("BGL: %s was added.", submitString));
                            Toast.makeText(getApplicationContext(), String.format("BGL: %s was added.", submitString),
                                    Toast.LENGTH_SHORT).show();
                            clearSharedPreferences();
                            clearText();
                        } else {
                            Toast.makeText(getApplicationContext(), "BGL value is outside the range (40-600).",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1: // food

                        db.insertData("Diet", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("ADDACTIVITY", String.format("FOOD: %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("FOOD: %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                    case 2: // exercise
                        db.insertData("Exercise", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("ADDACTIVITY", String.format("EXERCISE: %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("EXERCISE: %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                    case 3: // medication

                        db.insertData("Medication", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                        Log.v("ADDACTIVITY", String.format("MEDICINE: %s was added.", submitString));
                        Toast.makeText(getApplicationContext(), String.format("MEDICINE: %s was added.", submitString),
                                Toast.LENGTH_SHORT).show();
                        clearSharedPreferences();
                        clearText();
                        break;
                }

                // check if data is actually in db (delete later)
                Cursor cursor = db.getAllData();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Log.v("ADDACTIVITY", "DB DATA: " + String.format("%d %s, %s, %s, %s", cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                    } while (cursor.moveToNext());
                } else {
                    Log.v("ADDACTIVITY", "CURSOR EMPTY");
                }

            } else {
                Toast.makeText(getApplicationContext(), "Cannot submit, empty values in text fields.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void clearText() {
        time_editText.setText(null);
        date_editText.setText(null);
        description_editText.setText(null);
    }

    private View.OnClickListener dateTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.w("DATETIME", "CLICK ID: " + v.getId());
            if (v.getId() == R.id.AddActivity_editText_date) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date_editText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
            if (v.getId() == R.id.AddActivity_editText_time) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time_editText.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        }
    };

    /**
     * Performing Spinner action onItemSelected to change image & label description depending on activity type
     * @param arg0
     * @param arg1
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //TODO: change appearance of view on select
//        String[] activityNames = {"Blood Glucose","Food","Exercise","Medicine"};
//        Toast.makeText(getApplicationContext(), activityNames[position], Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0: // blood glucose
                description_editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                value.setText(R.string.value);
                img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                break;
            case 1: // food
                description_editText.setInputType(InputType.TYPE_CLASS_TEXT);
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                break;
            case 2: // exercise
                description_editText.setInputType(InputType.TYPE_CLASS_TEXT);
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                break;
            case 3: // medication
                description_editText.setInputType(InputType.TYPE_CLASS_TEXT);
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    /**
     * Bottom Navigation actions for onClick
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(AddActivity.this, Home.class));
                    break;
                case R.id.navigation_activity:
//                    startActivity(new Intent(AddActivity.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
                    startActivity(new Intent(AddActivity.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(AddActivity.this, Regimen.class));
                    break;
            }
            return true;
        }
    };

    /**
     * Save SharedPreferences as extra precaution
     */
    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("spinnerIndex", spinner_type.getSelectedItemPosition());
        editor.putString("date", date_editText.getText().toString());
        editor.putString("time", time_editText.getText().toString());
        editor.putString("description", description_editText.getText().toString());
        editor.commit();
    }

    /**
     * Show SharedPreferences: use inside onCreate()
     */
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            spinner_type.setSelection(sp.getInt("spinnerIndex", 0));
            date_editText.setText(sp.getString("date", ""));
            time_editText.setText(sp.getString("time", ""));
            description_editText.setText(sp.getString("description", ""));
        }
    }

    /**
     * Clear SharedPreferences upon submit
     */
    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        navigation.setSelectedItemId(R.id.navigation_home);
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
