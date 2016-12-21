package DBAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikantbadwaik.casestudy.R;

/**
 * Created by Deadpool on 5/13/2016.
 */

public class CategoryImageAdapter extends BaseAdapter
{
    Context context;
    ImageView imageView;
    TextView categoryLabel;

    public Integer[] categoryImage = {
            R.drawable.any,R.drawable.cart,R.drawable.cloths,
            R.drawable.computor,R.drawable.enter,R.drawable.food,
            R.drawable.medi,R.drawable.mobile, R.drawable.ticket
    };

    public String[] categoryTitle = {
            "Extras", "Grocery", "Cloths",
            "Appliances", "Entertainment", "Food",
            "Medicine", "Recharge", "Traveling"
    };

    public CategoryImageAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public int getCount()
    {
        return categoryImage.length;
    }

    @Override
    public Object getItem(int position) {
        return categoryImage[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LinearLayout layout = null;
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) layoutInflater.inflate(R.layout.activity_category_image, null);
        }
        else
        {
            layout=(LinearLayout)convertView;
        }

        imageView=(ImageView)layout.findViewById(R.id.images);

        imageView.setImageResource(categoryImage[position]);
        imageView.setPadding(10, 10, 10, 10);

        categoryLabel=(TextView)layout.findViewById(R.id.categoryLabel);
        categoryLabel.setText(categoryTitle[position]);

        return layout;
    }
}
