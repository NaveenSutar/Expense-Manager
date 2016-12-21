package com.shrikantbadwaik.casestudy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import DBClasses.ExpenseClass;
import DBFragment.AboutUsFragment;
import DBFragment.AddExpenseFragment;
import DBFragment.ListAllFragment;
import DBHelper.Helper;
import DBUtility.Constants;

public class ExpanseActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;

    AddExpenseFragment addExpenseFragment;
    ListAllFragment listAllFragment;
    AboutUsFragment aboutUsFragment;

    ProgressDialog progressDialog;

    private AdView mAdView;

    class ExpenseOperationAdapter extends FragmentStatePagerAdapter {

        public ExpenseOperationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "Add Expense";
                    break;
                case 1:
                    title = "List All";
                    break;
                case 2:
                    title = "About Us";
                    break;
            }
            return title;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = addExpenseFragment;
                    break;
                case 1:
                    fragment = listAllFragment;
                    break;
                case 2:
                    fragment = aboutUsFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    ExpenseOperationAdapter expenseOperationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanse);

        addExpenseFragment = new AddExpenseFragment();
        listAllFragment = new ListAllFragment();
        aboutUsFragment = new AboutUsFragment();

        pager = (ViewPager) findViewById(R.id.pager);
        expenseOperationAdapter = new ExpenseOperationAdapter(getSupportFragmentManager());
        pager.setAdapter(expenseOperationAdapter);

        tabLayout = (TabLayout) findViewById(R.id.expenseTabLayout);
        tabLayout.setupWithViewPager(pager);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userLogin = preferences.getString(Constants.LOGIN_NAME, "");

        Toast.makeText(this, "Welcome " + userLogin, Toast.LENGTH_SHORT).show();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add New Category");
        menu.add("Expense Report");
        menu.add("Calculate Expense");
        menu.add("Logout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Add New Category")) {
            Intent intent = new Intent(this, AddNewCategoryActivity.class);
            startActivity(intent);
        } else if (item.getTitle().equals("Expense Report")) {

            Helper helper = new Helper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int userId = preferences.getInt(Constants.LOGIN_ID, 0);

            String query = "SELECT * FROM " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " ;";

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.isAfterLast()) {
                if (cursor != null && cursor.getCount() == 0) {
                    Toast.makeText(this, "Please Add Expense Information", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(this, ExpenseReportActivity.class);
                startActivity(intent);
            }

            cursor.close();
            db.close();
        } else if (item.getTitle().equals("Calculate Expense")) {
            Helper helper = new Helper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int userId = preferences.getInt(Constants.LOGIN_ID, 0);

            String query = "SELECT * FROM " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " ;";

            Cursor cursor = db.rawQuery(query, null);

            if (cursor.isAfterLast()) {
                if (cursor != null && cursor.getCount() == 0) {
                    Toast.makeText(this, "Please Add Expense Information", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(this, CalculateExpenseActivity.class);
                startActivity(intent);
            }

            cursor.close();
            db.close();
        } else if (item.getTitle().equals("Logout")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning!!!");
            builder.setMessage("Are you sure you want to logout?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ExpanseActivity.this);
                    preferences.edit()
                            .putBoolean(Constants.LOGIN_STATUS, false)
                            .commit();

                    progressDialog = new ProgressDialog(ExpanseActivity.this);
                    progressDialog.setTitle("please wait...");
                    progressDialog.setMessage("Logging out...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(ExpanseActivity.this, SignInActivity.class));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    Toast.makeText(ExpanseActivity.this, "Thank You!!! Please Visit Again...", Toast.LENGTH_LONG).show();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
