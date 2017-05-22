package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.example.synerzip.recircle_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 19/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class AllRequestsActivity extends AppCompatActivity {
//    @BindView(R.id.toolbar)
//    protected Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_pending_request);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
//    setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getString(R.string.pending_requests));
//        mToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.common_white));


    }
}
