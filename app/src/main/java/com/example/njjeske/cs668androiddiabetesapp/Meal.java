package com.example.njjeske.cs668androiddiabetesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Meal extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
    }

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submit(v);
        }
    };

    // submit into database
    private void submit(View v) {
        String submitString;
//        submitString = String.format("%s, %s", time_editText.getText().toString(), value_editText.getText().toString());

        // TODO add to database method in DatabaseHelper
//        DatabaseHelper.add(BGL.class,submitString);

        Toast.makeText(getApplicationContext(), "Submitted your data!",
                Toast.LENGTH_SHORT).show();
        clear();
    }

    // TODO reset editText fields
    private void clear() {
    }
}