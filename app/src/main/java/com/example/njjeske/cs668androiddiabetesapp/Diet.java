package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Diet extends AppCompatActivity implements View.OnClickListener {
    private Button breakfast, lunch, dinner, water, snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        // Define buttons
        breakfast = findViewById(R.id.Diet_Breakfast);
        lunch = findViewById(R.id.Diet_Lunch);
        dinner = findViewById(R.id.Diet_Dinner);
        water = findViewById(R.id.Diet_Water);
        snack = findViewById(R.id.Diet_Snack);

        // Add click listeners
        breakfast.setOnClickListener(this);
        lunch.setOnClickListener(this);
        dinner.setOnClickListener(this);
        water.setOnClickListener(this);
        snack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, Meal.class);

        // Send button name to Meal activity
        Button b = (Button) v;
        String name = b.getText().toString();
        i.putExtra("dietKey", name);
        startActivity(i);
    }
}
