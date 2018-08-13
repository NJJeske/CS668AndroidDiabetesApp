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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class Results extends AppCompatActivity {
    BottomNavigationView navigation;
    DatabaseHelper db;
    LinearLayout contentView;
    Button graphs_btn, lists_btn, stats_btn;
    SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);

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
        ArrayList<DB_Object> arrayOfActivities = db.getAllActivity();
        DataAdapter dataAdapter = new DataAdapter(this, arrayOfActivities);

        // Attach adapter to the ListView
        lvItems.setAdapter(dataAdapter);

        // temp disabled for now
        if (sp.getBoolean("bgl_check", false)) {
            //dataAdapter.filterList(filterByType(filterByDate(filterByTime(filterByBglValue(filterByKeyWords(arrayOfActivities))))));
        } else {
            //dataAdapter.filterList(filterByType(filterByDate(filterByTime(filterByKeyWords(arrayOfActivities)))));
        }
    }

    /**
     * Filters by activity type
     *
     * @param objects
     * @return filtered list by type
     */
    private ArrayList<DB_Object> filterByType(ArrayList<DB_Object> objects) {
        //new array list that will hold the filtered data
        ArrayList<DB_Object> filteredDataByType = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("searchActivityInfo", Context.MODE_PRIVATE);
        if (sp != null) {
            for (int i = 0; i < objects.size(); i++) {
                String type = objects.get(i).getActivityType();
                if (type.equals("Blood Glucose") && sp.getBoolean("bgl_check", false)) {
                    filteredDataByType.add(objects.get(i));
                }
                if (type.equals("Exercise") && sp.getBoolean("exercise_check", false)) {
                    filteredDataByType.add(objects.get(i));
                }
                if (type.equals("Diet") && sp.getBoolean("diet_check", false)) {
                    filteredDataByType.add(objects.get(i));
                }
                if (type.equals("Medication") && sp.getBoolean("medication_check", false)) {
                    filteredDataByType.add(objects.get(i));
                }
            }
        }
        return filteredDataByType;
    }

    /**
     * Filters by date
     *
     * @param objects
     * @return filtered list by dates
     */
    private ArrayList<DB_Object> filterByDate(ArrayList<DB_Object> objects) {
        ArrayList<DB_Object> filterdData = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterDate(sp.getString("from_date", ""), objects.get(i).getDate()) &&
                    isBeforeDate(sp.getString("to_date", ""), objects.get(i).getDate()))
                filterdData.add(objects.get(i));

        }
        return filterdData;
    }

    /**
     * Filters list by time
     *
     * @param objects
     * @return filtered list by time
     */
    private ArrayList<DB_Object> filterByTime(ArrayList<DB_Object> objects) {
        ArrayList<DB_Object> filterdTime = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterTime(sp.getString("from_time", ""), objects.get(i).getDate()) &&
                    !isAfterTime(sp.getString("to_time", ""), objects.get(i).getDate()))
                filterdTime.add(objects.get(i));
        }
        return filterdTime;
    }

    /**
     * Filter database results by BGL Values
     *
     * @param objects
     * @return filtered list by the Bgl Values
     */
    private ArrayList<DB_Object> filterByBglValue(ArrayList<DB_Object> objects) {
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
     * Filter database results by keywords
     *
     * @param objects
     * @return filtered list of keywords
     */
    private ArrayList<DB_Object> filterByKeyWords(ArrayList<DB_Object> objects) {
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
     * Compares between dates - afterDate
     *
     * @param fromDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    private boolean isAfterDate(String fromDate, String dataBaseDate) {
        String[] dateOne = fromDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        String[] year1 = dateTwo[2].split(" ");
        if (Integer.parseInt(dateOne[2]) > Integer.parseInt(year1[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) < Integer.parseInt(year1[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return true;
        } else return Integer.parseInt(dateOne[1]) <= Integer.parseInt(dateTwo[1]);

    }

    /**
     * Compares between dates - beforeDate
     *
     * @param toDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    private boolean isBeforeDate(String toDate, String dataBaseDate) {
        String[] dateOne = toDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        String[] year1 = dateTwo[2].split(" ");
        if (Integer.parseInt(dateOne[2]) < Integer.parseInt(year1[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) > Integer.parseInt(year1[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return true;
        } else return Integer.parseInt(dateOne[1]) >= Integer.parseInt(dateTwo[1]);
    }

    /**
     * Compares between times
     *
     * @param fromTime
     * @param databaseTime
     * @return filtered list of times
     */
    private boolean isAfterTime(String fromTime, String databaseTime) {
        String[] timeOne = fromTime.split(":");
        String[] date = databaseTime.split("/");
        String[] YearAndTime = date[2].split(" ");
        String[] time = YearAndTime[1].split(":");
        Log.w("Values of Year And Time", YearAndTime[0] + " " + YearAndTime[1]);
        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        if (Integer.parseInt(timeOne[0]) < hour) {
            return true;
        } else if (Integer.parseInt(timeOne[0]) > hour) {
            return false;
        } else return (Integer.parseInt(timeOne[1]) <= minutes);
    }

    /**
     * Compares between BGL values
     *
     * @param bglValueFrom
     * @param bglValueData
     * @param bglValueTo
     * @return whether if actually between values
     */
    private boolean isBetweenBglValue(String bglValueFrom, String bglValueData, String bglValueTo) {
        Double valueFrom = Double.parseDouble(bglValueFrom);
        Double valueTo = Double.parseDouble(bglValueTo);
        Double bglValueData2 = Double.parseDouble(bglValueData);

        return (bglValueData2 < valueFrom || bglValueData2 > valueTo);
    }

    /**
     * Compares using keywords
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