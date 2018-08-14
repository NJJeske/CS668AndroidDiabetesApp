package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    private EditText email, pw;
    private TextView create;
    private Button login;
    private CheckBox rememberMe;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_email_login);
        pw = (EditText) findViewById(R.id.login_password_login);
        login = (Button) findViewById(R.id.login_button);
        create = (TextView) findViewById(R.id.createAccount_login);
        rememberMe = (CheckBox) findViewById(R.id.login_checkBox_rememberMe);

        db = new DatabaseHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getSharedPreferences("USERPREFS", MODE_PRIVATE);
                User user = new User();
                user.setName(email.getText().toString());
                user.setPassword(pw.getText().toString());
                boolean registered = db.isUserRegistered(user.getName());

                if (registered) {
                    if (rememberMe.isChecked()) {
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(email.getText().toString(), pw.getText().toString());
                        editor.putBoolean("rememberUser", true);
                        editor.commit();
                    }

                    Intent home = new Intent(Login.this, Home.class);
                    startActivity(home);
                }

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createUser = new Intent(Login.this, Create.class);
                startActivity(createUser);
            }
        });
    }




}
