package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Create extends AppCompatActivity {

    private EditText email, password, name_editText;
    private Button createUser;
    private TextView createErrorMsg, createPasswordRequirements;
    private CheckBox rememberMe;
    private DatabaseHelper db;
    private String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        //EditText Views
        email = (EditText) findViewById(R.id.create_new_email);
        name_editText = (EditText) findViewById(R.id.create_name);
        password = (EditText) findViewById(R.id.create_pw);

        //Error Message
        createErrorMsg = (TextView) findViewById(R.id.create_error_msg);
        createPasswordRequirements = (TextView) findViewById(R.id.create_pw_requirements);
        createPasswordRequirements.setVisibility(View.INVISIBLE);
        //Button
        createUser = (Button) findViewById(R.id.create_newUser_btn);
        createUser.setOnClickListener(createUserOnClickListener);

        rememberMe = (CheckBox) findViewById(R.id.login_checkBox_rememberMe);

        db = new DatabaseHelper(this);
    }

    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private boolean checkNotEmpty() {
        return email.length() > 0 &&
                name_editText.length() > 0 &&
                password.length() > 0;
    }

    private boolean allChecks() {
        String errMsg = "";
        boolean passesAll = false;
        if (checkNotEmpty() &&
                password.length() >= 4 &&
                isValid(email.getText().toString())) {
            createPasswordRequirements.setVisibility(View.INVISIBLE);
            passesAll = true;
        } else {
            if (!checkNotEmpty()) {
                errMsg += "Name, email, or password is empty. Please fill it in.";
                if (password.length() < 4) {
                    createPasswordRequirements.setVisibility(View.VISIBLE);
                } else {
                    createPasswordRequirements.setVisibility(View.INVISIBLE);
                }
                if (!isValid(email.getText().toString())) {
                    errMsg += "\n";
                    errMsg += "Email not in valid form (abc@email.com).";
                }
            } else {
                if (password.length() < 4) {
                    createPasswordRequirements.setVisibility(View.VISIBLE);
                } else {
                    createPasswordRequirements.setVisibility(View.INVISIBLE);
                }
                if (!isValid(email.getText().toString())) {
                    errMsg += "Email not in valid form (abc@email.com).";
                }
            }
        }
        createErrorMsg.setText(errMsg);
        return passesAll;
    }

    private View.OnClickListener createUserOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (allChecks()) {
                SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                String userEmail = email.getText().toString();
                String userName = name_editText.getText().toString();
                String userPw = password.getText().toString();

                if (!db.isUserRegistered(userEmail)) {
                    Toast.makeText(getApplicationContext(), "User Created",
                            Toast.LENGTH_LONG).show();

                    User user = new User(userEmail, userName, userPw);
                    db.addUser(user);

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("email", userEmail);
                    editor.putString("userInfo", userName);
                    editor.putBoolean("loggedIn", true);
                    if (rememberMe.isChecked())
                        editor.putString("checkBox", "checked");
                    else editor.putString("checkBox", "");
                    editor.commit();

                    Intent home = new Intent(Create.this, Home.class);
                    startActivity(home);
                } else {
                    Toast.makeText(getApplicationContext(), "User already created. Please login on previous page.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    private String encrypt(String data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = cipher.doFinal(data.getBytes());
        String encValue = Base64.encodeToString(encVal, Base64.DEFAULT);

        return encValue;

    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");

        digest.update(bytes, 0, bytes.length);

        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }

}
