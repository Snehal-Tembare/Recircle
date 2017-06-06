package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.RentItem;
import com.example.synerzip.recircle_android.models.UserProductUnAvailability;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 26/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RentInfoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "RentInfoActivity";
    public static boolean isDateChanged = false;
    public static boolean isDateEdited = false;
    public String formatedFromDate;
    public String formatedToDate;
    private int discount = 0;
    private int finalTotal = 0;
    private int subTotal;
    private int percentage;
    private int forDays;
    private int protectionPlanFee;
    private int serviceFee;
    public static RentItem mRentItem;

    private String user_id;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.txt_item_title)
    protected TextView mTxtTitle;

    @BindView(R.id.txt_manufaturer_name)
    protected TextView mTxtManufaturerName;

    @BindView(R.id.txt_price)
    protected TextView mTxtPrice;

    @BindView(R.id.txt_days)
    protected TextView mTxtDays;

    @BindView(R.id.txt_from_date)
    protected TextView mTxtFromDate;

    @BindView(R.id.txt_to_date)
    protected TextView mTxtToDate;

    @BindView(R.id.txt_subtotal)
    protected TextView mTxtSubTotal;

    @BindView(R.id.txt_discount)
    protected TextView mTxtDiscounts;

    @BindView(R.id.txt_total)
    protected TextView mTxtTotal;

    @BindView(R.id.txt_owner_name)
    protected TextView mTxtOwnerName;

    @BindView(R.id.txt_selected_dates)
    protected TextView mTxtSelectedDates;

    @BindView(R.id.img_product)
    protected ImageView mImgProduct;

    @BindView(R.id.img_owner)
    protected CircularImageView mImgOwner;

    @BindView(R.id.edt_user_msg)
    protected EditText mEdtUserMsg;

    @BindView(R.id.check_protection_plan)
    protected CheckBox mChckProtectionPlan;

    private Products mProduct;
    private Bundle mBundle;
    private Date fromDate;
    private Date toDate;
    private ArrayList<UserProductUnAvailability> userProductUnAvailabilities;
    private int dayCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_info);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.price_details);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mBundle = getIntent().getExtras();

        if (mBundle != null) {

            mProduct = mBundle.getParcelable(getString(R.string.product));
            userProductUnAvailabilities = mBundle.getParcelableArrayList(getString(R.string.unavail_dates));


            if (mProduct != null) {
                mRentItem = new RentItem();
                mRentItem.setUser_product_id(mProduct.getUser_product_info().getUser_product_id());
                mRentItem.setOrder_from_date(mBundle.getString(getString(R.string.from_date)));
                mRentItem.setOrder_to_date(mBundle.getString(getString(R.string.to_date)));
                if (mProduct.getUser_info().getUser_id() != null) {
                    user_id = mProduct.getUser_info().getUser_id();
                }

                mTxtTitle.setText(mProduct.getProduct_info().getProduct_title());

                mTxtManufaturerName.setText(mProduct.getProduct_info().getProduct_manufacturer_name());
                mTxtPrice.setText("$" + mProduct.getUser_product_info().getPrice_per_day() + "/day");

                Picasso.with(this).load(mProduct.getProduct_info()
                        .getProduct_image_url().getUser_prod_image_url())
                        .into(mImgProduct);
                Picasso.with(this).load(mProduct.getUser_info().getUser_image_url()).into(mImgOwner);

                mTxtOwnerName.setText(mProduct.getUser_info().getFirst_name() + " " + mProduct.getUser_info().getLast_name());
                dayCount = calculateDayCount(mBundle.getString(getString(R.string.from_date)),
                        mBundle.getString(getString(R.string.to_date)));

                mTxtFromDate.setText(formatedFromDate);
                mTxtToDate.setText(formatedToDate);

                subTotal = dayCount * Integer.parseInt(mProduct.getUser_product_info().getPrice_per_day());
                finalTotal = subTotal;
                mTxtDays.setText(String.valueOf(dayCount) + " " + getString(R.string.days));
                mTxtSubTotal.setText("$" + String.valueOf(subTotal));
            }
        }

        //It handles edit dates functionality
        mTxtSelectedDates.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mTxtSelectedDates.getRight() - mTxtSelectedDates.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent = new Intent(RentInfoActivity.this, CalendarActivity.class);
                        intent.putExtra(getString(R.string.from_date), mBundle.getString(getString(R.string.from_date)));
                        intent.putExtra(getString(R.string.to_date), mBundle.getString(getString(R.string.to_date)));
                        intent.putExtra(getString(R.string.selected_dates_list), CalendarActivity.selectedDates);
                        if (userProductUnAvailabilities != null && userProductUnAvailabilities.size() != 0) {
                            intent.putParcelableArrayListExtra(getString(R.string.unavail_dates), userProductUnAvailabilities);
                        }
                        isDateEdited = true;
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                return true;
            }
        });
    }

    /**
     * Calculate selected no of days
     */

    public int calculateDayCount(String from, String to) {

        DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
        try {
            fromDate = formatter.parse(from);
            toDate = formatter.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calFromDate = Calendar.getInstance();
        Calendar calToDate = Calendar.getInstance();
        calFromDate.setTime(fromDate);
        calToDate.setTime(toDate);
        CharSequence monthFromDate = android.text.format.DateFormat
                .format(getString(R.string.month_format), fromDate);
        CharSequence monthToDate = android.text.format.DateFormat
                .format(getString(R.string.month_format), toDate);
        formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate + ", " + calFromDate.get(Calendar.YEAR);
        formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);
        long diff = toDate.getTime() - fromDate.getTime();
        dayCount = (int) diff / (24 * 60 * 60 * 1000);

        return dayCount;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String from = data.getStringExtra(getString(R.string.from_date));
                String to = data.getStringExtra(getString(R.string.to_date));

                dayCount = calculateDayCount(from, to);

                subTotal = Math.abs(dayCount) * Integer.parseInt(mProduct.getUser_product_info().getPrice_per_day());
                finalTotal = subTotal;
                discount = 0;

                mTxtFromDate.setText(formatedFromDate);
                mTxtToDate.setText(formatedToDate);
                mTxtDays.setText(String.valueOf(dayCount) + " " + getString(R.string.days));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mProduct != null) {
            serviceFee= (int) (subTotal*(0.8));
            protectionPlanFee= (int) (Integer.parseInt(mProduct.getUser_product_info().getPrice_per_day()) *0.1);

            //Calculate discount
            if (mProduct.getUser_product_info().getUser_product_discounts() != null
                    && mProduct.getUser_product_info().getUser_product_discounts().size() > 0) {

                for (int i = 0; i < mProduct.getUser_product_info().getUser_product_discounts().size(); i++) {
                    if (mProduct.getUser_product_info().getUser_product_discounts().get(i).getIsActive() != null
                            && dayCount >= mProduct.getUser_product_info().getUser_product_discounts().get(i).getDiscount_for_days()
                            && !mProduct.getUser_product_info().getUser_product_discounts().get(i).getIsActive()) {
                        discount = subTotal * mProduct.getUser_product_info().getUser_product_discounts().get(i).getPercentage() / 100;
                        finalTotal = subTotal - discount;
                        percentage = mProduct.getUser_product_info().getUser_product_discounts().get(i).getPercentage();
                        forDays = mProduct.getUser_product_info().getUser_product_discounts().get(i).getDiscount_for_days();
                    }
                }

                if (discount == 0) {
                    mTxtDiscounts.setText("$0.0");
                    mTxtSubTotal.setText("$" + String.valueOf(subTotal));
                    mTxtTotal.setText("$" + String.valueOf(finalTotal));
                } else {
                    mTxtDiscounts.setText("$" + discount + " (" + percentage + "% for " + forDays + " " + getString(R.string.days) + ")");
                    mTxtSubTotal.setText("$" + String.valueOf(subTotal));
                    mTxtTotal.setText("$" + String.valueOf(finalTotal));
                }
            } else {
                mTxtDiscounts.setText("$0.0");
                mTxtTotal.setText("$" + String.valueOf(subTotal));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailsActivity.isShowInfo = true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * Show payment mode screen
     */

    @OnClick(R.id.btn_proceed_to_pay)
    public void showPaymentModes() {

        mRentItem.setUser_msg(mEdtUserMsg.getText().toString());

        mRentItem.setService_fee(serviceFee);
        mRentItem.setFinal_payment(finalTotal);

        if (mChckProtectionPlan.isChecked()) {
            mRentItem.setProtection_plan(1);
            mRentItem.setProtection_plan_fee(protectionPlanFee);
        } else {
            mRentItem.setProtection_plan(0);
            mRentItem.setProtection_plan_fee(0);
        }

        Intent intentPayMode = new Intent(this, PaymentModeActivity.class);
        intentPayMode.putExtra(getString(R.string.total), finalTotal);
        intentPayMode.putExtra(getString(R.string.user_id), user_id);
        startActivity(intentPayMode);
    }
}
