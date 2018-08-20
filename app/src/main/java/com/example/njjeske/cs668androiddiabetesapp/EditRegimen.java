package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditRegimen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_type;
    private ImageView img;
    private EditText time, description;
    private Button saveButton, deleteButton;
    private int value;
    private DB_Object object;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_regimen);

        db = new DatabaseHelper(this);

        spinner_type = (Spinner) findViewById(R.id.spinner);
        spinner_type.setOnItemSelectedListener(this);

        img = (ImageView) findViewById(R.id.AddRegimen_image);
        description = (EditText) findViewById(R.id.AddRegimen_editText_value);
        time = (EditText) findViewById(R.id.AddRegimen_editText_time);
        saveButton = (Button) findViewById(R.id.AddRegimen_Submit);
        saveButton.setOnClickListener(submitOnClickListener);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        saveButton.setLayoutParams(lp);

        //create delete button
        int buttonStyle = R.style.button_delete;
        deleteButton = new Button(new ContextThemeWrapper(this, buttonStyle), null, buttonStyle);
        lp.setMargins(8, 8, 8, 8);
        deleteButton.setLayoutParams(lp);
        deleteButton.setText(R.string.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }

        });
        LinearLayout buttonrow = (LinearLayout) findViewById(R.id.AddRegimen_buttonRow);
        buttonrow.addView(deleteButton);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditRegimen.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String correctMinute = selectedMinute + "";
                        String correctHour = selectedHour + "";
                        if (selectedMinute < 10) {
                            correctMinute = "0" + selectedMinute;
                        }
                        if (selectedHour < 10) {
                            correctHour = "0" + selectedHour;
                        }
                        time.setText(correctHour + ":" + correctMinute);

                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            value = extra.getInt("Data");
            //The key argument here must match that used in the other activity
            object = getDB_Object(value);


            description.setText(object.description);
            time.setText(object.time);

            switch (object.activityType) {
                case "Blood Glucose":
                    img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                    break;
                case "Diet":
                    img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                    break;
                case "Exercise":
                    img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                    break;
                case "Medication":
                    img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                    break;
            }
        }
    }

    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!description.getText().toString().isEmpty() &&
                    !time.getText().toString().isEmpty()) {
                System.out.println("Spinner select: " + spinner_type.getSelectedItem().toString());
                object.setActivityType(spinner_type.getSelectedItem().toString());
                object.setTime(time.getText().toString());
                object.setDescription(description.getText().toString());

                db.updateRegimen(value, object);
                Log.v("EDITREGIMEN", String.format("%s was saved.", object.toString()));
                Toast.makeText(getApplicationContext(), String.format("%s: %s at %s was saved.", object.getActivityType(), object.getDescription(), object.getTime()),
                        Toast.LENGTH_SHORT).show();
                clearSharedPreferences();

                // check if data is actually in db (delete later)
                ArrayList<DB_Object> cursor = db.getAllRegimen();
                for (int i = 0; i < cursor.size(); i++) {
                    Log.v("EDITREGIMEN", "DB DATA: " + cursor.get(i).toString());
                }
                if (cursor.size() == 0) {
                    Log.v("EDITREGIMEN", "CURSOR EMPTY");
                }
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Cannot submit, empty values in text fields.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void delete(View v) {
        final int _id = object.getId();

        AlertDialog.Builder builder1 =
                new AlertDialog.Builder(EditRegimen.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder1.setTitle("Delete");
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.dialog_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (db.deleteRowByID(_id, "RegimenActivity")) {
                            System.out.println("Deleted item with id " + _id);
                            Toast.makeText(getApplicationContext(), "Regimen item was deleted.",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            System.out.println("ERROR: Cannot delete item.");
                        }
                    }
                });

        builder1.setNegativeButton(
                R.string.dialog_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog delete_alert = builder1.create();
        delete_alert.show();
    }

    /**
     * Performing Spinner action onItemSelected to change image & label description depending on activity type
     *
     * @param arg0
     * @param arg1
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        String[] activityNames = {"Blood Glucose","Food","Exercise","Medicine"};
//        Toast.makeText(getApplicationContext(), activityNames[position], Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0: // blood glucose
                img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                break;
            case 1: // food
                img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                break;
            case 2: // exercise
                img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                break;
            case 3: // medication
                img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private DB_Object getDB_Object(int id) {
        ArrayList<DB_Object> arrayOfActivities = db.getAllRegimen();
        DB_Object object = new DB_Object();

        for (int i = 0; i < arrayOfActivities.size(); i++) {
            if (id == arrayOfActivities.get(i).id) {
                object = arrayOfActivities.get(i);
                System.out.println("OBJECT RETRIEVED: " + object.toString());
            }
        }

        return object;
    }

    /**
     * Save SharedPreferences as extra precaution
     */
    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("spinnerIndex", spinner_type.getSelectedItemPosition());
        editor.putString("time", time.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.commit();
    }

    /**
     * Show SharedPreferences: use inside onCreate()
     */
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            spinner_type.setSelection(sp.getInt("spinnerIndex", 0));
            time.setText(sp.getString("time", ""));
            description.setText(sp.getString("description", ""));
        }
    }

    /**
     * Clear SharedPreferences upon submit
     */
    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("AddRegimenInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSharedPreferences();
        db.close();
    }
}
