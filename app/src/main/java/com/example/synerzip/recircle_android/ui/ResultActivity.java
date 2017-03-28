package com.example.synerzip.recircle_android.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.SearchProduct;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 20/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class ResultActivity extends AppCompatActivity {
//    @BindView(R.id.img)
//    public ImageView mImgView;

    private String productId = "";
    private String manufacturerId = "";
    private String query = "";
    private String mFromDate = "";
    private String mToDate = "";
    private Bundle mIntent;

    private String mName;
    private String mPlace;
    private String mStartDate;
    private String mEndDate;

    private ArrayList<Products> productsArrayList;
    private SearchAdapter mSearchAdapter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.layout_expanded)
    public LinearLayout mExpandLayout;

    @BindView(R.id.layout_header)
    public LinearLayout mHeaderLayout;

    @BindView(R.id.lv_searched_items)
    public RecyclerView mLvSearcedItems;

    @BindView(R.id.txt_name)
    public TextView mTxtName;

    @BindView(R.id.txt_place)
    public TextView mTxtPlace;

    @BindView(R.id.txt_start_date)
    public TextView mTxtStartDate;

    @BindView(R.id.txt_end_date)
    public TextView mTxtEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mIntent = getIntent().getExtras();
        Log.v("Intent", mIntent.getParcelable("searchProduct").toString());
        SearchProduct sd = mIntent.getParcelable("searchProduct");
        productsArrayList = sd.getProducts();

        mName = mIntent.getString("name", "");
        mPlace = mIntent.getString("place", "");
        mStartDate = mIntent.getString("startDate", "");
        mEndDate = mIntent.getString("endDate", "");

        mTxtName.setText(mName);
        mTxtPlace.setText(mPlace);
        mTxtStartDate.setText(mStartDate);
        mTxtEndDate.setText(mEndDate);

        mSearchAdapter=new SearchAdapter(this,productsArrayList);
        mLvSearcedItems.setLayoutManager(new LinearLayoutManager(this));
        mLvSearcedItems.setAdapter(mSearchAdapter);
    }

    @OnClick(R.id.layout_header)
    public void expandView() {
        mExpandLayout.setVisibility(View.VISIBLE);
        mHeaderLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_search)
    public void collapse() {
        mExpandLayout.setVisibility(View.GONE);
        mHeaderLayout.setVisibility(View.VISIBLE);

        //TODO code yet to complete

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
