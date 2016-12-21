package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import DBClasses.UserClass;

public class SignUpActivity extends AppCompatActivity {

    EditText editFullName, editUser, editPwd, editConfirmPwd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFullName = (EditText) findViewById(R.id.editFullName);
        editUser = (EditText) findViewById(R.id.editUser);
        editPwd = (EditText) findViewById(R.id.editPwd);
        editConfirmPwd = (EditText) findViewById(R.id.editConfirmPwd);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        editFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editFullName.getWindowToken(), 0);
                }
            }
        });

        editUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editUser.getWindowToken(), 0);
                }
            }
        });

        editPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editPwd.getWindowToken(), 0);
                }
            }
        });

        editConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editConfirmPwd.getWindowToken(), 0);
                }
            }
        });
    }

    public void register(View view) {
        if (editFullName.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editUser.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editPwd.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editPwd.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
        } else if (editConfirmPwd.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (!(editPwd.getText().toString().equals(editConfirmPwd.getText().toString()))) {
            Toast.makeText(this, "Password and Confirm Password are mismatched!!!", Toast.LENGTH_SHORT).show();
        } else {
            UserClass user = new UserClass();

            user.setFullName(editFullName.getText().toString());
            user.setUserName(editUser.getText().toString());
            user.setPassword(editPwd.getText().toString());
            user.setConfirmPassword(editConfirmPwd.getText().toString());

            if (user.insertUsers(this) == true) {
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setTitle("please wait...");
                progressDialog.setMessage("Signing Up...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6000);
                            progressDialog.dismiss();
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public void clear(View view) {
        editFullName.setText("");
        editUser.setText("");
        editPwd.setText("");
        editConfirmPwd.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
