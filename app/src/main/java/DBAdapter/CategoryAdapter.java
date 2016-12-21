package DBAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikantbadwaik.casestudy.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import DBClasses.Category;

/**
 * Created by Deadpool on 5/12/2016.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    Context context;
    ArrayList<Category> categoryList;

    ImageView categoryImage,nextPage;
    TextView categoryText;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        super(context, 0);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) layoutInflater.inflate(R.layout.activity_category_list, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        categoryImage = (ImageView) layout.findViewById(R.id.categoryImage);
        categoryText = (TextView) layout.findViewById(R.id.categoryText);
        nextPage=(ImageView)layout.findViewById(R.id.nextPage);

        Category category = categoryList.get(position);

        categoryText.setText(category.getCategoryTitle());

        try {
            String path = "/sdcard/ExpenseManager/" + category.getImageName();
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            categoryImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return layout;
    }
}
