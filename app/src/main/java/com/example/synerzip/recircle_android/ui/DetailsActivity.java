package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 31/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class DetailsActivity extends AppCompatActivity {

    private boolean isExpandable = true;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.expand_txt_description)
    public ExpandableTextView mTxtDecscriptionDetail;

    @BindView(R.id.txt_desc_see_more)
    public TextView mTxtDescSeeMore;

    @BindView(R.id.expand_txt_condition)
    public ExpandableTextView mTxtConditionDetail;

    @BindView(R.id.txt_condition_see_more)
    public TextView mTxtConditionSeeMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.recircle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
;

//        mTxtDecscriptionDetail.setExpandInterpolator(new OvershootInterpolator());
//        mTxtDecscriptionDetail.setCollapseInterpolator(new OvershootInterpolator());
//        mTxtConditionDetail.setExpandInterpolator(new OvershootInterpolator());
//        mTxtConditionDetail.setCollapseInterpolator(new OvershootInterpolator());

        mTxtDescSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtDecscriptionDetail.toggle();
                mTxtDescSeeMore.setText(mTxtDecscriptionDetail.isExpanded() ? R.string.see_more : R.string.view_less);
            }
        });

        mTxtConditionSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtConditionDetail.toggle();
                mTxtConditionSeeMore.setText(mTxtConditionDetail.isExpanded() ? R.string.see_more : R.string.view_less);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
