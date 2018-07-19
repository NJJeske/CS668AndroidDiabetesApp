package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Home extends AppCompatActivity implements View.OnClickListener {
    private CardView bglCard, foodCard, exerciseCard, medicineCard, dataCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // defining Cards
        bglCard = (CardView) findViewById(R.id.bgl_card);
        foodCard = (CardView) findViewById(R.id.food_card);
        exerciseCard = (CardView) findViewById(R.id.exercise_card);
        medicineCard = (CardView) findViewById(R.id.medicine_card);
        dataCard = (CardView) findViewById(R.id.data_card);

        // Add click listeners to the card
        bglCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        exerciseCard.setOnClickListener(this);
        medicineCard.setOnClickListener(this);
        dataCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.bgl_card:
                i = new Intent(this, BGL.class);
                startActivity(i);
                break;
            case R.id.food_card:
                i = new Intent(this, Meal.class);
                startActivity(i);
                break;
            case R.id.exercise_card:
                i = new Intent(this, Exercise.class);
                startActivity(i);
                break;
            case R.id.medicine_card:
                i = new Intent(this, Medicine.class);
                startActivity(i);
                break;
            case R.id.data_card:
                i = new Intent(this, History.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
