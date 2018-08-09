package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Regimen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regimen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3); //add activity index
        menuItem.setChecked(true);
    }

    // Bottom Navigation actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(Regimen.this, Home.class));
                    break;
                case R.id.navigation_activity:
                    startActivity(new Intent(Regimen.this, AddActivity.class));
                    break;
                case R.id.navigation_history:
                    startActivity(new Intent(Regimen.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
//                    startActivity(new Intent(Regimen.this, Regimen.class));
                    break;
            }
            return true;
        }
    };
}
