package DBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import DBHelper.Helper;

/**
 * Created by Deadpool on 5/21/2016.
 */
public class ExpenseClass implements Parcelable {
    public static final String TABLE_EXPENSE = "expenses";
    public static final String COL_EXPENSE_ID = "expenseid";
    public static final String COL_EXPENSE_DATE = "expensedate";
    public static final String COL_EXPENSE_NAME = "expensename";
    public static final String COL_EXPENSE_DESCRIPTION = "description";
    public static final String COL_EXPENSE_AMOUNT = "expenseamount";
    public static final String COL_EXPENSE_PAYMENT_MODE = "paymentmode";
    public static final String COL_EXPENSE_CATEGORY_ID = "expensecategoryid";
    public static final String COL_EXPENSE_CATEGORY_NAME="expensecategoryname";
    public static final String COL_EXPENSE_USER_ID = "expenseuserid";

    private int expenseId;
    private int expenseCategoryId;
    private String expenseCategoryName;
    private String expenseDate;
    private String expenseName;
    private String description;
    private float expenseAmount;
    private String paymentMode;

    public ExpenseClass() {
    }

    public ExpenseClass(int expenseId, int expenseCategoryId, String expenseCategoryName, String expenseDate, String expenseName, String description, float expenseAmount, String paymentMode) {
        this.expenseId = expenseId;
        this.expenseCategoryId = expenseCategoryId;
        this.expenseCategoryName = expenseCategoryName;
        this.expenseDate = expenseDate;
        this.expenseName = expenseName;
        this.description = description;
        this.expenseAmount = expenseAmount;
        this.paymentMode = paymentMode;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public String getExpenseCategoryName() {
        return expenseCategoryName;
    }

    public void setExpenseCategoryName(String expenseCategoryName) {
        this.expenseCategoryName = expenseCategoryName;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(float expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.expenseId);
        parcel.writeInt(this.expenseCategoryId);
        parcel.writeString(this.expenseCategoryName);
        parcel.writeString(this.expenseDate);
        parcel.writeString(this.expenseName);
        parcel.writeString(this.description);
        parcel.writeFloat(this.expenseAmount);
        parcel.writeString(this.paymentMode);
    }

    public static Creator<ExpenseClass> CREATOR = new Creator<ExpenseClass>() {
        @Override
        public ExpenseClass createFromParcel(Parcel parcel) {
            ExpenseClass expense = new ExpenseClass();

            expense.expenseId = parcel.readInt();
            expense.expenseCategoryId = parcel.readInt();
            expense.expenseCategoryName=parcel.readString();
            expense.expenseDate = parcel.readString();
            expense.expenseName = parcel.readString();
            expense.description = parcel.readString();
            expense.expenseAmount = parcel.readFloat();
            expense.paymentMode = parcel.readString();

            return expense;
        }

        @Override
        public ExpenseClass[] newArray(int size) {
            return new ExpenseClass[0];
        }
    };
}
