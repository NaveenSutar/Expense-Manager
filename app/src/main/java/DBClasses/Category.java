package DBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import DBHelper.Helper;

/**
 * Created by Deadpool on 5/12/2016.
 */

public class Category implements Parcelable {
    public static final String TABLE_CATEGORY = "categories";
    public static final String COL_CATEGORY_ID = "categoryid";
    public static final String COL_CATEGORY_TITLE = "categorytitle";
    public static final String COL_IMAGE_NAME = "imagename";
    public static final String COL_CATEGORY_USER_ID = "categoryuserid";

    private int categoryId;
    private int categoryUserId;
    private String categoryTitle;
    private String imageName;

    public Category() {
    }

    public Category(int categoryId, int categoryUserId, String categoryTitle, String imageName) {
        this.categoryId = categoryId;
        this.categoryUserId = categoryUserId;
        this.categoryTitle = categoryTitle;
        this.imageName = imageName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryUserId() {
        return categoryUserId;
    }

    public void setCategoryUserId(int categoryUserId) {
        this.categoryUserId = categoryUserId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return this.categoryTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.categoryId);
        parcel.writeString(this.categoryTitle);
        parcel.writeString(this.imageName);
    }

    public static Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel parcel) {
            Category category=new Category();

            category.categoryId=parcel.readInt();
            category.categoryTitle=parcel.readString();
            category.imageName=parcel.readString();

            return category;
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[0];
        }
    };
}
