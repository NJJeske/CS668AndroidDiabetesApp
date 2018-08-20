package com.example.njjeske.cs668androiddiabetesapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private TextView header;
    private EditText description;
    private EditText date;
    private EditText time;
    private Button submit, deleteBtn;
    private DatabaseHelper db;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int value;
    private DB_Object object;
    Calendar c;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        header = (TextView) findViewById(R.id.Activity_heading);
        description = (EditText) findViewById(R.id.Edit_editText_name);
        date = (EditText) findViewById(R.id.Edit_editText_date);
        time = (EditText) findViewById(R.id.Edit_editText_time);
        submit = (Button) findViewById(R.id.Activity_Edit);
        deleteBtn = (Button) findViewById(R.id.Activity_delete);
        img = (ImageView) findViewById(R.id.EditActivity_image);
        db = new DatabaseHelper(this);


        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
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


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        String strDate = format.format(calendar.getTime());
                        date.setText(strDate);

                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(EditActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, mYear, mMonth, mDay);
                dpDialog.show();
            }
        });


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            value = extra.getInt("Data");
            //The key argument here must match that used in the other activity
            object = getDB_Object(value);


            header.setText(object.activityType);
            description.setText(object.description);
            date.setText(object.date);
            time.setText(object.time);

            switch (object.activityType) {
                case "Blood Glucose":
                    description.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    img.setImageResource(R.drawable.ic_icons8_diabetes_filled_24);
                    break;
                case "Diet":
                    description.setInputType(InputType.TYPE_CLASS_TEXT);
                    img.setImageResource(R.drawable.ic_icons8_vegetarian_food_filled_24);
                    break;
                case "Exercise":
                    description.setInputType(InputType.TYPE_CLASS_TEXT);
                    img.setImageResource(R.drawable.ic_icons8_walking_filled_24);
                    break;
                case "Medication":
                    description.setInputType(InputType.TYPE_CLASS_TEXT);
                    img.setImageResource(R.drawable.ic_icons8_pills_filled_24);
                    break;
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((header.getText().toString().equals("Blood Glucose") && !(Double.parseDouble(description.getText().toString()) < 40.0 ||
                        Double.parseDouble(description.getText().toString()) > 600.0))) {
                    submit(v);
                } else {
                    Toast.makeText(getApplicationContext(), "BGL value is outside the range (40-600).",
                            Toast.LENGTH_SHORT).show();
                }
                submit(v);
            }

        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }

        });
    }

    private void submit(View v) {

        if (!description.getText().toString().isEmpty() &&
                !date.getText().toString().isEmpty() &&
                !time.getText().toString().isEmpty()) {
            object.setDate(date.getText().toString());
            object.setTime(time.getText().toString());
            object.setDescription(description.getText().toString());

            db.updateActivity(value, object);
            Log.v("EDITACTIVITY", String.format(header.getText().toString() + ": %s was saved.", object.toString()));
            Toast.makeText(getApplicationContext(), String.format(header.getText().toString() + " %s was saved.", ""),
                    Toast.LENGTH_LONG).show();
            clearSharedPreferences();


            // check if data is actually in db (delete later)
            Cursor cursor = db.getAllData();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Log.v("EDITACTIVITY", "DB DATA: " + String.format("%s, %s, %s, %s", cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                } while (cursor.moveToNext());
            } else {
                Log.v("EDITACTIVITY", "CURSOR EMPTY");
            }

        } else {
            Toast.makeText(getApplicationContext(), "Cannot submit, empty values in text fields.",
                    Toast.LENGTH_SHORT).show();
        }

        clear();
    }

    private void delete(View v) {
        final int _id = object.getId();

        AlertDialog.Builder builder1 =
                new AlertDialog.Builder(EditActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        builder1.setTitle("Delete");
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.dialog_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (db.deleteRowByID(_id)) {
                            System.out.println("Deleted item with id " + _id);
                            Toast.makeText(getApplicationContext(), String.format(header.getText().toString() + " %s was Deleted.", ""),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("ERROR: Cannot delete item.");
                        }

                        clear();
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


    private void clear() {
        finish();
    }

    private DB_Object getDB_Object(int id) {
        ArrayList<DB_Object> arrayOfActivities = db.getAllActivity();
        DB_Object object = new DB_Object();

        for (int i = 0; i < arrayOfActivities.size(); i++) {
            if (id == arrayOfActivities.get(i).id) {
                object = arrayOfActivities.get(i);
                System.out.println("OBJECT RETRIEVED: " + object.toString());
            }
        }

        return object;
    }

    public void clearSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("addActivityInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSharedPreferences();
        db.close();
    }

}