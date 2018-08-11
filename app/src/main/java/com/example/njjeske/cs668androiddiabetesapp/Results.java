package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Results extends AppCompatActivity {
    BottomNavigationView navigation;
    DatabaseHelper db;
    LinearLayout contentView;
    Button graphs_btn, lists_btn, stats_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        db = new DatabaseHelper(this);
        contentView = (LinearLayout) findViewById(R.id.Results_content);
        graphs_btn = (Button) findViewById(R.id.Results_button_graph);
        lists_btn = (Button) findViewById(R.id.Results_button_list);
        stats_btn = (Button) findViewById(R.id.Results_button_stats);


        // TODO get info from Shared Preferences for Results
        //fillListView();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);

    }

    /**
     * Fills the ListView with items from DataAdapter
     */
    private void fillListView() {
        // Create listView programmatically since we change depending on content
        ListView lvItems = new ListView(this);
        lvItems.setPadding(20, 10, 20, 10);
        lvItems.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lvItems.setDividerHeight(20);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight=1;
        lvItems.setLayoutParams(params);
        contentView.addView(lvItems);
//        contentView.setVisibility(View.INVISIBLE);
        //create query string
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            String submitString = String.format("%s, %s, %s, %s, %s, %s, %s",
                    sp.getString("to_date", ""),
                    sp.getString("from_date", ""),
                    sp.getString("to_time", ""),
                    sp.getString("from_time", ""),
                    sp.getString("keywords", ""),
                    sp.getString("start_val", ""),
                    sp.getString("end_val", ""));
            System.out.println("RESULTS: submitString - "+submitString);
            String checkboxString = String.format("%b, %b, %b, %b",
                    sp.getBoolean("bgl_check", false),
                    sp.getBoolean("exercise_check", false),
                    sp.getBoolean("diet_check", false),
                    sp.getBoolean("medication_check", false));
            System.out.println("RESULTS: CHECKBOXES - "+checkboxString);
        } else {
            System.out.println("RESULTS: SharedPreferences Empty");
        }
        // Construct the data source
        // todo MAKE SURE TO CHECK IF EMPTY VALUES IN DATABASEHELPER
        //ArrayList<DB_Object> arrayOfActivities = db.getAllActivity();
        //DataAdapter dataAdapter = new DataAdapter(this, arrayOfActivities);

        // Attach adapter to the ListView
        //lvItems.setAdapter(dataAdapter);
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
                    startActivity(new Intent(Results.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(Results.this, Regimen.class));
                    break;
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        navigation.setSelectedItemId(R.id.navigation_history);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}