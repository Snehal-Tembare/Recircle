package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdReview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 19/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AllReviewsActivity extends AppCompatActivity {

    private ArrayList<UserProdReview> userProdReviewArrayList;
    private AllReviewsListAdapter mAdapter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.recyclerview)
    public RecyclerView mRecyleAllReviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.all_reviews));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        Bundle bundle = getIntent().getExtras();
        userProdReviewArrayList = bundle.getParcelableArrayList(getString(R.string.all_reviews_list));

        mAdapter = new AllReviewsListAdapter(getApplicationContext(), userProdReviewArrayList);

        mRecyleAllReviewsList.setLayoutManager(new LinearLayoutManager(this));
        mRecyleAllReviewsList.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
