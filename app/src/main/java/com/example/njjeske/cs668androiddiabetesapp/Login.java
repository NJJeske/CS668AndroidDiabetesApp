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
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private TextView create;
    private Button login;
    private CheckBox rememberMe;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_email_login);
        password = (EditText) findViewById(R.id.login_password_login);
        login = (Button) findViewById(R.id.login_button);
        create = (TextView) findViewById(R.id.createAccount_login);
        rememberMe = (CheckBox) findViewById(R.id.login_checkBox_rememberMe);

        db = new DatabaseHelper(this);

        showSharedPreferences();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNotEmpty()) {
                    //See if user exists
                    if (!db.isUserRegistered(email.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_SHORT).show();
                    } else {
                        User user;
                        user = db.getUserByEmail(email.getText().toString());
                        if (user.getPassword().equals(password.getText().toString())) {
                            saveSharedPreferences(true);
                            Intent intent = new Intent(Login.this, Home.class);
                            intent.putExtra("userName", user.getName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private boolean checkNotEmpty() {
        if (!email.getText().toString().equals("") &&
                !password.getText().toString().equals("")) {
            System.out.println("Fields not empty");
            return true;
        } else {
            System.out.println("Fields empty");
            Toast.makeText(getApplicationContext(), "Email or password is empty. Please fill it in.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SQLCipherInit() {
        SQLiteDatabase.loadLibs(this);
        File dbFile = getDatabasePath("DIABETES_DB.db");
        dbFile.mkdirs();
        dbFile.delete();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, "password", null);
        db.close();
    }

    //use this inside onPause() & login onClickListener
    public void saveSharedPreferences(boolean loggingIn) {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        if (loggingIn) {
            editor.putBoolean("loggedIn", true);
            editor.putString("password", ""); //never remember password
        }

        if (rememberMe.isChecked())
            editor.putString("checkBox", "checked");
        else editor.putString("checkBox", "");
        editor.commit();
    }

    //use this inside onCreate() to go directly to Home
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);

        Boolean islog = sp.getBoolean("loggedIn", false);
        if (islog) {
            if (sp.getString("checkBox", "").equals("checked")) {
                // go directly to Home
                Intent intent = new Intent(Login.this, Home.class);
                intent.putExtra("userName", sp.getString("name", ""));
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rememberMe.isChecked()) {
            saveSharedPreferences(false);
        } else {
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
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
        db.close();
        super.onDestroy();
    }

}
