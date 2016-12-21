package com.shrikantbadwaik.casestudy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import DBAdapter.ExpenseAdapter;
import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class ListCategoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listViewCategory;
    ArrayList<ExpenseClass> arrayList = new ArrayList<>();
    ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        listViewCategory = (ListView) findViewById(R.id.listViewCategory);
        listViewCategory.setOnItemClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    public void refresh() {
        arrayList.clear();

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt(Constants.LOGIN_ID, 0);

        String query = "Select * from " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " order by " + ExpenseClass.COL_EXPENSE_CATEGORY_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);

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

                arrayList.add(expense);

                expenseAdapter = new ExpenseAdapter(this, arrayList);
                listViewCategory.setAdapter(expenseAdapter);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        expenseAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpenseClass expense = arrayList.get(position);

        Intent intent = new Intent(this, ExpenseOperationActivity.class);
        intent.putExtra("Expenses", expense);
        startActivity(intent);
    }
}
