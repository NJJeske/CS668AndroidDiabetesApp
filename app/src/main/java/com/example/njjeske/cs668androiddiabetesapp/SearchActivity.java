package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    private Button searchButton, graphs, lists, stats;
    private EditText fromDate, toDate, keywords, startValue, endValue;
    private CheckBox check_bgl, check_exercise, check_diet, check_medication;
    DatabaseHelper db;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DatabaseHelper(this);

        // connect buttons to XML & onClick listeners
        searchButton = (Button) findViewById(R.id.Data_search);
        searchButton.setOnClickListener(searchOnClickListener);

        // connect to XML
        fromDate = (EditText) findViewById(R.id.Data_editText_fromDate);
        toDate = (EditText) findViewById(R.id.Data_editText_toDate);
        keywords = (EditText) findViewById(R.id.Data_editText_keywords);
        startValue = (EditText) findViewById(R.id.Data_editText_startval);
        endValue = (EditText) findViewById(R.id.Data_editText_endval);
        check_bgl = (CheckBox) findViewById(R.id.Data_Radio_bloodGlucose);
        check_exercise = (CheckBox) findViewById(R.id.Data_Radio_exercise);
        check_diet = (CheckBox) findViewById(R.id.Data_Radio_diet);
        check_medication = (CheckBox) findViewById(R.id.Data_Radio_medication);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);

        // set up Calendar for DatePickerDialog
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

                DatePickerDialog dpDialog = new DatePickerDialog(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
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

                DatePickerDialog dpDialog = new DatePickerDialog(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });

        fillListview();
        showSharedPreferences();

    }

    private void fillListview() {
        Cursor cursor = db.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            // Find ListView to populate
            ListView lvItems = (ListView) findViewById(R.id.Data_listView);
            lvItems.setPadding(20, 10, 20, 10);
            lvItems.setDivider(new ColorDrawable(Color.TRANSPARENT));
            lvItems.setDividerHeight(20);
            // Setup cursor adapter using cursor from last step
            DataAdapter dataAdapter = new DataAdapter(this, cursor);
            // Attach cursor adapter to the ListView
            lvItems.setAdapter(dataAdapter);
        } else {
            Log.v("SEARCH", "SEARCH: fillListView failed, cursor empty");
        }
    }

    // Search: load new Results activity with passed string
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("HISTORY", "SearchActivity clicked");
            String submitString;
            submitString = String.format("%s, %s, %s",
                    fromDate.getText().toString(),
                    toDate.getText().toString(),
                    keywords.getText().toString());
            Log.d("HISTORY", "Submit string: " + submitString);

            // TODO add search in database in DatabaseHelper
//        DatabaseHelper.search(submitString);

            // start new Results activity from query
            Toast.makeText(getApplicationContext(), "SEARCHING...", Toast.LENGTH_SHORT).show();

            clearSharedPreferences();
        }
    };

    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(SearchActivity.this, Home.class));
                    break;
                case R.id.navigation_activity:
                    startActivity(new Intent(SearchActivity.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
//                    startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(SearchActivity.this, Regimen.class));
                    break;
            }
            return true;
        }
    };

    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("to_date", fromDate.getText().toString());
        editor.putString("from_date", toDate.getText().toString());
        editor.putString("keywords", keywords.getText().toString());
        editor.putString("start_val", startValue.getText().toString());
        editor.putString("end_val", endValue.getText().toString());
        editor.putBoolean("bgl_check", check_bgl.isChecked());
        editor.putBoolean("exercise_check", check_exercise.isChecked());
        editor.putBoolean("diet_check", check_diet.isChecked());
        editor.putBoolean("medication_check", check_medication.isChecked());
        editor.commit();
    }

    //use this inside onCreate()
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            fromDate.setText(sp.getString("to_date", ""));
            toDate.setText(sp.getString("from_date", ""));
            keywords.setText(sp.getString("keywords", ""));
            startValue.setText(sp.getString("start_val", ""));
            endValue.setText(sp.getString("end_val", ""));
            check_bgl.setChecked(sp.getBoolean("bgl_check", false));
            check_exercise.setChecked(sp.getBoolean("exercise_check", false));
            check_diet.setChecked(sp.getBoolean("diet_check", false));
            check_medication.setChecked(sp.getBoolean("medication_check", false));
        }
    }

    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
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
