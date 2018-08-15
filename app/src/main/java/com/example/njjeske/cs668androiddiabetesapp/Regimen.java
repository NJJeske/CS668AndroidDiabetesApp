package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Regimen extends AppCompatActivity {
    BottomNavigationView navigation;
    ListView lvItems;
    private ArrayList<DB_Object> arrayOfRegimen;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regimen);

        db = new DatabaseHelper(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_regimen);

        // setup listview
        fillListView();
    }

    /**
     * Fills the ListView with items from DataAdapter
     */
    private void fillListView() {
        Log.v("HOME", "fillListView()");
        // Find ListView to populate
        lvItems = (ListView) findViewById(R.id.Regimen_listView);
        lvItems.setPadding(20, 20, 20, 20);
        lvItems.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lvItems.setDividerHeight(20);

        // Construct the data source
        arrayOfRegimen = db.getAllRegimen();

        DataAdapter dataAdapter = new DataAdapter(this, arrayOfRegimen);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(dataAdapter);

        /**
         *  Used to get the selected activity for edit
         */
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(Regimen.this, EditRegimen.class);
                DB_Object obj = (DB_Object) lvItems.getItemAtPosition(position);
                System.out.println("Item: " + obj.toString());
                i.putExtra("Data", obj.getId());
                startActivity(i);
            }
        });
    }

    // This method will add regimen_nav.xml to this activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.regimen_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        startActivity(new Intent(Regimen.this, AddRegimen.class));
        return true;
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

    @Override
    public void onResume() {
        super.onResume();
        fillListView(); // if you come back from EditActivity
    }

    @Override
    public void onBackPressed() {
        navigation.setSelectedItemId(R.id.navigation_home);
    }
}
