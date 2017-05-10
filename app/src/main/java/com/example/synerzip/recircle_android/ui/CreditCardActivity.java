package com.example.synerzip.recircle_android.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.craftman.cardform.CardForm;
import com.example.synerzip.recircle_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Snehal Tembare on 3/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class CreditCardActivity extends AppCompatActivity {

    private Bundle mBundle;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.credit_card_details));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.common_white));

        mBundle=getIntent().getExtras();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
