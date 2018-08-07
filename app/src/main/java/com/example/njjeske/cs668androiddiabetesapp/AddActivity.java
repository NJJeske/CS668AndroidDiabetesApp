package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    private TextView value;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1); //add activity index
        menuItem.setChecked(true);

        addListenerOnSpinnerItemSelection();

        value = (TextView) findViewById(R.id.AddActivity_Value_label);
        img = (ImageView) findViewById(R.id.AddActivity_image);

    }

    private void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(this);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //TODO: change appearance of view on select
//        String[] activityNames = {"Blood Glucose","Food","Exercise","Medicine"};
//        Toast.makeText(getApplicationContext(), activityNames[position], Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0: // blood glucose
                value.setText(R.string.value);
                img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                break;
            case 1: // food
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                break;
            case 2: // exercise
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                break;
            case 3: // medication
                value.setText(R.string.description);
                img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //TODO: switch bottom nav to home
                    startActivity(new Intent(AddActivity.this, Home.class));
                    break;
                case R.id.navigation_activity:
//                    startActivity(new Intent(AddActivity.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
                    //TODO: switch bottom nav to history
                    startActivity(new Intent(AddActivity.this, History.class));
                    break;
                case R.id.navigation_regimen:
                    //TODO: switch bottom nav to regimen
                    startActivity(new Intent(AddActivity.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}
