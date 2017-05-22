package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.synerzip.recircle_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.btn_edit_profile)
    public void btnEditProfile(View view) {
        startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
    }

    @OnClick(R.id.card_payments)
    public void cardPayments(View view) {
        startActivity(new Intent(SettingsActivity.this, PaymentsActivity.class));
    }

    @OnClick(R.id.card_bank_acc)
    public void cardBankAcc(View view) {
        startActivity(new Intent(SettingsActivity.this, BankAccActivity.class));
    }

    /**
     * action bar back button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
