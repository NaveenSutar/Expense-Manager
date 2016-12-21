package DBAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikantbadwaik.casestudy.R;

import java.util.ArrayList;

import DBClasses.ExpenseClass;

/**
 * Created by Deadpool on 02/06/2016.
 */
public class ReportAdapter extends ArrayAdapter<ExpenseClass>
{
    Context context;
    ArrayList<ExpenseClass> arrayList;
    TextView itemDate, itemName, itemDescription, itemAmount;

    public ReportAdapter(Context context, ArrayList<ExpenseClass> arrayList) {
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
            layout = (LinearLayout) layoutInflater.inflate(R.layout.activity_report_list, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        itemDate=(TextView)layout.findViewById(R.id.textExpDate);
        itemDate.setTextColor(Color.parseColor("#136cb2"));

        itemName = (TextView) layout.findViewById(R.id.textExpName);
        itemName.setTextColor(Color.parseColor("#136cb2"));

        itemDescription=(TextView)layout.findViewById(R.id.textExpDescription);
        itemDescription.setTextColor(Color.parseColor("#136cb2"));

        itemAmount = (TextView) layout.findViewById(R.id.textExpAmount);
        itemAmount.setTextColor(Color.parseColor("#136cb2"));

        ExpenseClass expense=arrayList.get(position);

        itemDate.setText(expense.getExpenseDate());
        itemName.setText(expense.getExpenseName());
        itemDescription.setText(expense.getDescription());
        itemAmount.setText("" + expense.getExpenseAmount());

        if ((position % 2) == 0) {
            itemDate.setBackgroundColor(Color.parseColor("#BEE9F7"));
            itemName.setBackgroundColor(Color.parseColor("#BEE9F7"));
            itemDescription.setBackgroundColor(Color.parseColor("#BEE9F7"));
            itemAmount.setBackgroundColor(Color.parseColor("#BEE9F7"));
        } else {
            itemDate.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemName.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemDescription.setBackgroundColor(Color.parseColor("#fbfbfb"));
            itemAmount.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }

        return layout;
    }
}
