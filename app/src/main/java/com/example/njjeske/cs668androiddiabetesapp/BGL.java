package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BGL extends AppCompatActivity {
    private Button submitbutton;
    private EditText time_editText, value_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgl);

        submitbutton = findViewById(R.id.BGL_Submit);
        time_editText = findViewById(R.id.BGL_editText_time);
        value_editText = findViewById(R.id.BGL_editText_value);

        submitbutton.setOnClickListener(submitOnClickListener);
    }


    private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submit(v);
        }
    };

    private void submit(View v) {
        String submitString;
        submitString = String.format("%s, %s", time_editText.getText().toString(), value_editText.getText().toString());
        Intent i;
        i = new Intent(this, Results.class);
        i.putExtra("bgl_info", submitString);
        startActivity(i);
    }
}
