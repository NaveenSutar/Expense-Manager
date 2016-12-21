package com.shrikantbadwaik.casestudy;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import DBAdapter.CategoryImageAdapter;
import DBClasses.Category;
import DBHelper.Helper;
import DBUtility.Constants;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddNewCategoryActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    int position;
    ImageView addCategoryImage;
    CategoryImageAdapter imageAdapter;
    EditText categoryName;
    Bitmap selectImage;
    int getId;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        categoryName = (EditText) findViewById(R.id.categoryName);
        addCategoryImage = (ImageView) findViewById(R.id.addCategoryImage);

        addCategoryImage.setImageResource(0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        categoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(categoryName.getWindowToken(), 0);
                }
            }
        });

    }

    public void selectCategory(View view) {
        Intent intent = new Intent(this, ImageViewActivity.class);
        startActivityForResult(intent, 0);
    }

    public void save(View view) {
        if (selectImage == null) {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
        } else if (categoryName.getText().toString().length() == 0) {
            Toast.makeText(this, "Please Enter Category Title", Toast.LENGTH_SHORT).show();
        } else {
            if (checkPermission()) {
                createAndStoreInDirectory();
            } else if (!checkPermission()) {
                requestPermission();
            } else {
                createAndStoreInDirectory();
            }
        }
    }

    public void createAndStoreInDirectory() {
        String fileName = UUID.randomUUID().toString() + ".png";
        try {
            File makeNewDirectory = new File("/sdcard/ExpenseManager");
            if (!makeNewDirectory.exists()) {
                makeNewDirectory.mkdir();
            }

            String file = "/sdcard/ExpenseManager/" + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            selectImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        saveInExternalCard();

        Toast.makeText(this, "Category Added Successfully!!!", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void saveInExternalCard() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int loginId = preferences.getInt(Constants.LOGIN_ID, 0);

        Helper helper = new Helper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Category.COL_CATEGORY_TITLE, categoryName.getText().toString());
        values.put(Category.COL_IMAGE_NAME, fileName);
        values.put(Category.COL_CATEGORY_USER_ID, loginId);

        db.insert(Category.TABLE_CATEGORY, null, values);

        db.close();
    }

    public void clear(View view) {
        categoryName.setText("");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            position = data.getExtras().getInt("image");

            imageAdapter = new CategoryImageAdapter(this);

            addCategoryImage.setImageResource(imageAdapter.categoryImage[position]);
            addCategoryImage.buildDrawingCache();
            selectImage = addCategoryImage.getDrawingCache();

            addCategoryImage.setImageBitmap(selectImage);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeExternalStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writeExternalStorageAccepted == true) {
                        createAndStoreInDirectory(); //next page
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow this permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        }
                                );
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCategoryActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", okListener);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        saveInExternalCard();
                        finish();
                    }
                }
        );
        builder.create().show();
    }
}
