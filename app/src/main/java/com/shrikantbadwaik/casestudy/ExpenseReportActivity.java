package com.shrikantbadwaik.casestudy;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DBAdapter.ReportAdapter;
import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class ExpenseReportActivity extends AppCompatActivity {

    TextView textFullName, textSetFromDate, textSetToDate;
    ListView listReport;
    ArrayList<ExpenseClass> reportList = new ArrayList<>();
    ReportAdapter reportAdapter;
    private EditText editTotal;

    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date;
    private int userId;
    private Cursor cursorTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        textFullName = (TextView) findViewById(R.id.textFullName);
        textSetFromDate = (TextView) findViewById(R.id.textSetFromDate);
        textSetToDate = (TextView) findViewById(R.id.textSetToDate);

        editTotal=(EditText)findViewById(R.id.getTotalExpense);

        listReport = (ListView) findViewById(R.id.listReport);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userLogin = preferences.getString(Constants.LOGIN_NAME, "");

        textFullName.setText(userLogin);

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                try {

                    date = dateFormat.parse(String.format("%d/%d/%d", day, (month + 1), year));
                    String setdate = dateFormat.format(date);
                    textSetFromDate.setText(setdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                try {

                    date = dateFormat.parse(String.format("%d/%d/%d", day, (month + 1), year));
                    String setdate = dateFormat.format(date);
                    textSetToDate.setText(setdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String getTotal = " Select SUM(" + ExpenseClass.COL_EXPENSE_AMOUNT + ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId + " ;";
        cursorTotal = db.rawQuery(getTotal, null);

        if (!cursorTotal.isAfterLast()) {
            cursorTotal.moveToFirst();
            while (!cursorTotal.isAfterLast()) {
                float amount = cursorTotal.getFloat(0);
                editTotal.setText("" + amount);
                cursorTotal.moveToNext();
            }
        }
        cursorTotal.close();
        db.close();

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_report_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_refresh) {
            textSetFromDate.setText("");
            textSetToDate.setText("");

            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        reportList.clear();

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt(Constants.LOGIN_ID, 0);

        String query = "Select * from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " order by " + ExpenseClass.COL_EXPENSE_DATE + " ; ";
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ExpenseClass expense = new ExpenseClass();

                expense.setExpenseDate(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DATE)));
                expense.setExpenseName(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_NAME)));
                expense.setDescription(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DESCRIPTION)));
                expense.setExpenseAmount(cursor.getFloat(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_AMOUNT)));

                reportList.add(expense);

                reportAdapter = new ReportAdapter(this, reportList);
                listReport.setAdapter(reportAdapter);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        reportAdapter.notifyDataSetChanged();
    }

    public void setExpFromDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog1 = new DatePickerDialog(this, dateSetListener1, year, month, day);
        dialog1.show();
    }

    public void setExpToDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog2 = new DatePickerDialog(this, dateSetListener2, year, month, day);
        dialog2.show();
    }

    public void getReport(View view) {

        String fromDate = textSetFromDate.getText().toString();
        String toDate = textSetToDate.getText().toString();

        if (textSetFromDate.getText().toString().length() == 0) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
        } else if (textSetToDate.getText().toString().length() == 0) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
        } else {
            reportList.clear();

            Helper helper = new Helper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            String query = "Select * from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId +" and "+ ExpenseClass.COL_EXPENSE_DATE + " between '" + fromDate + "' AND '" + toDate + "' order by " + ExpenseClass.COL_EXPENSE_DATE + " ;";
            Cursor cursor = db.rawQuery(query, null);

            if (!cursor.isAfterLast()) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    ExpenseClass expense = new ExpenseClass();

                    expense.setExpenseDate(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DATE)));
                    expense.setExpenseName(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_NAME)));
                    expense.setDescription(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DESCRIPTION)));
                    expense.setExpenseAmount(cursor.getFloat(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_AMOUNT)));

                    reportList.add(expense);

                    reportAdapter = new ReportAdapter(this, reportList);
                    listReport.setAdapter(reportAdapter);
                    cursor.moveToNext();
                }
            }

            String getTotal = " Select SUM(" + ExpenseClass.COL_EXPENSE_AMOUNT + ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId +
                              " and "+ExpenseClass.COL_EXPENSE_DATE+" BETWEEN '"+textSetFromDate.getText().toString()+"' and '"+textSetToDate.getText().toString()+"';";
            cursorTotal = db.rawQuery(getTotal, null);

            if (!cursorTotal.isAfterLast()) {
                cursorTotal.moveToFirst();
                while (!cursorTotal.isAfterLast()) {
                    float amount = cursorTotal.getFloat(0);
                    editTotal.setText("" + amount);
                    cursorTotal.moveToNext();
                }
            }

            cursorTotal.close();
            cursor.close();
            db.close();

            reportAdapter.notifyDataSetChanged();
        }
    }
}
