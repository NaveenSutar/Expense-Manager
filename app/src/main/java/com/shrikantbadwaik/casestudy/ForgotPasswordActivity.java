package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import DBClasses.UserClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editNewPassword, editConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editNewPassword = (EditText) findViewById(R.id.editNewPassword);
        editConfirmNewPassword = (EditText) findViewById(R.id.editConfirmNewPassword);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        editNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editNewPassword.getWindowToken(), 0);
                }
            }
        });

        editConfirmNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editConfirmNewPassword.getWindowToken(), 0);
                }
            }
        });
    }

    public void savePassword(View view) {
        if (editNewPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editConfirmNewPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (editNewPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must contain atleast 6 characters", Toast.LENGTH_SHORT).show();
        } else if (!editConfirmNewPassword.getText().toString().equals(editNewPassword.getText().toString())) {
            Toast.makeText(this, "Passwords does not match!!!", Toast.LENGTH_SHORT).show();
        } else {
            Helper helper = new Helper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
            String userLogin = preferences1.getString(Constants.LOGIN_NAME, "");

            ContentValues values = new ContentValues();

            values.put(UserClass.COL_PASSWORD, editConfirmNewPassword.getText().toString());

            String whereClause = UserClass.COL_USER_NAME + " = '" + userLogin + "' ;";
            db.update(UserClass.TABLE_USER, values, whereClause, null);

            db.close();
            finish();

            Toast.makeText(this, "Password Updated Successfully!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view) {
        editNewPassword.setText("");
        editConfirmNewPassword.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
