package com.shrikantbadwaik.casestudy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DBAdapter.ExpenseAdapter;
import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class CalculateExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner categorySpinner;
    ListView selectedCategoryView;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<ExpenseClass> expenseList = new ArrayList<>();

    ArrayAdapter<String> categoryAdapter;
    ExpenseAdapter expenseAdapter;

    EditText editTotal, editDaily, editMax, editMin;

    int userId;

    String selectedCategory;
    Cursor cursor, cursorAvg, cursorMin, cursorMax, cursorTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_expense);

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        selectedCategoryView = (ListView) findViewById(R.id.selectedCategoryView);

        editTotal = (EditText) findViewById(R.id.editTotal);
        editDaily = (EditText) findViewById(R.id.editDaily);
        editMax = (EditText) findViewById(R.id.editMax);
        editMin = (EditText) findViewById(R.id.editMin);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        refresh();

        categorySpinner.setOnItemSelectedListener(this);
    }

    public void refresh() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "Select distinct " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " ; ";
        cursor = db.rawQuery(query, null);

        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String category = cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_CATEGORY_NAME));

                categoryList.add(category);
                categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList);

                categorySpinner.setAdapter(categoryAdapter);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_calculate_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_calculate) {
            ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
            final PackageManager pm = getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                if (pi.packageName.toString().toLowerCase().contains("calcul")) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("appName", pi.applicationInfo.loadLabel(pm));
                    map.put("packageName", pi.packageName);
                    items.add(map);
                }
            }

            if (items.size() >= 1) {
                String packageName = (String) items.get(0).get("packageName");
                Intent i = pm.getLaunchIntentForPackage(packageName);
                if (i != null)
                    startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        expenseList.clear();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        String category = (String) categorySpinner.getItemAtPosition(position);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "Select * from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId + " and " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " = '" + category + "' ;";
        cursor = db.rawQuery(query, null);

        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {


                ExpenseClass expense = new ExpenseClass();

                expense.setExpenseId(cursor.getInt(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_ID)));
                expense.setExpenseCategoryId(cursor.getInt(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_CATEGORY_ID)));
                expense.setExpenseCategoryName(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_CATEGORY_NAME)));
                expense.setExpenseName(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_NAME)));
                expense.setDescription(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DESCRIPTION)));
                expense.setExpenseDate(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_DATE)));
                expense.setExpenseAmount(cursor.getFloat(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_AMOUNT)));
                expense.setPaymentMode(cursor.getString(cursor.getColumnIndex(ExpenseClass.COL_EXPENSE_PAYMENT_MODE)));

                expenseList.add(expense);
                expenseAdapter = new ExpenseAdapter(this, expenseList);
                selectedCategoryView.setAdapter(expenseAdapter);

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        calculateDailyExpense();
        calculateMinExpense();
        calculateMaxExpense();
        calculateTotalExpense();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void calculateDailyExpense() {
        selectedCategory = categorySpinner.getSelectedItem().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String getTotal = " Select AVG(" + ExpenseClass.COL_EXPENSE_AMOUNT +
                ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId +
                " and " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " = '" + selectedCategory + "' ;";

        cursorAvg = db.rawQuery(getTotal, null);
        if (!cursorAvg.isAfterLast()) {
            cursorAvg.moveToFirst();
            while (!cursorAvg.isAfterLast()) {
                float amount = cursorAvg.getFloat(0);
                editDaily.setText("" + amount);
                cursorAvg.moveToNext();
            }
        }
        cursorAvg.close();
        db.close();

    }

    public void calculateMinExpense() {
        selectedCategory = categorySpinner.getSelectedItem().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String getMin = " Select MIN(" + ExpenseClass.COL_EXPENSE_AMOUNT + ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId + " and " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " = '" + selectedCategory + "' ;";
        cursorMin = db.rawQuery(getMin, null);

        if (!cursorMin.isAfterLast()) {
            cursorMin.moveToFirst();
            while (!cursorMin.isAfterLast()) {
                float amount = cursorMin.getFloat(0);
                editMin.setText("" + amount);
                cursorMin.moveToNext();
            }
        }
        cursorMin.close();
        db.close();
    }

    public void calculateMaxExpense() {
        selectedCategory = categorySpinner.getSelectedItem().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String getMax = " Select MAX(" + ExpenseClass.COL_EXPENSE_AMOUNT + ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId + " and " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " = '" + selectedCategory + "' ;";
        cursorMax = db.rawQuery(getMax, null);

        if (!cursorMax.isAfterLast()) {
            cursorMax.moveToFirst();
            while (!cursorMax.isAfterLast()) {
                float amount = cursorMax.getFloat(0);
                editMax.setText("" + amount);
                cursorMax.moveToNext();
            }
        }
        cursorMax.close();
        db.close();
    }

    public void calculateTotalExpense() {
        selectedCategory = categorySpinner.getSelectedItem().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String getTotal = " Select SUM(" + ExpenseClass.COL_EXPENSE_AMOUNT + ") from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " =" + userId + " and " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + " = '" + selectedCategory + "' ;";
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
    }
}
