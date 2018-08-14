package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        createUser.setOnClickListener(createUserOnClickListener);
    }

    private boolean match() {
        String uEmail, uReEmail, uPw, uRePw;
        uEmail = email.getText().toString();
        uReEmail = reEnterEmail.getText().toString();
        uPw = pw.getText().toString();
        uRePw = reEnterPw.getText().toString();

        return (uEmail.equals(uReEmail) && uPw.equals(uRePw));
    }

    private View.OnClickListener createUserOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (match()) {
                SharedPreferences preferences = getSharedPreferences("USERPREFS", MODE_PRIVATE);
                String userEmail = email.getText().toString();
                String userPw = pw.getText().toString();

                Toast.makeText(getApplicationContext(), "User Created",
                        Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(userEmail, userPw);
                editor.commit();

                Intent addActivity = new Intent(Create.this, AddActivity.class);
                startActivity(addActivity);

            } else {
                createErrorMsg.setText("Email and/or Username does not match");
            }

        }
    };

}
