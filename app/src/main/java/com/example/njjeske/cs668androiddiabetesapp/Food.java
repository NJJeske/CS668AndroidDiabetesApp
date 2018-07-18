package com.example.njjeske.cs668androiddiabetesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Food extends AppCompatActivity {
    // need to rename to Meal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        // set heading to selected button from Diet activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("dietKey");
            TextView tv1 = findViewById(R.id.BGL_heading);
            tv1.setText(value);
            //The key argument here must match that used in the other activity
        }
    }
}