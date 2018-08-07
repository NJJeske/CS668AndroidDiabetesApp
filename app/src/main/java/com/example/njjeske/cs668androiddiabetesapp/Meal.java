package com.example.njjeske.cs668androiddiabetesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import android.widget.Toast;

public class Meal extends AppCompatActivity {

    private EditText name;
    private EditText amount;
    private EditText time;
    private Button submit;
    private DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        name = (EditText) findViewById(R.id.Meal_editText_name);
        amount = (EditText) findViewById(R.id.Meal_editText_amount);
        time = (EditText) findViewById(R.id.Meal_editText_time);
        submit = (Button) findViewById(R.id.Meal_Submit);
        DB = new DatabaseHelper(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }

        });
    }

    private void submit(View v) {

//        DB.insertData("Diet",time.getText().toString(),amount.getText().toString(),name.getText().toString());

        Toast.makeText(getApplicationContext(), "Submitted your data!",
                Toast.LENGTH_SHORT).show();
        clear();
    }


    private void clear() {

        name.setText("");
        amount.setText("");
        time.setText("");

    }
}