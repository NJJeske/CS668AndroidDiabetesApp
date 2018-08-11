package com.example.njjeske.cs668androiddiabetesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Create extends AppCompatActivity {

    private EditText email, reEnterEmail, pw, reEnterPw;
    private Button createUser;
    private TextView createErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        //EditText Views
        email = (EditText) findViewById(R.id.create_new_email);
        reEnterEmail = (EditText) findViewById(R.id.create_re_enter_email);
        pw = (EditText) findViewById(R.id.create_pw);
        reEnterPw = (EditText) findViewById(R.id.create_re_enter_pw);
        //Error Message
        createErrorMsg = (TextView) findViewById(R.id.create_error_msg);
        //Button
        createUser = (Button) findViewById(R.id.create_newUser_btn);


    }


}
