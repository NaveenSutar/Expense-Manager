package com.shrikantbadwaik.casestudy;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import DBAdapter.CategoryImageAdapter;

public class ImageViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    GridView categoryGrid;
    CategoryImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        categoryGrid=(GridView)findViewById(R.id.categoryGrid);
        categoryGrid.setOnItemClickListener(this);

        imageAdapter=new CategoryImageAdapter(this);
        categoryGrid.setAdapter(imageAdapter);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent=new Intent(this,AddNewCategoryActivity.class);

        intent.putExtra("image",position);
        setResult(0,intent);
        finish();
    }
}
