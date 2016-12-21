package DBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.Toast;

import DBHelper.Helper;
import DBUtility.Constants;

/**
 * Created by Deadpool on 5/10/2016.
 */

public class UserClass {
    public static final String TABLE_USER = "users";
    public static final String COL_USER_ID = "userid";
    public static final String COL_FULL_NAME = "fullname";
    public static final String COL_USER_NAME = "username";
    public static final String COL_PASSWORD = "password";

    private int userId;
    private String fullName;
    private String userName;
    private String password;
    private String confirmPassword;

    public UserClass() {
    }

    public UserClass(int userId, String fullName, String userName, String password, String confirmPassword) {
        this.userId = userId;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean insertUsers(Context context) {
        boolean result = true;

        Helper helper = new Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = "Select * from " + UserClass.TABLE_USER + " where " + UserClass.COL_USER_NAME + " = '" + userName + "';";

        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.isAfterLast()) {
            Toast.makeText(context, "User Name Already Exists...", Toast.LENGTH_SHORT).show();
            result = false;
        } else {
            ContentValues values = new ContentValues();

            values.put(COL_FULL_NAME, fullName);
            values.put(COL_USER_NAME, userName);
            values.put(COL_PASSWORD, password);

            db.insert(TABLE_USER, null, values);
            result = true;

        }
        cursor.close();
        db.close();

        return result;
    }

    public boolean authenticate(Context context, boolean rememberMe) {
        boolean result = true;

        Helper helper = new Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = "select * from " + TABLE_USER + " WHERE "
                + COL_USER_NAME + " = '" + userName + "' and "
                + COL_PASSWORD + " = '" + password + "';";

        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();

            String fullName = cursor.getString(cursor.getColumnIndex(UserClass.COL_FULL_NAME));
            int userId = cursor.getInt(cursor.getColumnIndex(UserClass.COL_USER_ID));

            result = true;

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit()
                    .putBoolean(Constants.LOGIN_STATUS, true)
                    .putInt(Constants.LOGIN_ID, userId)
                    .putString(Constants.LOGIN_NAME, fullName)
                    .commit();
        } else {
            result = false;
        }

        cursor.close();
        db.close();

        return result;
    }
}
