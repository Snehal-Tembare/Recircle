package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.UserProdImages;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    private Products product;
    private ArrayList<UserProdImages> listUploadItemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.list_an_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        selectedDates = new ArrayList<>();
        mItemAvailability = new ArrayList<>();

        if (MyProfileActivity.isItemEdit) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                product = bundle.getParcelable(getString(R.string.product));
                if (product != null) {
                    mEditTxtItemDesc.setText(product.getUser_product_info().getUser_prod_desc());
                    mEditTxtZipcode.setText(product.getUser_product_info().getUser_product_zipcode());

                    ArrayList<UserProductUnAvailability> unavailableDates;
                    unavailableDates = product.getUser_product_info().getUser_prod_unavailability();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.ddd_mm));

                    for (UserProductUnAvailability date : unavailableDates) {
                        try {
                            selectedDates.add(simpleDateFormat.parse(date.getUnavai_from_date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    daysCount = unavailableDates.size();
                    if (daysCount != 0) {
                        mTxtDaysOfAvailability.setVisibility(View.VISIBLE);
                        mTxtDaysOfAvailability.setText(getString(R.string.calendar_days_selected) + " " +
                                daysCount + " " + getString(R.string.calendar_days));
                    }

                }
            }
        }

    }

    /**
     * check whether zipcode valid or not
     */
    private void checkZipcodes() {
        if (mZipcode != 0) {
            if (ApiClient.getClient(this)!=null){
            service = ApiClient.getClient(this).create(RCAPInterface.class);
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
                            intent.putExtra(getString(R.string.product), product);
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
        }else {

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
        if (MyProfileActivity.isItemEdit) {
            intent.putExtra(getString(R.string.dates), selectedDates);

        } else {
            intent.putExtra(getString(R.string.dates), selectedDates);

        }
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
                selectedDates.clear();
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
    public void btnNext(View view) {
        if (getValues()) {
            checkZipcodes();


            UserProdImages mUserProdImages;

            mUserProdImages = new UserProdImages("https://s3.ap-south-1.amazonaws.com/recircleimages/1398934243000_1047081.jpg",
                    "2017-02-04T13:13:09.000Z");
            listUploadItemImage = new ArrayList<>();
            listUploadItemImage.add(mUserProdImages);
            if (MyProfileActivity.isItemEdit){
                ListItemFragment.editProduct.setUser_prod_desc(mEditTxtItemDesc.getText().toString());
                ListItemFragment.editProduct.setUser_prod_unavailability(mItemAvailability);
                ListItemFragment.editProduct.setUser_product_zipcode(mEditTxtZipcode.getText().toString());
            ListItemFragment.editProduct.setUser_prod_images(listUploadItemImage);}

        } else {
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
