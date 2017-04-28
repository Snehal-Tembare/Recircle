package com.example.synerzip.recircle_android.ui;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 28/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class PaymentModeActivity extends AppCompatActivity {

    private BottomSheetBehavior sheetBehavior;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.btn_total_price)
    protected Button mBtnTotalPrice;

    @BindView(R.id.bottom_sheet_layout)
    protected LinearLayout mBottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.select_payment_mode));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        Bundle bundle = getIntent().getExtras();

        mBtnTotalPrice.setText("$" + String.valueOf(bundle.getInt(getString(R.string.total))));

//        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @OnClick(R.id.btn_total_price)
    public void showInfo() {
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
