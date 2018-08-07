package com.example.njjeske.cs668androiddiabetesapp;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class History extends AppCompatActivity {
    private Button searchButton, graphs, lists, stats;
    private EditText fromDate, toDate, keywords;
    private RadioButton radio_bgl, radio_exercise, radio_diet, radio_medication;
    private int[] buttonSelected, radioSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // connect buttons to XML & onClick listeners
        searchButton = (Button) findViewById(R.id.Data_search);
        searchButton.setOnClickListener(dataOnClickListener);

        graphs = (Button) findViewById(R.id.Data_btn_graphs);
        lists = (Button) findViewById(R.id.Data_btn_lists);
        stats = (Button) findViewById(R.id.Data_btn_stats);
        graphs.setOnClickListener(dataOnClickListener);
        lists.setOnClickListener(dataOnClickListener);
        stats.setOnClickListener(dataOnClickListener);

        // connect editText to XML
        fromDate = (EditText) findViewById(R.id.Data_editText_fromDate);
        toDate = (EditText) findViewById(R.id.Data_editText_toDate);
        keywords = (EditText) findViewById(R.id.Data_editText_keywords);

        // connect radio to XML
        radio_bgl = (RadioButton) findViewById(R.id.Data_Radio_bloodGlucose);
        radio_exercise = (RadioButton) findViewById(R.id.Data_Radio_exercise);
        radio_diet = (RadioButton) findViewById(R.id.Data_Radio_diet);
        radio_medication = (RadioButton) findViewById(R.id.Data_Radio_medication);

        // simple selected array
        buttonSelected = new int[3];
        radioSelected = new int[4];

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);
    }

    private View.OnClickListener dataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.Data_search) {
                search(v);
            } else if (v.getId() == R.id.Data_btn_graphs || v.getId() == R.id.Data_btn_lists || v.getId() == R.id.Data_btn_stats) {
                buttonSelect(v);
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

    private void buttonSelect(View v) {
        Log.d("Button", "Button clicked!");
        // TODO change in private selection array
        // TODO change color of button & return rest to normal
    }

    // submit into database
    private void search(View v) {
        Log.d("Button", "History clicked");
        // checkData to make sure valid info passed
        if (checkData()) {
            String submitString;
            submitString = String.format("%s, %s, %s", fromDate.getText().toString(), toDate.getText().toString(), keywords.getText().toString());

            // TODO add search in database in DatabaseHelper
//        DatabaseHelper.search(submitString);

            // start new Results activity from query
            Intent i;
            i = new Intent(this, Results.class);
            startActivity(i);
        }
    }

    // check dates in proper MM/DD/YYYY format
    private boolean checkData() {
        if (!fromDate.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            Toast.makeText(getApplicationContext(), "Incorrect date format for FROM DATE", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!toDate.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            Toast.makeText(getApplicationContext(), "Incorrect date format for TO DATE", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
