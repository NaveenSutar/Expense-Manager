package DBHelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import DBClasses.Category;
import DBClasses.ExpenseClass;
import DBClasses.UserClass;

/**
 * Created by Deadpool on 5/10/2016.
 */

public class Helper extends SQLiteOpenHelper {
    private static final String DB_NAME = "expanseManager.sqlite";
    private static final int DB_VERSION = 3;

    public Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + UserClass.TABLE_USER + " ("
                        + UserClass.COL_USER_ID + " integer primary key autoincrement, "
                        + UserClass.COL_FULL_NAME + " TEXT, "
                        + UserClass.COL_USER_NAME + " TEXT, "
                        + UserClass.COL_PASSWORD + " TEXT );");

        db.execSQL(
                "create table " + Category.TABLE_CATEGORY + " ( "
                        + Category.COL_CATEGORY_ID + " integer primary key autoincrement, "
                        + Category.COL_CATEGORY_TITLE + " TEXT, "
                        + Category.COL_IMAGE_NAME + " TEXT, "
                        + Category.COL_CATEGORY_USER_ID + " integer ); ");

        db.execSQL(
                "create table " + ExpenseClass.TABLE_EXPENSE + " ("
                        + ExpenseClass.COL_EXPENSE_ID + " integer primary key autoincrement, "
                        + ExpenseClass.COL_EXPENSE_DATE + " TEXT, "
                        + ExpenseClass.COL_EXPENSE_NAME + " TEXT, "
                        + ExpenseClass.COL_EXPENSE_DESCRIPTION + " TEXT, "
                        + ExpenseClass.COL_EXPENSE_AMOUNT + " integer, "
                        + ExpenseClass.COL_EXPENSE_PAYMENT_MODE + " TEXT, "
                        + ExpenseClass.COL_EXPENSE_CATEGORY_ID + " integer, "
                        + ExpenseClass.COL_EXPENSE_CATEGORY_NAME+" text, "
                        + ExpenseClass.COL_EXPENSE_USER_ID + " integer); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 3) {
            db.execSQL(
                    "create table " + Category.TABLE_CATEGORY + " ( "
                            + Category.COL_CATEGORY_ID + " integer primary key autoincrement, "
                            + Category.COL_CATEGORY_TITLE + " TEXT, "
                            + Category.COL_IMAGE_NAME + " TEXT, "
                            + Category.COL_CATEGORY_USER_ID + " integer); ");

            db.execSQL(
                    "create table " + ExpenseClass.TABLE_EXPENSE + " ("
                            + ExpenseClass.COL_EXPENSE_ID + " integer primary key autoincrement, "
                            + ExpenseClass.COL_EXPENSE_DATE + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_NAME + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_DESCRIPTION + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_AMOUNT + " integer, "
                            + ExpenseClass.COL_EXPENSE_PAYMENT_MODE + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_CATEGORY_ID + " integer, "
                            + ExpenseClass.COL_EXPENSE_CATEGORY_NAME+" text, "
                            + ExpenseClass.COL_EXPENSE_USER_ID + " integer); ");

        } else if (oldVersion == 2 && newVersion == 3) {
            db.execSQL(
                    "create table " + ExpenseClass.TABLE_EXPENSE + " ("
                            + ExpenseClass.COL_EXPENSE_ID + " integer primary key autoincrement, "
                            + ExpenseClass.COL_EXPENSE_DATE + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_NAME + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_DESCRIPTION + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_AMOUNT + " integer, "
                            + ExpenseClass.COL_EXPENSE_PAYMENT_MODE + " TEXT, "
                            + ExpenseClass.COL_EXPENSE_CATEGORY_ID + " integer, "
                            + ExpenseClass.COL_EXPENSE_CATEGORY_NAME+" text, "
                            + ExpenseClass.COL_EXPENSE_USER_ID + " integer); ");
        }

    }
}
