package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {
    private Button searchButton, graphs, lists, stats;
    private EditText fromDate, toDate, fromTime, toTime, keywords, startValue, endValue;
    private CheckBox check_bgl, check_exercise, check_diet, check_medication;
    private int mYear, mMonth, mDay;
    Calendar c;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // connect buttons to XML & onClick listeners
        searchButton = (Button) findViewById(R.id.Data_search);
        searchButton.setOnClickListener(searchOnClickListener);

        // connect to XML
        fromDate = (EditText) findViewById(R.id.Data_editText_fromDate);
        toDate = (EditText) findViewById(R.id.Data_editText_toDate);
        fromTime = (EditText) findViewById(R.id.Data_editText_fromTime);
        toTime = (EditText) findViewById(R.id.Data_editText_toTime);
        keywords = (EditText) findViewById(R.id.Data_editText_keywords);
        startValue = (EditText) findViewById(R.id.Data_editText_startval);
        endValue = (EditText) findViewById(R.id.Data_editText_endval);
        check_bgl = (CheckBox) findViewById(R.id.Data_Radio_bloodGlucose);
        check_exercise = (CheckBox) findViewById(R.id.Data_Radio_exercise);
        check_diet = (CheckBox) findViewById(R.id.Data_Radio_diet);
        check_medication = (CheckBox) findViewById(R.id.Data_Radio_medication);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_history);

        // set BGL checkbox listener
        check_bgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    startValue.setEnabled(true);
                    endValue.setEnabled(true);
                } else {
                    startValue.setEnabled(false);
                    endValue.setEnabled(false);
                    startValue.getText().clear();
                    endValue.getText().clear();
                }
            }

        });

        // set up Calendar for DatePickerDialog
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            if (selectedHour < 10) {
                                fromTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else {
                                fromTime.setText(selectedHour + ":0" + selectedMinute);
                            }
                        } else {
                            if (selectedHour < 10) {
                                fromTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                fromTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedMinute < 10) {
                            if (selectedHour < 10) {
                                toTime.setText("0" + selectedHour + ":0" + selectedMinute);
                            } else {
                                toTime.setText(selectedHour + ":0" + selectedMinute);
                            }
                        } else {
                            if (selectedHour < 10) {
                                toTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else {
                                toTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

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
        showSharedPreferences();

    }

    /**
     * Search button onClickListener: load new Results activity with passed string
     */
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("HISTORY", "SearchActivity clicked");
            SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
            String submitString = String.format("%s, %s, %s, %s, %s, %s, %s",
                    sp.getString("to_date", ""),
                    sp.getString("from_date", ""),
                    sp.getString("to_time", ""),
                    sp.getString("from_time", ""),
                    sp.getString("keywords", ""),
                    sp.getString("start_val", ""),
                    sp.getString("end_val", ""));
            String checkboxString = String.format("%b, %b, %b, %b",
                    sp.getBoolean("bgl_check", false),
                    sp.getBoolean("exercise_check", false),
                    sp.getBoolean("diet_check", false),
                    sp.getBoolean("medication_check", false));
            Log.d("HISTORY", "Submit string: " + submitString+ " "+checkboxString);

            // start new Results activity from query
            Toast.makeText(getApplicationContext(), "SEARCHING...", Toast.LENGTH_SHORT).show();
            //Add search queries into SharedPreferences
            Intent i = new Intent(SearchActivity.this, Results.class);
            saveSharedPreferences();
            startActivity(i);
        }
    };

    /**
     * Bottom Navigation actions for onClick
     */
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
                    startActivity(new Intent(SearchActivity.this, RegimenActivity.class));
                    break;
            }
            return true;
        }
    };

    /**
     * Save SharedPreferences as extra precaution
     */
    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("from_date", fromDate.getText().toString());
        editor.putString("to_date", toDate.getText().toString());
        editor.putString("to_time",toTime.getText().toString());
        editor.putString("from_time",fromTime.getText().toString());
        editor.putString("keywords", keywords.getText().toString());
        editor.putString("start_val", startValue.getText().toString());
        editor.putString("end_val", endValue.getText().toString());
        editor.putBoolean("bgl_check", check_bgl.isChecked());
        editor.putBoolean("exercise_check", check_exercise.isChecked());
        editor.putBoolean("diet_check", check_diet.isChecked());
        editor.putBoolean("medication_check", check_medication.isChecked());
        editor.commit();
    }

    /**
     * Show SharedPreferences: use inside onCreate()
     */
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            fromDate.setText(sp.getString("from_date", ""));
            toDate.setText(sp.getString("to_date", ""));
            fromTime.setText(sp.getString("from_time",""));
            toTime.setText(sp.getString("to_time",""));
            keywords.setText(sp.getString("keywords", ""));
            startValue.setText(sp.getString("start_val", ""));
            endValue.setText(sp.getString("end_val", ""));
            check_bgl.setChecked(sp.getBoolean("bgl_check", true));
            check_exercise.setChecked(sp.getBoolean("exercise_check", true));
            check_diet.setChecked(sp.getBoolean("diet_check", true));
            check_medication.setChecked(sp.getBoolean("medication_check", true));
        } else {
            check_bgl.setChecked(true);
            check_exercise.setChecked(true);
            check_diet.setChecked(true);
            check_medication.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    /**
     * Clear SharedPreferences upon submit
     */
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
        clearSharedPreferences();
    }
}
