package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Data extends AppCompatActivity {
    private Button searchButton, graphs, lists, stats;
    private EditText fromDate, toDate, keywords;
    private RadioButton radio_bgl, radio_exercise, radio_diet, radio_medication;
    private int[] buttonSelected, radioSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // connect buttons to XML & onClick listeners
        searchButton = findViewById(R.id.Data_search);
        searchButton.setOnClickListener(dataOnClickListener);

        graphs = findViewById(R.id.Data_btn_graphs);
        lists = findViewById(R.id.Data_btn_lists);
        stats = findViewById(R.id.Data_btn_stats);
        graphs.setOnClickListener(dataOnClickListener);
        lists.setOnClickListener(dataOnClickListener);
        stats.setOnClickListener(dataOnClickListener);

        // connect editText to XML
        fromDate = findViewById(R.id.Data_editText_fromDate);
        toDate = findViewById(R.id.Data_editText_toDate);
        keywords = findViewById(R.id.Data_editText_keywords);

        // connect radio to XML
        radio_bgl = findViewById(R.id.Data_Radio_bloodGlucose);
        radio_exercise = findViewById(R.id.Data_Radio_exercise);
        radio_diet = findViewById(R.id.Data_Radio_diet);
        radio_medication = findViewById(R.id.Data_Radio_medication);

        // simple selected array
        buttonSelected = new int[3];
        radioSelected = new int[4];
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
        Log.d("Button", "Search clicked");
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
}
