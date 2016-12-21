package DBAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikantbadwaik.casestudy.R;

import java.util.ArrayList;

import DBClasses.Category;
import DBClasses.ExpenseClass;
import DBHelper.Helper;

/**
 * Created by Deadpool on 5/22/2016.
 */
public class ExpenseAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<ExpenseClass> arrayList;

    TextView itemName, itemCategory, itemAmount,itemPayMode,itemDate;

    public ExpenseAdapter(Context context, ArrayList<ExpenseClass> arrayList) {
        super(context, 0);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) layoutInflater.inflate(R.layout.activity_expense_list, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        itemName = (TextView) layout.findViewById(R.id.textItemName);
        itemName.setTextColor(Color.parseColor("#136cb2"));

        itemCategory = (TextView) layout.findViewById(R.id.textItemCategory);
        itemCategory.setTextColor(Color.parseColor("#136cb2"));

        itemAmount = (TextView) layout.findViewById(R.id.textItemAmount);
        itemAmount.setTextColor(Color.parseColor("#136cb2"));

        itemPayMode=(TextView)layout.findViewById(R.id.textItemPayMode);
        itemPayMode.setTextColor(Color.parseColor("#136cb2"));

        itemDate=(TextView)layout.findViewById(R.id.textItemDate);
        itemDate.setTextColor(Color.parseColor("#136cb2"));

        ExpenseClass expense=arrayList.get(position);

        itemName.setText(expense.getExpenseName());
        itemCategory.setText(expense.getExpenseCategoryName());
        itemAmount.setText(""+expense.getExpenseAmount());
        itemPayMode.setText(expense.getPaymentMode());
        itemDate.setText(expense.getExpenseDate());

        if ((position % 2) == 0) {
            itemName.setBackgroundColor(Color.parseColor("#f6f6f5"));
            itemCategory.setBackgroundColor(Color.parseColor("#f6f6f5"));
            itemAmount.setBackgroundColor(Color.parseColor("#f6f6f5"));
            itemPayMode.setBackgroundColor(Color.parseColor("#f6f6f5"));
            itemDate.setBackgroundColor(Color.parseColor("#f6f6f5"));
        } else {
            itemName.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemCategory.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemAmount.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemPayMode.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemDate.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }

        return layout;
    }
}
