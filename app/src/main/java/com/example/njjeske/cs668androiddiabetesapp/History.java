package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class History extends AppCompatActivity {
    private Button searchButton, graphs, lists, stats;
    private EditText fromDate, toDate, keywords;
    private RadioButton radio_bgl, radio_exercise, radio_diet, radio_medication;
    DatabaseHelper db;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(this);

        // connect buttons to XML & onClick listeners
        searchButton = (Button) findViewById(R.id.Data_search);
        searchButton.setOnClickListener(dataOnClickListener);

        // connect editText to XML
        fromDate = (EditText) findViewById(R.id.Data_editText_fromDate);
        toDate = (EditText) findViewById(R.id.Data_editText_toDate);
        keywords = (EditText) findViewById(R.id.Data_editText_keywords);

        // connect radio to XML
        radio_bgl = (RadioButton) findViewById(R.id.Data_Radio_bloodGlucose);
        radio_exercise = (RadioButton) findViewById(R.id.Data_Radio_exercise);
        radio_diet = (RadioButton) findViewById(R.id.Data_Radio_diet);
        radio_medication = (RadioButton) findViewById(R.id.Data_Radio_medication);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fromDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(History.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        toDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(History.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });
    }

    private View.OnClickListener dataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.Data_search) {
                search(v);
            } else {
                radioSelect(v);
            }
        }
    };

    private void radioSelect(View v) {
        Log.d("RadioButton", "RadioButton clicked!");
        // TODO change in private selection array
        // TODO unselect other radio buttons
    }

    // submit into database
    private void search(View v) {
        Log.d("HISTORY", "History clicked");
        String submitString;
        submitString = String.format("%s, %s, %s", fromDate.getText().toString(), toDate.getText().toString(), keywords.getText().toString());
        Log.d("HISTORY", "Submit string: " + submitString);

        // TODO add search in database in DatabaseHelper
//        DatabaseHelper.search(submitString);

        // start new Results activity from query
        Toast.makeText(this, "SEARCHING...", Toast.LENGTH_SHORT).show();

    }

    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(History.this, Home.class));
                    break;
                case R.id.navigation_activity:
                    startActivity(new Intent(History.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
//                    startActivity(new Intent(History.this, History.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(History.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}
