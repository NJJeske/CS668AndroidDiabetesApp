package com.example.njjeske.cs668androiddiabetesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.SharedPreferences;
import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private CardView bglCard, foodCard, exerciseCard, medicineCard;
    private TextView welcome, logout;
    private ListView lvItems;
    private ArrayList<DB_Object> arrayOfActivities;
    private static final String HOME = "Home";
    DatabaseHelper db;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);

        welcome = (TextView) findViewById(R.id.welcome);
//        welcome.setText("Welcome "+name);

        // defining Cards
        bglCard = (CardView) findViewById(R.id.bgl_card);
        foodCard = (CardView) findViewById(R.id.food_card);
        exerciseCard = (CardView) findViewById(R.id.exercise_card);
        medicineCard = (CardView) findViewById(R.id.medicine_card);

        // Add click listeners to the card
        bglCard.setOnClickListener(cardOnClickListener);
        foodCard.setOnClickListener(cardOnClickListener);
        exerciseCard.setOnClickListener(cardOnClickListener);
        medicineCard.setOnClickListener(cardOnClickListener);

        // Bottom Navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Ensure correct menu item is selected (where the magic happens)
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0); //add activity index
        menuItem.setChecked(true);

        // setup listview
        fillListView();
    }

    /**
     * Fills the ListView with items from DataAdapter
     */
    private void fillListView() {
        Log.v("HOME","fillListView()");
        // Find ListView to populate
        lvItems = (ListView) findViewById(R.id.Home_listView);
        lvItems.setPadding(20, 10, 20, 10);
        lvItems.setDivider(new ColorDrawable(Color.TRANSPARENT));
        lvItems.setDividerHeight(20);
        // Construct the data source: MOST RECENT LIMIT 3
        arrayOfActivities = db.getAllActivity();
        ArrayList<DB_Object> temp = new ArrayList<>();
        for (int i = 0; i < arrayOfActivities.size(); i++) {
            if (arrayOfActivities.get(i) != null && i < 3) {
                temp.add(arrayOfActivities.get(i));
            }
        }
        arrayOfActivities = temp;

        DataAdapter dataAdapter = new DataAdapter(this, arrayOfActivities);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(dataAdapter);

        /**
         *  Used to get the selected activity for edit
         */
        lvItems.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Intent i = new Intent(Home.this, EditActivity.class);
                DB_Object obj = (DB_Object) lvItems.getItemAtPosition(position);
                System.out.println("Item: " + obj.toString());
                i.putExtra("Data", obj.getId());
                startActivity(i);

            }
        });
    }

    // This method will add main_menu.xml to this activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    private View.OnClickListener cardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Home.this, AddActivity.class);

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
    };

    /**
     * Bottom Navigation actions for onClick
     */
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
                    startActivity(new Intent(Home.this, SearchActivity.class));
                    break;
                case R.id.navigation_regimen:
                    startActivity(new Intent(Home.this, Regimen.class));
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Logout_id:
                Log.v("HOME", "HOME: Menu > Logout SELECTED");
                // logout popup
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
                                Intent i = new Intent(Home.this, Login.class);
                                Toast.makeText(getApplicationContext(), "Logged out!",
                                        Toast.LENGTH_SHORT).show(); // temp

                                SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("loggedin", false);
                                editor.putString("name", "");
                                editor.commit();
                                i.putExtra("userName", "");
                                finish();
                                startActivity(i);
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
                return true;
            case R.id.about_us_id:
                Log.v("HOME", "HOME: Menu > About SELECTED");
                startActivity(new Intent(Home.this, About.class));
                return true;
            case R.id.contact_us_id:
                Log.v("HOME", "HOME: Menu > Contact Us SELECTED");
                startActivity(new Intent(Home.this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
