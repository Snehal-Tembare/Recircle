package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserProdImages;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 11/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */


public class ZoomActivity extends AppCompatActivity {

    private ArrayList<UserProdImages> userProdImagesArrayList;
    private Bundle bundle;

    private PagerAdapter mPagerAdapter;

    private int selectedImgPosition;

    public ImageAdapter mImageAdapter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.view_pager)
    public ViewPager mViewPager;

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        bundle = getIntent().getExtras();
        if (bundle != null) {
            userProdImagesArrayList = bundle.getParcelableArrayList(getString(R.string.image_urls_array));
            selectedImgPosition = bundle.getInt(getString(R.string.selected_image_position), 0);
            if (userProdImagesArrayList != null && userProdImagesArrayList.size() != 0) {
                mPagerAdapter = new CustomPagerAdapter(getApplicationContext(), userProdImagesArrayList);
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setCurrentItem(selectedImgPosition);

                mImageAdapter = new ImageAdapter(getApplicationContext(), selectedImgPosition, userProdImagesArrayList, new ImageAdapter.OnImageItemClickListener() {
                    @Override
                    public void onImageClick(int position, UserProdImages userProdImages) {

                        mViewPager.setCurrentItem(position);

                        View view = mRecyclerView.getChildAt(position);
                        if (view != null) {
                            view.setBackground(ContextCompat.getDrawable(ZoomActivity.this, R.drawable.selected_image_background));
                        }

                        for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                            view = mRecyclerView.getChildAt(i);
                            if (i != position) {
                                if (view != null) {
                                    view.setBackground(ContextCompat.getDrawable(ZoomActivity.this, R.drawable.custom_imageview));
                                }
                            }
                        }
                    }
                });

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setAdapter(mImageAdapter);

                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        View view = mRecyclerView.getChildAt(position);

                        if (view != null) {
                            view.setBackground(ContextCompat.getDrawable(ZoomActivity.this, R.drawable.selected_image_background));
                        }

                        for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                            view = mRecyclerView.getChildAt(i);
                            if (view != null) {
                                if (i != mViewPager.getCurrentItem()) {
                                    view.setBackground(ContextCompat.getDrawable(ZoomActivity.this, R.drawable.custom_imageview));
                                }
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }

    }

    /**
     * Show previous Image
     */

    @OnClick(R.id.img_previous)
    public void showPreviousImage() {
        if (mViewPager.getCurrentItem() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);

            int position = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);

            View view = mRecyclerView.getChildAt(mViewPager.getCurrentItem());
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_image_background));

            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                view = mRecyclerView.getChildAt(i);
                if (i == position) {
                    view.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_imageview));
                }
            }
        }
    }

    /**
     * Show next Image
     */

    @OnClick(R.id.img_next)
    public void showNextImage() {
        if (mViewPager.getCurrentItem() < mViewPager.getAdapter().getCount() - 1) {
            mRecyclerView.setVisibility(View.VISIBLE);
            int position = mViewPager.getCurrentItem();

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

            View view = mRecyclerView.getChildAt(mViewPager.getCurrentItem());
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_image_background));

            for (int i = 0; i < userProdImagesArrayList.size(); i++) {
                view = mRecyclerView.getChildAt(i);
                if (i == position) {
                    view.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_imageview));
                }
            }
        }
    }


    /**
     * OnClick of home button
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
