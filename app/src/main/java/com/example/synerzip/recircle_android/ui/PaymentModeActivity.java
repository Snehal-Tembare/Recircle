package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
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

    private Bundle mBundle;
    private String user_id;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.btn_total_price)
    protected Button mBtnTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        ButterKnife.bind(this);

        init();
    }

    /**
     * Initialize views
     */

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.select_payment_mode));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            mBtnTotalPrice.setText("$" + String.valueOf(mBundle.getInt(getString(R.string.total))));
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

    /**
     * Show credit card Info
     */

    @OnClick(R.id.layout_cc)
    public void showCreditCardDetails() {
        Intent ccIntent = new Intent(PaymentModeActivity.this, CreditCardActivity.class);
        ccIntent.putExtra(getString(R.string.total), mBundle.getInt(getString(R.string.total)));
        ccIntent.putExtra(getString(R.string.user_id), mBundle.getString(getString(R.string.user_id)));
        startActivity(ccIntent);
    }

    /**
     * Show debit card Info
     */

    @OnClick(R.id.layout_dc)
    public void showDebitCardDetails() {
        Intent ccIntent = new Intent(PaymentModeActivity.this, CreditCardActivity.class);
        ccIntent.putExtra(getString(R.string.total), mBundle.getInt(getString(R.string.total)));
        ccIntent.putExtra(getString(R.string.user_id), mBundle.getInt(getString(R.string.user_id)));
        startActivity(ccIntent);
    }
}
