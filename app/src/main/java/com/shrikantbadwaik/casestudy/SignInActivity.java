package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import DBClasses.UserClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class SignInActivity extends AppCompatActivity {

    EditText editUserName, editPassword;
    CheckBox checkRememberMe;
    ProgressDialog progressDialog;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);

        checkRememberMe = (CheckBox) findViewById(R.id.checkRememberMe);

        editUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editUserName.getWindowToken(), 0);
                }
            }
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editPassword.getWindowToken(), 0);
                }
            }
        });

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editUserName.setText(loginPreferences.getString("username", ""));
            editPassword.setText(loginPreferences.getString("password", ""));
            checkRememberMe.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Exit")) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void signIn(View view) {
        if (editUserName.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else {
            final UserClass user = new UserClass();

            user.setUserName(editUserName.getText().toString());
            user.setPassword(editPassword.getText().toString());

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
            preferences1.edit()
                    .putString(Constants.LOGIN_NAME, editUserName.getText().toString())
                    .commit();

            if (user.authenticate(this,checkRememberMe.isChecked()) == true) {

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setTitle("please wait...");
                progressDialog.setMessage("Logging in...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            progressDialog.dismiss();

                            Intent intent = new Intent(SignInActivity.this, ExpanseActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            if (user.authenticate(this,checkRememberMe.isChecked()) == false) {
                Toast.makeText(this, "Invalid User Name or Password", Toast.LENGTH_SHORT).show();
            }

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editUserName.getWindowToken(), 0);

            if (checkRememberMe.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", editUserName.getText().toString());
                loginPrefsEditor.putString("password", editPassword.getText().toString());
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
        }
    }

    public void forgotPassword(View view) {
        if ((editUserName.getText().toString().length() != 0) && (editPassword.getText().toString().length() != 0)) {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        } else {
            Toast.makeText(this, "Please Enter UserName and Password!!!", Toast.LENGTH_SHORT).show();
        }
    }
}