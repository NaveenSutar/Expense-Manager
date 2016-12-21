package DBFragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shrikantbadwaik.casestudy.AddNewExpenseActivity;
import com.shrikantbadwaik.casestudy.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import DBAdapter.CategoryAdapter;
import DBClasses.Category;
import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class AddExpenseFragment extends Fragment implements AdapterView.OnItemClickListener {

    TextView textInformation;
    ListView categoryListView;
    ArrayList<Category> categoryList = new ArrayList<>();
    CategoryAdapter adapter;

    public AddExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textInformation = (TextView) view.findViewById(R.id.textInformation);

        categoryListView = (ListView) view.findViewById(R.id.categoryListView);
        categoryListView.setOnItemClickListener(this);

        adapter = new CategoryAdapter(getContext(), categoryList);
        categoryListView.setAdapter(adapter);

        registerForContextMenu(categoryListView);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.expenseanim_1);
        textInformation.setAnimation(animation);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Category category = categoryList.get(position);

        Intent intent = new Intent(getContext(), AddNewExpenseActivity.class);
        intent.putExtra("Category", category);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh();
    }

    public void refresh() {
        categoryList.clear();

        Helper helper = new Helper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int loginId = preferences.getInt(Constants.LOGIN_ID, 0);

        Cursor cursor = db.rawQuery("SELECT * FROM " + Category.TABLE_CATEGORY + " WHERE " + Category.COL_CATEGORY_USER_ID + " = " + loginId + " ;", null);

        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                Category category = new Category();

                category.setCategoryId(cursor.getInt(cursor.getColumnIndex(Category.COL_CATEGORY_ID)));

                category.setCategoryUserId(cursor.getInt(cursor.getColumnIndex(Category.COL_CATEGORY_USER_ID)));
                category.setCategoryTitle(cursor.getString(cursor.getColumnIndex(Category.COL_CATEGORY_TITLE)));
                category.setImageName(cursor.getString(cursor.getColumnIndex(Category.COL_IMAGE_NAME)));

                categoryList.add(category);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Options");
        menu.add("Edit");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Category category = categoryList.get(info.position);

        if (item.getTitle().equals("Delete")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("confirmation");
            builder.setMessage("Are you sure you want to delete this record?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Helper helper = new Helper(getContext());

                    SQLiteDatabase db = helper.getWritableDatabase();

                    String query = "DELETE FROM " + Category.TABLE_CATEGORY + " WHERE " + Category.COL_CATEGORY_ID + " = " + category.getCategoryId();
                    db.execSQL(query);


                    db.close();
                    //dbt.close();

                    refresh();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();

        } else if (item.getTitle().equals("Edit")) {
            final EditText editCategory = new EditText(getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setView(editCategory);
            builder.setTitle("edit");
            builder.setMessage("Enter Category Title : ");

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Helper helper = new Helper(getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();

                    String query = "Update " + Category.TABLE_CATEGORY + " set " + Category.COL_CATEGORY_TITLE + " = '" + editCategory.getText().toString() + "' Where " + Category.COL_CATEGORY_ID + " = " + category.getCategoryId();
                    db.execSQL(query);

                    db.close();

                    refresh();
                }
            });

            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }
        return super.onContextItemSelected(item);

    }
}
