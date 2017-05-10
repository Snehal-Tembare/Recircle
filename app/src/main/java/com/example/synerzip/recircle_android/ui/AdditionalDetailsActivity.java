package com.example.synerzip.recircle_android.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Discounts;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by Prajakta Patil on 9/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class AdditionalDetailsActivity extends AppCompatActivity {

    private ArrayList<UserProdImages> listUploadItemImage;

    private ArrayList<String> uploadGalleryImages;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    public static ArrayList<Date> selectedDates;

    public UserProductUnAvailability userProductUnAvailability;

    public static ArrayList<UserProductUnAvailability> mItemAvailability;

    public static int daysCount;

    @BindView(R.id.txt_days_for_availability)
    protected TextView mTxtDaysOfAvailability;

    public static long mZipcode;

    public static String mItemDesc;

    @BindView(R.id.edit_item_desc)
    protected EditText mEditTxtItemDesc;

    private RCAPInterface service;

    private int sizeZipcodeList;

    public static int fromAustin;

    @BindView(R.id.checkbox_agreement)
    protected CheckBox mCheckboxAgreement;

    @BindView(R.id.edit_enter_zip)
    protected EditText mEditTxtZipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        uploadGalleryImages = new ArrayList<>();
        selectedDates = new ArrayList<>();
        mItemAvailability = new ArrayList<>();

    }
    /**
     * check whether zipcode valid or not
     */
    private void checkZipcodes() {
        if (mZipcode != 0) {
            service = ApiClient.getClient().create(RCAPInterface.class);
            Call<ZipcodeRoot> call = service.zipcodeCheck(mZipcode);
            call.enqueue(new Callback<ZipcodeRoot>() {
                @Override
                public void onResponse(Call<ZipcodeRoot> call, Response<ZipcodeRoot> response) {
                    if (response.isSuccessful()) {
                        response.body();
                        sizeZipcodeList = response.body().getResults().size();
                        String[] stringArrayZipcodes = getResources().getStringArray(R.array.zipcodes_list_austin);
                        ArrayList<String> listAustinZipcodes = new ArrayList<>();
                        listAustinZipcodes.addAll(Arrays.asList(stringArrayZipcodes));
                        String zipcode = String.valueOf(mZipcode);
                        if (sizeZipcodeList != 0) {
                            if (listAustinZipcodes.contains(zipcode)) {
                                fromAustin = 1;
                            } else {
                                if (listAustinZipcodes.contains(zipcode)) {
                                    fromAustin = 0;
                                }
                            }
                        } else {
                            if (sizeZipcodeList == 0) {
                                RCLog.showToast(AdditionalDetailsActivity.this, getString(R.string.invalid_zipcode));
                                return;
                            }
                        }
                        if (mCheckboxAgreement.isChecked()) {

                            Intent intent = new Intent(AdditionalDetailsActivity.this, ListItemSummaryActivity.class);
                            startActivity(intent);


                        } else {
                            RCLog.showToast(AdditionalDetailsActivity.this, getString(R.string.terms_and_agreement));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ZipcodeRoot> call, Throwable t) {
                }
            });
        }
        return;
    }

    /**
     * get values
     */
    public boolean getValues() {
        String strDesc = mEditTxtItemDesc.getText().toString();
        String strZipcode = mEditTxtZipcode.getText().toString();
        if (!strDesc.isEmpty() && !strZipcode.isEmpty()) {
            mItemDesc = mEditTxtItemDesc.getText().toString().trim();
            mZipcode = Long.parseLong(mEditTxtZipcode.getText().toString().trim());

            return true;
        }
        return false;
    }
    /**
     * enter calendar unavailability
     *
     * @param view
     */
    @OnClick(R.id.edit_calendar_availability)
    public void calendarAvailability(View view) {
        Intent intent = new Intent(AdditionalDetailsActivity.this, ListItemCalendarActivity.class);
        intent.putExtra(getString(R.string.dates), selectedDates);
        startActivityForResult(intent, 1);
    }

    /**
     * get calendar availability dates
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> availableDates;
                availableDates = data.getStringArrayListExtra(getString(R.string.calendar_availability_days));
                selectedDates = (ArrayList<Date>) data.getSerializableExtra(getString(R.string.selectedDates));
                for (String date : availableDates) {
                    userProductUnAvailability = new UserProductUnAvailability(date, date);
                    mItemAvailability.add(userProductUnAvailability);
                }
                daysCount = data.getIntExtra(getString(R.string.calendar_availability_days_count), 0);
                if (daysCount != 0) {
                    mTxtDaysOfAvailability.setVisibility(View.VISIBLE);
                    mTxtDaysOfAvailability.setText(getString(R.string.calendar_days_selected) + " " +
                            daysCount + " " + getString(R.string.calendar_days));
                }
            }
        }
    }

    @OnClick(R.id.btn_next)
    public void btnNext(View view){
        
        if(getValues()) {
          checkZipcodes();
        }else {
            RCLog.showToast(AdditionalDetailsActivity.this, getString(R.string.mandatory_dates));
        }
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
