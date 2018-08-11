package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Results extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        // TODO get info from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            String value = extras.getString("dietKey");
//            TextView tv1 = findViewById(R.id.BGL_heading);
//            tv1.setText(value);
            //The key argument here must match that used in the other activity
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);

    }

    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(Results.this, Home.class));
                    break;
                case R.id.navigation_activity:
                    startActivity(new Intent(Results.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
//                    startActivity(new Intent(Results.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(Results.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}