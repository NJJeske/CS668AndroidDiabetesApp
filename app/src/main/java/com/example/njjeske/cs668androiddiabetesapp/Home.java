package com.example.njjeske.cs668androiddiabetesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener {
    private CardView bglCard, foodCard, exerciseCard, medicineCard;
    private TextView welcome, logout;
    private static final String HOME = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcome = (TextView) findViewById(R.id.welcome);
//        welcome.setText("Welcome "+name);
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                builder1.setTitle(R.string.logout);
                builder1.setMessage(R.string.dialog_message);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.dialog_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO: logout & switch to login screen
                                //logout
                                //startActivity(new Intent(this, Login.class));
                                Toast.makeText(getApplicationContext(), "Logged out!",
                                        Toast.LENGTH_SHORT).show(); // temp
                            }
                        });

                builder1.setNegativeButton(
                        R.string.dialog_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog logout_alert = builder1.create();
                logout_alert.show();
            }
        });
        // defining Cards
        bglCard = (CardView) findViewById(R.id.bgl_card);
        foodCard = (CardView) findViewById(R.id.food_card);
        exerciseCard = (CardView) findViewById(R.id.exercise_card);
        medicineCard = (CardView) findViewById(R.id.medicine_card);

        // Add click listeners to the card
        bglCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        exerciseCard.setOnClickListener(this);
        medicineCard.setOnClickListener(this);

        // Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // Card Navigation
    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, AddActivity.class);

        switch (v.getId()) {
            case R.id.bgl_card:
                i.putExtra("SPINNER_SELECT", "BGL");
                startActivity(i);
                break;
            case R.id.food_card:
                i.putExtra("SPINNER_SELECT", "Food");
                startActivity(i);
                break;
            case R.id.exercise_card:
                i.putExtra("SPINNER_SELECT", "Exercise");
                startActivity(i);
                break;
            case R.id.medicine_card:
                i.putExtra("SPINNER_SELECT", "Medicine");
                startActivity(i);
                break;
            default:
                break;
        }
    }

    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    startActivity(new Intent(Home.this, Home.class));
                    break;
                case R.id.navigation_activity:
                    startActivity(new Intent(Home.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
                    startActivity(new Intent(Home.this, History.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(Home.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}
