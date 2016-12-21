package DBFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shrikantbadwaik.casestudy.ListCategoryActivity;
import com.shrikantbadwaik.casestudy.ListDateActivity;
import com.shrikantbadwaik.casestudy.ListExpandableActivity;
import com.shrikantbadwaik.casestudy.ListPaymodeActivity;
import com.shrikantbadwaik.casestudy.R;

import java.util.ArrayList;

import DBClasses.ExpenseClass;
import DBHelper.Helper;
import DBUtility.Constants;

public class ListAllFragment extends Fragment implements View.OnClickListener {

    Button btnListAll, btnListByCategory, btnListByPaymode, btnListByDate;
    ArrayList<ExpenseClass> arrayList = new ArrayList<>();

    public ListAllFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_all, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnListAll = (Button) view.findViewById(R.id.btnListAll);
        btnListAll.setOnClickListener(this);

        btnListByCategory = (Button) view.findViewById(R.id.btnListByCategory);
        btnListByCategory.setOnClickListener(this);

        btnListByPaymode = (Button) view.findViewById(R.id.btnListByPaymode);
        btnListByPaymode.setOnClickListener(this);

        btnListByDate = (Button) view.findViewById(R.id.btnListByDate);
        btnListByDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Helper helper = new Helper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int userId = preferences.getInt(Constants.LOGIN_ID, 0);

        String query = "SELECT * FROM " + ExpenseClass.TABLE_EXPENSE + " where " + ExpenseClass.COL_EXPENSE_USER_ID + " = " + userId + " ;";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.isAfterLast()) {
            if (cursor != null && cursor.getCount() == 0) {
                Toast.makeText(getContext(), "Please Add Expense Information", Toast.LENGTH_SHORT).show();
            }
        } else if (view == btnListAll) {
            startActivity(new Intent(getContext(), ListExpandableActivity.class));
        } else if (view == btnListByCategory) {
            startActivity(new Intent(getContext(), ListCategoryActivity.class));
        } else if (view == btnListByDate) {
            startActivity(new Intent(getContext(), ListDateActivity.class));
        } else if (view == btnListByPaymode) {
            startActivity(new Intent(getContext(), ListPaymodeActivity.class));
        }
        cursor.close();
        db.close();
    }
}


