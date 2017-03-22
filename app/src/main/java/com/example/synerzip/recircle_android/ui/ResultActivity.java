package com.example.synerzip.recircle_android.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.synerzip.recircle_android.R;

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

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.layout_expanded)
    public LinearLayout mExpandLayout;

    @BindView(R.id.layout_header)
    public LinearLayout mHeaderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));



//        Picasso.with(this)
//                .load("https://s3.ap-south-1.amazonaws.com/recircleimages/1398934894000_IMG_388284.jpg")
//                .into(mImgView);


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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
