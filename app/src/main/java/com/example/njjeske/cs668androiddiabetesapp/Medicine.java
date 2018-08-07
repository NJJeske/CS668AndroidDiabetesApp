package com.example.njjeske.cs668androiddiabetesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;


public class Medicine extends AppCompatActivity {


    private EditText name;
    private EditText value;
    private EditText time;
    private Button submit;
    private DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        name = (EditText) findViewById(R.id.Medicine_editText_name);
        value = (EditText) findViewById(R.id.Medicine_editText_value);
        time = (EditText) findViewById(R.id.Medicine_editText_time);
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

//      DB.insertData("Diet",time.getText().toString(),value.getText().toString(),name.getText().toString());

        Toast.makeText(getApplicationContext(), "Submitted your data!",
                Toast.LENGTH_SHORT).show();
        clear();
    }

    private void clear() {

        name.setText("");
        value.setText("");
        time.setText("");

    }

}
