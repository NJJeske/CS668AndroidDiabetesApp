package com.example.njjeske.cs668androiddiabetesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BGL extends AppCompatActivity {
    private Button submitbutton;
    private EditText time_editText, value_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgl);

        submitbutton = (Button) findViewById(R.id.BGL_Submit);
        time_editText = (EditText) findViewById(R.id.BGL_editText_time);
        value_editText = (EditText) findViewById(R.id.BGL_editText_value);

        submitbutton.setOnClickListener(submitOnClickListener);
    }


    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submit(v);
        }
    };

    // submit into database
    private void submit(View v) {
        String submitString;
        submitString = String.format("%s, %s", time_editText.getText().toString(), value_editText.getText().toString());

        // TODO add to database method in DatabaseHelper
//        DatabaseHelper.add(BGL.class,submitString);

        Toast.makeText(getApplicationContext(), "Submitted your data!",
                Toast.LENGTH_SHORT).show();
        clear();
    }

    private void clear() {
        time_editText.setText(null);
        value_editText.setText(null);
    }


}
