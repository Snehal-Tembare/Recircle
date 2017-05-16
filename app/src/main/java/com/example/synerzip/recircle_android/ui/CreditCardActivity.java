package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.RentItem;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snehal Tembare on 3/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class CreditCardActivity extends AppCompatActivity {

    private static final String EXPIRARY_DATE_PATTERN = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$";
    private static final String TAG = "CreditCardActivity";
    private Bundle mBundle;
    private AwesomeValidation awesomeValidation;
    private SharedPreferences sharedPreferences;
    private boolean isLoggedIn;
    private String mAccessToken;
    private RCAPInterface service;
    private String user_id;
    private String looged_user_id;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.btn_pay)
    protected Button mBtnPay;

    @BindView(R.id.payment_amount)
    protected TextView mTxtPaymentAmount;

    @BindView(R.id.parent_layout)
    protected LinearLayout mLinearLayout;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

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
        getSupportActionBar().setTitle(getString(R.string.card_details));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mTxtPaymentAmount.setVisibility(View.GONE);

//        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        service = ApiClient.getClient().create(RCAPInterface.class);

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        looged_user_id = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, user_id);

//       awesomeValidation.addValidation(CreditCardActivity.this, R.id.expiry_date, EXPIRARY_DATE_PATTERN, R.string.expiry_date_error);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            user_id = mBundle.getString(getString(R.string.user_id));
            mBtnPay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRedSecondary));
            mBtnPay.setText("Pay $" + String.valueOf(mBundle.getInt(getString(R.string.total))));
        }


    }

    @OnClick(R.id.btn_pay)
    public void validateFields() {
        if (isLoggedIn) {
            if (!user_id.equalsIgnoreCase(looged_user_id)) {
                    HideKeyboard.hideKeyBoard(CreditCardActivity.this);
                    if (NetworkUtility.isNetworkAvailable(this)) {

                        DateFormat dateFormat = new SimpleDateFormat(getString(R.string.calendar_date_format));
                        DateFormat calendarFormat = new SimpleDateFormat(getString(R.string.date_format));
                        try {
                            if (RentInfoActivity.mRentItem != null) {

                                Date fromDate = calendarFormat.parse(RentInfoActivity.mRentItem.getOrder_from_date());
                                Date toDate = calendarFormat.parse(RentInfoActivity.mRentItem.getOrder_to_date());

                                String orderFromDate = dateFormat.format(fromDate);
                                String orderToDate = dateFormat.format(toDate);

                                RentInfoActivity.mRentItem.setOrder_from_date(orderFromDate);
                                RentInfoActivity.mRentItem.setOrder_to_date(orderToDate);
                                Log.v(TAG, "From Date" + orderFromDate);
                                Log.v(TAG, "To Date" + orderToDate);

                                Calendar calendar = Calendar.getInstance();
                                String dateOnOrder = dateFormat.format(calendar.getTime());
                                Log.v(TAG, "currentDate" + dateOnOrder);

                                RentInfoActivity.mRentItem.setDate_on_order(dateOnOrder);

                                Log.v("CreditCardActivity", "UserId" + RentInfoActivity.mRentItem.getUser_product_id());
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mProgressBar.setVisibility(View.VISIBLE);
                        mLinearLayout.setAlpha((float) 0.6);

                        final Call<RentItem> call = service.rentItem("Bearer " + mAccessToken, RentInfoActivity.mRentItem);
                        call.enqueue(new Callback<RentItem>() {
                            @Override
                            public void onResponse(Call<RentItem> call, Response<RentItem> response) {
                                mProgressBar.setVisibility(View.GONE);
                                mLinearLayout.setAlpha((float) 1.0);

                                if (response.isSuccessful()) {
                                    RCLog.showToast(CreditCardActivity.this, getString(R.string.item_added));
                                    Intent intent = new Intent(CreditCardActivity.this, RentItemSuccessActivity.class);
                                    startActivity(intent);
                                } else {
                                    if (response.code() == RCWebConstants.RC_ERROR_CODE_BAD_REQUEST) {
                                        RCLog.showToast(CreditCardActivity.this, getString(R.string.product_creation_failed));
                                    } else {
                                        RCLog.showToast(CreditCardActivity.this, getString(R.string.user_not_authenticated));
                                        logInDialog();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RentItem> call, Throwable t) {
                                RCLog.showToast(CreditCardActivity.this, getString(R.string.product_creation_failed));
                            }
                        });
                    }
            } else {
                RCLog.showToast(this, getString(R.string.rent_warning_msg));
            }
        } else {
            RCLog.showToast(CreditCardActivity.this, getString(R.string.user_must_login));
            logInDialog();
        }
    }

    /**
     * Login again dialog
     */
    private void logInDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_again_dialog);
        dialog.setTitle(getString(R.string.log_in));
        final EditText mEditTxtUserName = (EditText) dialog.findViewById(R.id.edit_login_email_dialog);
        final EditText mEditTxtPwd = (EditText) dialog.findViewById(R.id.edit_login_pwd_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_user_log_in_dialog);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setAlpha((float) 0.6);

                final String mUserName = mEditTxtUserName.getText().toString();
                final String mUserPwd = mEditTxtPwd.getText().toString();
                LogInRequest logInRequest = new LogInRequest(mUserName, mUserPwd);

                service = ApiClient.getClient().create(RCAPInterface.class);
                Call<User> userCall = service.userLogIn(logInRequest);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);

                        if (response.isSuccessful()) {
                            mAccessToken = response.body().getToken();
                            sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
                            editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, true);
                            editor.apply();
                            isLoggedIn=true;
                            dialog.dismiss();
                        } else {
                            if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                RCLog.showToast(CreditCardActivity.this, getString(R.string.err_credentials));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);
                    }
                });
            }
        });

        dialog.show();
    }
}
