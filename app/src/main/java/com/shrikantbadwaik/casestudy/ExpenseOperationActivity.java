package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import DBClasses.ExpenseClass;
import DBHelper.Helper;

public class ExpenseOperationActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener dateSetListener;

    TextView getCategoryTitle, getDate;
    EditText getItemName, getItemDescription, getItemAmount;

    Spinner getPaymentDetail;

    ArrayList<String> getPaymentList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date;

    ExpenseClass expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_operation);

        getCategoryTitle = (TextView) findViewById(R.id.getCategoryTitle);
        getDate = (TextView) findViewById(R.id.getDate);

        getItemName = (EditText) findViewById(R.id.getItemName);
        getItemDescription = (EditText) findViewById(R.id.getItemDescription);
        getItemAmount = (EditText) findViewById(R.id.getItemAmount);

        getPaymentDetail = (Spinner) findViewById(R.id.getPaymentDetail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                try {

                    date = dateFormat.parse(String.format("%d/%d/%d", day, (month + 1), year));
                    String setdate = dateFormat.format(date);
                    getDate.setText(setdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        getPaymentList.add("By Cash");
        getPaymentList.add("By Cheque");
        getPaymentList.add("By Debit Card");
        getPaymentList.add("By Credit Card");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getPaymentList);
        getPaymentDetail.setAdapter(adapter);

        Intent intent = getIntent();
        expense = intent.getParcelableExtra("Expenses");

        getCategoryTitle.setText(expense.getExpenseCategoryName());

        getDate.setText(expense.getExpenseDate());
        getItemName.setText(expense.getExpenseName());
        getItemDescription.setText(expense.getDescription());
        getItemAmount.setText("" + expense.getExpenseAmount());

        getItemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getItemName.getWindowToken(), 0);
                }
            }
        });

        getItemDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getItemDescription.getWindowToken(), 0);
                }
            }
        });

        getItemAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getItemAmount.getWindowToken(), 0);
                }
            }
        });

        if (expense.getPaymentMode().equals("By Cash")) {
            getPaymentDetail.setSelection(0);
        } else if (expense.getPaymentMode().equals("By Cheque")) {
            getPaymentDetail.setSelection(1);
        } else if (expense.getPaymentMode().equals("By Debit Card")) {
            getPaymentDetail.setSelection(2);
        } else if (expense.getPaymentMode().equals("By Credit Card")) {
            getPaymentDetail.setSelection(3);
        }

    }

    public void setDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        dialog.show();
    }

    public void updateExpense(View view) {
        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ExpenseClass.COL_EXPENSE_DATE, getDate.getText().toString());
        values.put(ExpenseClass.COL_EXPENSE_NAME, getItemName.getText().toString());
        values.put(ExpenseClass.COL_EXPENSE_DESCRIPTION, getItemDescription.getText().toString());
        values.put(ExpenseClass.COL_EXPENSE_AMOUNT, Float.parseFloat(getItemAmount.getText().toString()));
        values.put(ExpenseClass.COL_EXPENSE_PAYMENT_MODE, getPaymentDetail.getSelectedItem().toString());

        String whereClause = ExpenseClass.COL_EXPENSE_ID + " = " + expense.getExpenseId();
        db.update(ExpenseClass.TABLE_EXPENSE, values, whereClause, null);

        db.close();

        Toast.makeText(this, "Expense Updated Successfully!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void deleteExpense(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!!!");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Helper helper = new Helper(ExpenseOperationActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                String query = "DELETE FROM " + ExpenseClass.TABLE_EXPENSE + " WHERE " + ExpenseClass.COL_EXPENSE_ID + " = " + expense.getExpenseId();
                db.execSQL(query);

                db.close();

                Toast.makeText(ExpenseOperationActivity.this, "Expense Deleted Successfully!!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void cancelExpense(View view) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
