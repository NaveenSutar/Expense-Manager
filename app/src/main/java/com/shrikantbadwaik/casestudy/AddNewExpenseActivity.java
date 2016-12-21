package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DBClasses.Category;
import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class AddNewExpenseActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener dateSetListener;

    TextView categoryTitle, datePicker;
    EditText itemName, itemDescription, itemAmount;

    Spinner paymentSpinner;

    String categoryName;

    ArrayList<String> paymentList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);

        categoryTitle = (TextView) findViewById(R.id.CategoryTitle);
        datePicker = (TextView) findViewById(R.id.datePicker);
        itemName = (EditText) findViewById(R.id.itemName);
        itemDescription = (EditText) findViewById(R.id.itemDescription);
        itemAmount = (EditText) findViewById(R.id.itemAmount);
        paymentSpinner = (Spinner) findViewById(R.id.paymentSpinner);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Add New Expense");

        Intent intent = getIntent();
        Category category = intent.getParcelableExtra("Category");
        categoryTitle.setText(category.getCategoryTitle());
        categoryName = category.getCategoryTitle();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                try {

                    date = dateFormat.parse(String.format("%d/%d/%d", day, (month + 1), year));
                    String setdate = dateFormat.format(date);
                    datePicker.setText(setdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        paymentList.add("By Cash");
        paymentList.add("By Cheque");
        paymentList.add("By Debit Card");
        paymentList.add("By Credit Card");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paymentList);
        paymentSpinner.setAdapter(adapter);

        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(itemName.getWindowToken(), 0);
                }
            }
        });

        itemDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(itemDescription.getWindowToken(), 0);
                }
            }
        });

        itemAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(itemAmount.getWindowToken(), 0);
                }
            }
        });
    }

    public void setDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        dialog.show();
    }

    public void saveExpense(View view) {
        if (datePicker.getText().toString().length() == 0) {
            Toast.makeText(this, "Please Select Date", Toast.LENGTH_SHORT).show();
        } else if (itemName.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (itemDescription.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else if (itemAmount.getText().toString().length() == 0) {
            Toast.makeText(this, "Can not left empty!!!", Toast.LENGTH_SHORT).show();
        } else {

            Helper helper = new Helper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            Category category = getIntent().getParcelableExtra("Category");

            ContentValues values = new ContentValues();

            values.put(ExpenseClass.COL_EXPENSE_CATEGORY_ID, category.getCategoryId());
            values.put(ExpenseClass.COL_EXPENSE_DATE, datePicker.getText().toString());
            values.put(ExpenseClass.COL_EXPENSE_NAME, itemName.getText().toString());
            values.put(ExpenseClass.COL_EXPENSE_DESCRIPTION, itemDescription.getText().toString());
            values.put(ExpenseClass.COL_EXPENSE_AMOUNT, Float.parseFloat(itemAmount.getText().toString()));
            values.put(ExpenseClass.COL_EXPENSE_PAYMENT_MODE, paymentSpinner.getSelectedItem().toString());
            values.put(ExpenseClass.COL_EXPENSE_CATEGORY_NAME, categoryName);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            values.put(ExpenseClass.COL_EXPENSE_USER_ID, preferences.getInt(Constants.LOGIN_ID, 0));

            db.insert(ExpenseClass.TABLE_EXPENSE, null, values);

            db.close();

            Toast.makeText(this, "Expense Added Successfully!!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void clearExpense(View view) {
        datePicker.setText("");
        itemName.setText("");
        itemDescription.setText("");
        itemAmount.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
