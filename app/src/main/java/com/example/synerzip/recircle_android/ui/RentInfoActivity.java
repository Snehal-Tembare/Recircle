package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.Products;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static boolean isDateEdited=false;

    private Products mProduct;
    private Bundle mBundle;

    private Date fromDate;
    private Date toDate;

    public static String formatedFromDate;
    public static String formatedToDate;

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

//    @BindView(R.id.btn_total_price)
//    protected TextView mBtnTotalPrice;

    @BindView(R.id.txt_selected_dates)
    protected TextView mTxtSelectedDates;

    @BindView(R.id.img_product)
    protected ImageView mImgProduct;

    @BindView(R.id.img_owner)
    protected CircularImageView mImgOwner;


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

        mProduct = mBundle.getParcelable(getString(R.string.product));

        mTxtTitle.setText(mProduct.getProduct_info().getProduct_title());

        mTxtManufaturerName.setText(mProduct.getProduct_info().getProduct_manufacturer_name());
        mTxtPrice.setText("$" + mProduct.getUser_product_info().getPrice_per_day() + "/day");

        Picasso.with(this).load(mProduct.getProduct_info().getProduct_image_url()).into(mImgProduct);
        Picasso.with(this).load(mProduct.getUser_info().getUser_image_url()).into(mImgOwner);

        mTxtOwnerName.setText(mProduct.getUser_info().getFirst_name() + " " + mProduct.getUser_info().getLast_name());

        mTxtFromDate.setText(mBundle.getString(getString(R.string.from_date)));
        mTxtToDate.setText(mBundle.getString(getString(R.string.to_date)));

//        Log.v(TAG,mBundle.getString(getString(R.string.from_date)));
//        Log.v(TAG,mBundle.getString(getString(R.string.to_date)));

        mTxtDays.setText(String.valueOf(mBundle.getInt(getString(R.string.days_count))) + " days");
        mTxtSubTotal.setText("$" + String.valueOf(mBundle.getInt(getString(R.string.total))));

        // TODO calculate discount yet to complete
        mTxtDiscounts.setText("$0.0");
        mTxtTotal.setText(mTxtSubTotal.getText());
//        mBtnTotalPrice.setText(mTxtSubTotal.getText());

        mTxtSelectedDates.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mTxtSelectedDates.getRight() - mTxtSelectedDates.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent=new Intent(RentInfoActivity.this, CalendarActivity.class);
                        intent.putExtra(getString(R.string.from_date),mBundle.getString(getString(R.string.from_date)));
                        intent.putExtra(getString(R.string.to_date),mBundle.getString(getString(R.string.to_date)));
                        intent.putExtra(getString(R.string.selected_dates_list),CalendarActivity.selectedDates);
                        isDateEdited=true;

                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String from = data.getStringExtra(getString(R.string.from_date));
                String to = data.getStringExtra(getString(R.string.to_date));

                DateFormat formatter = new SimpleDateFormat(getString(R.string.date_format));
                try {
                    fromDate = formatter.parse(from.toString());
                    toDate = formatter.parse(to.toString());
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
                Details.dayCount = (int) diff / (24 * 60 * 60 * 1000);

                Details.total = (int) Math.abs(Details.dayCount) * Integer.parseInt(mProduct.getUser_product_info().getPrice_per_day());

//                if (Details.total != 0) {
//                    mBtnTotalPrice.setText(" $" + Details.total);
//                }

                mTxtFromDate.setText(formatedFromDate);
                mTxtToDate.setText(formatedToDate);

                mTxtDays.setText(String.valueOf(Details.dayCount) + " days");
                mTxtSubTotal.setText("$" + String.valueOf(Details.total));

                // TODO calculate discount yet to complete
                mTxtDiscounts.setText("$0.0");
                mTxtTotal.setText("$" + String.valueOf(Details.total));
//                Details.isFromNextActivity = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Details.isFromNextActivity = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Details.isFromNextActivity = true;
            finish();
        }
        return true;
    }

    /**
     * Show payment mode screen
     */

    @OnClick(R.id.btn_proceed_to_pay)
    public void showPaymentModes() {
        Intent intentPayMode = new Intent(this, PaymentModeActivity.class);
        intentPayMode.putExtra(getString(R.string.total), Details.total);
        intentPayMode.putExtra(getString(R.string.total), Details.total);
        startActivity(intentPayMode);
    }
}
