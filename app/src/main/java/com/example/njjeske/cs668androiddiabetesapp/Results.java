package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.njjeske.cs668androiddiabetesapp.Fragments.GraphFragment;
import com.example.njjeske.cs668androiddiabetesapp.Fragments.StatsFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class Results extends AppCompatActivity {
    BottomNavigationView navigation;
    DatabaseHelper db;
    FrameLayout contentView;
    Button graphs_btn, lists_btn, stats_btn;
    SharedPreferences sp;
    ListView listView;
    Fragment fragment;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        db = new DatabaseHelper(this);
        contentView = (FrameLayout) findViewById(R.id.Results_content);
        graphs_btn = (Button) findViewById(R.id.Results_button_graph);
        lists_btn = (Button) findViewById(R.id.Results_button_list);
        stats_btn = (Button) findViewById(R.id.Results_button_stats);
        graphs_btn.setOnClickListener(viewTypesOnClickListener);
        lists_btn.setOnClickListener(viewTypesOnClickListener);
        stats_btn.setOnClickListener(viewTypesOnClickListener);
        sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);


        // TODO get info from Shared Preferences for Results
        fillListView();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2); //index of history
        menuItem.setChecked(true);

        manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.Results_content);

    }

    /**
     * Fills the ListView with items from DataAdapter
     */
    private void fillListView() {
        // Create listView programmatically since we change depending on content
        listView = new ListView(this);
        listView.setPadding(20, 10, 20, 10);
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerHeight(20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        contentView.addView(listView);

        //create query string
        if (!sp.equals(null)) {
            String submitString = String.format("%s, %s, %s, %s, %s, %s, %s",
                    sp.getString("to_date", ""),
                    sp.getString("from_date", ""),
                    sp.getString("to_time", ""),
                    sp.getString("from_time", ""),
                    sp.getString("keywords", ""),
                    sp.getString("start_val", ""),
                    sp.getString("end_val", ""));
            System.out.println("RESULTS: submitString - " + submitString);
            String checkboxString = String.format("%b, %b, %b, %b",
                    sp.getBoolean("bgl_check", false),
                    sp.getBoolean("exercise_check", false),
                    sp.getBoolean("diet_check", false),
                    sp.getBoolean("medication_check", false));
            System.out.println("RESULTS: CHECKBOXES - " + checkboxString);
        } else {
            System.out.println("RESULTS: SharedPreferences Empty");
        }
        // Construct the data source
        // todo MAKE SURE TO CHECK IF EMPTY VALUES IN HELPER METHODS
        ArrayList<DB_Object> arrayOfActivities;
        // search by keyword first
        arrayOfActivities = db.getByKeyword(sp.getString("keywords", ""));

        // filter all results
        if (sp.getBoolean("bgl_check", false)) {
            Log.v("RESULTS", "FILTERING... w/BGL");
            arrayOfActivities = filterByType(filterByDate(filterByTime(filterByBglValue(arrayOfActivities))));
        } else {
            Log.v("RESULTS", "FILTERING...");
            arrayOfActivities = filterByType(filterByDate(filterByTime(arrayOfActivities)));
        }
        DataAdapter dataAdapter = new DataAdapter(this, arrayOfActivities);

        // Attach adapter to the ListView
        listView.setAdapter(dataAdapter);

        // Used to get the selected activity for edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(Results.this, EditActivity.class);
                i.putExtra("Data", position);
                startActivity(i);

            }
        });
    }

    /**
     * Top buttons onClickListener: switch between data results
     */
    private View.OnClickListener viewTypesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Results_button_graph:
                    listView.setVisibility(View.INVISIBLE);
                    fillGraphsView();
                    Toast.makeText(getApplicationContext(), "RESULTS: SHOWING GRAPHS", Toast.LENGTH_SHORT).show();
                    Log.d("RESULTS", "Graphs Button clicked");
                    break;
                case R.id.Results_button_list:
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.hide(fragment);
                    transaction.commit();
                    listView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "RESULTS: SHOWING LISTS", Toast.LENGTH_SHORT).show();
                    Log.d("RESULTS", "Lists Button clicked");
                    break;
                case R.id.Results_button_stats:
                    listView.setVisibility(View.INVISIBLE);
                    fillStatsView();
                    Toast.makeText(getApplicationContext(), "RESULTS: SHOWING STATS", Toast.LENGTH_SHORT).show();
                    Log.d("RESULTS", "Stats Button clicked");
                    break;
                default:
                    Log.d("RESULTS", "Button clicked");
                    break;
            }
        }
    };

    private void fillStatsView() {
        // TODO assignment 4
        if (fragment == null) {
            fragment = new StatsFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.Results_content, fragment);
            transaction.commit();
        } else {
            FragmentTransaction ft = manager.beginTransaction();
            fragment = new StatsFragment();
            ft.replace(R.id.Results_content, fragment).commit();
        }
    }

    private void fillGraphsView() {
        // TODO assignment 4
        if (fragment == null) {
            fragment = new GraphFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.Results_content, fragment);
            transaction.commit();
        } else {
            FragmentTransaction ft = manager.beginTransaction();
            fragment = new GraphFragment();
            ft.replace(R.id.Results_content, fragment).commit();
        }
    }

    /**
     * SEARCH HELPER: Filters by activity type
     *
     * @param objects
     * @return filtered list by type
     */
    private ArrayList<DB_Object> filterByType(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY TYPE");
        Log.v("RESULTS", "TYPES - Starting List Size: " + objects.size());
        //new array list that will hold the filtered data
        ArrayList<DB_Object> filteredDataByType = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        if (sp != null) {
            for (int i = 0; i < objects.size(); i++) {
                String type = objects.get(i).getActivityType();
                Log.v("RESULTS", "Result item: " + type);
                if (type.equals("Blood Glucose") && sp.getBoolean("bgl_check", false)) {
                    Log.v("RESULTS", type + " added to results list");
                    filteredDataByType.add(objects.get(i));
                } else if (type.equals("Exercise") && sp.getBoolean("exercise_check", false)) {
                    Log.v("RESULTS", type + " added to results list");
                    filteredDataByType.add(objects.get(i));
                } else if (type.equals("Diet") && sp.getBoolean("diet_check", false)) {
                    Log.v("RESULTS", type + " added to results list");
                    filteredDataByType.add(objects.get(i));
                } else if (type.equals("Medication") && sp.getBoolean("medication_check", false)) {
                    Log.v("RESULTS", type + " added to results list");
                    filteredDataByType.add(objects.get(i));
                }
            }
        } else {
            Log.v("RESULTS", "SharedPreferences NULL");
        }
        Log.v("RESULTS", "TYPES - Ending List Size: " + filteredDataByType.size());
        return filteredDataByType;
    }

    /**
     * SEARCH HELPER: Filters by date
     *
     * @param objects
     * @return filtered list by dates
     */
    private ArrayList<DB_Object> filterByDate(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY DATE");
        Log.v("RESULTS", "DATE - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filterdData = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterDate(sp.getString("from_date", ""), objects.get(i).getDate()) &&
                    isBeforeDate(sp.getString("to_date", ""), objects.get(i).getDate()))
                filterdData.add(objects.get(i));

        }
        return filterdData;
    }

    /**
     * SEARCH HELPER: Filters list by time
     *
     * @param objects
     * @return filtered list by time
     */
    private ArrayList<DB_Object> filterByTime(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY TIME");
        Log.v("RESULTS", "TIME - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredTime = new ArrayList<>();
        String fromTime = sp.getString("from_time", "");
        String toTime = sp.getString("to_time", "");
        if (fromTime.equals("")) {
            fromTime = "00:00";
        }
        if (toTime.equals("")) {
            toTime = "24:00";
        }

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterTime(fromTime, objects.get(i).getTime()) &&
                    !isAfterTime(toTime, objects.get(i).getTime()))
                filteredTime.add(objects.get(i));
        }
        return filteredTime;
    }

    /**
     * SEARCH HELPER: Filter database results by BGL Values
     *
     * @param objects
     * @return filtered list by the Bgl Values
     */
    private ArrayList<DB_Object> filterByBglValue(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY BGL VALUE");
        Log.v("RESULTS", "BGL VAL - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filterdBglValue = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getActivityType().equals("Blood Glucose")) {
                if (isBetweenBglValue(sp.getString("start_val", ""), objects.get(i).getDescription(), sp.getString("end_val", ""))) {
                    filterdBglValue.add(objects.get(i));
                }
            } else {
                filterdBglValue.add(objects.get(i));
            }
        }
        return filterdBglValue;
    }

    /**
     * SEARCH HELPER: Filter database results by keywords
     *
     * @param objects
     * @return filtered list of keywords
     */
    private ArrayList<DB_Object> filterByKeyWords(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY KEYWORDS");
        Log.v("RESULTS", "KEYWORDS - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredKeyWords = new ArrayList<>();
        String containsKeywords = sp.getString("keywords", "");
        for (int i = 0; i < objects.size(); i++) {
            if (!objects.get(i).getActivityType().equals("Blood Glucose")) {
                if (containsWords(containsKeywords, objects.get(i).getDescription())) {
                    filteredKeyWords.add(objects.get(i));
                }
            } else filteredKeyWords.add(objects.get(i));
        }
        return filteredKeyWords;
    }

    /**
     * SEARCH HELPER: Compares between dates - afterDate
     *
     * @param fromDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    private boolean isAfterDate(String fromDate, String dataBaseDate) {
        if (fromDate.equals("")) {
            return true;
        }
        String[] dateOne = fromDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        if (Integer.parseInt(dateOne[2]) > Integer.parseInt(dateTwo[2])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) < Integer.parseInt(dateTwo[2])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return true;
        } else return Integer.parseInt(dateOne[1]) <= Integer.parseInt(dateTwo[1]);

    }

    /**
     * SEARCH HELPER: Compares between dates - beforeDate
     *
     * @param toDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    private boolean isBeforeDate(String toDate, String dataBaseDate) {
        if (toDate.equals("")) {
            return true;
        }
        String[] dateOne = toDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        if (Integer.parseInt(dateOne[2]) < Integer.parseInt(dateTwo[2])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) > Integer.parseInt(dateTwo[2])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return true;
        } else return Integer.parseInt(dateOne[1]) >= Integer.parseInt(dateTwo[1]);
    }

    /**
     * SEARCH HELPER: Compares between times
     *
     * @param fromTime
     * @param databaseTime
     * @return filtered list of times
     */
    private boolean isAfterTime(String fromTime, String databaseTime) {
        if (fromTime.equals("")) {
            return true;
        }
        String[] timeOne = fromTime.split(":");
        String[] time = databaseTime.split(":");
        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        if (Integer.parseInt(timeOne[0]) < hour) {
            return true;
        } else if (Integer.parseInt(timeOne[0]) > hour) {
            return false;
        } else return (Integer.parseInt(timeOne[1]) <= minutes);
    }

    /**
     * SEARCH HELPER: Compares between BGL values
     *
     * @param bglValueFrom
     * @param bglValueData
     * @param bglValueTo
     * @return whether if actually between values
     */
    private boolean isBetweenBglValue(String bglValueFrom, String bglValueData, String bglValueTo) {
        Double valueFrom, valueTo;
        if (bglValueFrom.equals("") && bglValueTo.equals("")) {
            return true; // default return if both entries empty
        }

        if (!bglValueFrom.equals("")) {
            valueFrom = Double.parseDouble(bglValueFrom);
        } else {
            valueFrom = 40.0; //default min
        }
        if (!bglValueTo.equals("")) {
            valueTo = Double.parseDouble(bglValueTo);
        } else {
            valueTo = 600.0;
        }
        Double bglValueData2 = Double.parseDouble(bglValueData);

        return (valueFrom <= bglValueData2 && bglValueData2 <= valueTo);
    }

    /**
     * SEARCH HELPER: Compares using keywords
     *
     * @param keywordString
     * @param fromData
     * @return whether data contains keywords
     */
    private boolean containsWords(String keywordString, String fromData) {
        ArrayList<String> stringOne = new ArrayList<>(Arrays.asList(keywordString.split(" ")));
        if (keywordString.equals("") || keywordString.equals(null))
            return true;
        if (stringOne.contains("AND")) {
            return fromData.contains(stringOne.get(0)) && fromData.contains(stringOne.get(2));
        } else if (stringOne.contains("OR")) {
            return fromData.contains(stringOne.get(0)) || fromData.contains(stringOne.get(2));
        } else return fromData.contains(keywordString);
    }

    /**
     * Clear SharedPreferences upon submit
     */
    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
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
        clearSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSharedPreferences();
        db.close();
    }
}