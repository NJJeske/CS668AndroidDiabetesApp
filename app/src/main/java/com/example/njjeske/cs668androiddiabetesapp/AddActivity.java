package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.Calendar;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    private TextView value;
    private ImageView img;
    private EditText time_editText, date_editText, description_editText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;
    private Button submitbutton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1); //add activity index
        menuItem.setChecked(true);

        db = new DatabaseHelper(this);

        addListenerOnSpinnerItemSelection();
        if (getIntent().getStringExtra("SPINNER_SELECT") != null) {
            String spinner_select = getIntent().getStringExtra("SPINNER_SELECT");
            switch (spinner_select) {
                case "BGL": // blood glucose
                    spinner1.setSelection(0);
                    break;
                case "Food": // food
                    spinner1.setSelection(1);
                    break;
                case "Exercise": // exercise
                    spinner1.setSelection(2);
                    break;
                case "Medicine": // medication
                    spinner1.setSelection(3);
                    break;
            }
        }

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
                        time_editText.setText(selectedHour + ":" + selectedMinute);
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
                submitString = String.format("%s, %s, %s", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                Log.v("ADDACTIVITY", "Submit string: " + submitString);

                switch (spinner1.getSelectedItemPosition()) {
                    case 0: // blood glucose
                        if (!(Double.parseDouble(description_editText.getText().toString()) < 40.0 ||
                                Double.parseDouble(description_editText.getText().toString()) > 600.0)) {

                            Log.v("ADDACTIVITY", "TRYING TO INSERT ACTIVITY INTO DATABASE!!!!");
                            db.insertData("Blood Glucose", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());

                            Toast.makeText(getApplicationContext(), "BGL was updated.",
                                    Toast.LENGTH_SHORT).show();
                            clearText();
                        } else {

                            Toast.makeText(getApplicationContext(), "BGL value is outside the range (40-600).",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1: // food

                        db.insertData("Diet", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());

                        Toast.makeText(getApplicationContext(), "Food was updated.",
                                Toast.LENGTH_SHORT).show();
                        clearText();
                        break;
                    case 2: // exercise

                        db.insertData("Exercise", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());
                        Toast.makeText(getApplicationContext(), "Exercise was updated.",
                                Toast.LENGTH_SHORT).show();
                        clearText();
                        break;
                    case 3: // medication

                        db.insertData("Medication", date_editText.getText().toString(), time_editText.getText().toString(), description_editText.getText().toString());

                        Toast.makeText(getApplicationContext(), "Medication was updated.",
                                Toast.LENGTH_SHORT).show();
                        clearText();
                        break;
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

    private void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(this);
    }

    //Performing action onItemSelected and onNothing selected
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


    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //TODO: switch bottom nav to home
                    startActivity(new Intent(AddActivity.this, Home.class));
                    break;
                case R.id.navigation_activity:
//                    startActivity(new Intent(AddActivity.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
                    //TODO: switch bottom nav to history
                    startActivity(new Intent(AddActivity.this, History.class));
                    break;
                case R.id.navigation_regimen:
                    //TODO: switch bottom nav to regimen
                    startActivity(new Intent(AddActivity.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}
