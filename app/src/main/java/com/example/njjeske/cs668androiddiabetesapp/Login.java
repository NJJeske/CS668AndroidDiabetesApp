package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

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

        showSharedPreferences();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //See if user exists
                if (!db.isUserRegistered(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_SHORT).show();
                } else {
                    User user;
                    user = db.getUserByUserName(email.getText().toString());
                    if (user.getPassword().equals(pw.getText().toString())) {
                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("userName", user.getName());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//                SharedPreferences preferences = getSharedPreferences("USERPREFS", MODE_PRIVATE);
//                User user = new User();
//                user.setName(email.getText().toString());
//                user.setPassword(pw.getText().toString());
//                boolean registered = db.isUserRegistered(user.getName());
//
//                if (registered) {
//                    if (rememberMe.isChecked()) {
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        email.setText(db.getUserByUserName(user.getName()).toString());
//                        editor.putString(email.getText().toString(), pw.getText().toString());
//                        editor.putBoolean("rememberUser", true);
//                        editor.commit();
//                    }
//
//                    Intent home = new Intent(Login.this, Home.class);
//                    startActivity(home);
//                }

//            }
//        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent createUser = new Intent(Login.this, Create.class);
                startActivity(createUser);
            }
        });

    }

    private void SQLCipherInit() {
        SQLiteDatabase.loadLibs(this);
        File dbFile = getDatabasePath("DIABETES_DB.db");
        dbFile.mkdirs();
        dbFile.delete();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, "password", null);
        db.close();
    }

    //use this inside onPause()
    public void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", email.getText().toString());
        editor.putString("password", pw.getText().toString());
        editor.putBoolean("loggedin", true);
        if (rememberMe.isChecked())
            editor.putString("checkBox", "checked");
        else editor.putString("checkBox", "");
        editor.commit();
    }

    //use this inside onCreate()
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);

        Boolean islog = sp.getBoolean("loggedin", false);
        if (islog) {
            if (sp.getString("checkBox", "").equals("checked")) {
                //rememberMe.setChecked(true);
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
            saveSharedPreferences();
        } else {
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }
    }

}
