package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.SignUpRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class SignUpActivity extends AppCompatActivity {

    private RCAPInterface service;

    private String mFirstName, mLastName, mEmail, mPassword;

    private int mVerficationCode;

    private long mUserMobNo;

    @BindView(R.id.edit_first_name)
    public EditText mEditFirstName;

    @BindView(R.id.edit_last_name)
    public EditText mEditLastName;

    @BindView(R.id.edit_email)
    public EditText mEditEmail;

    @BindView(R.id.edit_pwd)
    public EditText mEditPassword;

    @BindView(R.id.edit_mob_no)
    public EditText mEditMobNo;

    @BindView(R.id.edit_otp)
    public EditText mEditVerificationCode;

    @BindView(R.id.input_layout_signup_first_name)
    public TextInputLayout mInputLayoutFirstName;

    @BindView(R.id.input_layout_signup_last_name)
    public TextInputLayout mInputLayoutLastName;

    @BindView(R.id.input_layout_signup_email)
    public TextInputLayout mInputLayoutEmail;

    @BindView(R.id.input_layout_signup_pwd)
    public TextInputLayout mInputLayoutPwd;

    @BindView(R.id.input_layout_signup_mob_no)
    public TextInputLayout mInputLayoutMobNo;

    @BindView(R.id.input_layout_signup_verification_code)
    public TextInputLayout mInputLayoutVerificationCode;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.txt_send_code)
    public TextView mTxtSendOtp;

    @BindView(R.id.progress_bar)
    public RelativeLayout mProgressBar;

    @BindView(R.id.layout_scrollView)
    public ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mEditFirstName.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditFirstName));
        mEditLastName.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditLastName));
        mEditEmail.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditEmail));
        mEditPassword.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditPassword));
        mEditMobNo.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditMobNo));
        mEditVerificationCode.addTextChangedListener(new SignUpActivity.MyTextWatcher(mEditVerificationCode));
    }

    /**
     * api call for user sign up
     */
    private void getUserSignUp() {
        SignUpRequest signUpRequest = new SignUpRequest(mFirstName, mLastName, mEmail,
                mPassword, mUserMobNo, mVerficationCode);

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<User> userCall = service.userSignUp(signUpRequest);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setAlpha((float) 1.0);

                if (response.body() != null) {
                    startActivity(new Intent(SignUpActivity.this, SearchActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setAlpha((float) 1.0);
            }
        });
    }

    /**
     * sign up submit button
     *
     * @param v
     */
    @OnClick(R.id.btn_user_sign_up)
    public void btnSignUp(View v) {
        submitForm();
        HideKeyboard.hideKeyBoard(this);

        if (NetworkUtility.isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setAlpha((float) 0.6);
            mFirstName = mEditFirstName.getText().toString();
            mLastName = mEditLastName.getText().toString();
            mEmail = mEditEmail.getText().toString();
            mPassword = mEditPassword.getText().toString();
            mUserMobNo = Long.parseLong(mEditMobNo.getText().toString());
            mVerficationCode = Integer.parseInt(mEditVerificationCode.getText().toString());
            getUserSignUp();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setAlpha((float) 1.0);
            RCLog.showToast(this, getResources().getString(R.string.err_network_available));
        }
    }

    /**
     * textview log in
     *
     * @param view
     */
    @OnClick(R.id.txt_log_in)
    public void txtLogIn(View view) {
        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
    }

    /**
     * button for send verification code
     *
     * @param view
     */
    @OnClick(R.id.txt_send_code)
    public void btnSendCode(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollView.setAlpha((float) 0.6);
        getVerificationCode();
    }

    /**
     * api call for verification code
     */
    private void getVerificationCode() {
        mEmail = mEditEmail.getText().toString();
        mUserMobNo = Long.parseLong(mEditMobNo.getText().toString());

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<User> userCall = service.verificationCode(mEmail, mUserMobNo);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setAlpha((float) 1.0);
                RCLog.showToast(SignUpActivity.this, getString(R.string.toast_send_otp));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setAlpha((float) 1.0);
            }
        });

    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if (!validateVerificationCode()) {
            return;
        }

        if (!validateMobileNo()) {
            return;
        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_first_name:
                    validateFirstName();
                    break;

                case R.id.edit_last_name:
                    validateLastName();
                    break;
                case R.id.edit_email:
                    validateEmail();
                    break;

                case R.id.edit_pwd:
                    validatePassword();
                    break;
                case R.id.edit_mob_no:
                    validateMobileNo();
                    break;

                case R.id.edit_otp:
                    validateVerificationCode();
                    break;
            }
        }
    }
    /**
     * validate first name
     * @return
     */
    private boolean validateFirstName() {
        if (mEditFirstName.getText().toString().trim().isEmpty()) {
            mInputLayoutFirstName.setError(getString(R.string.enter_first_name));
            requestFocus(mEditFirstName);
            return false;
        } else {
            mInputLayoutFirstName.setErrorEnabled(false);
        }

        return true;
    }
    /**
     * validate last name
     * @return
     */
    private boolean validateLastName() {
        if (mEditLastName.getText().toString().trim().isEmpty()) {
            mInputLayoutLastName.setError(getString(R.string.enter_last_name));
            requestFocus(mEditLastName);
            return false;
        } else {
            mInputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }
    /**
     * validate email
     * @return
     */
    private boolean validateEmail() {
        String email = mEditEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mInputLayoutEmail.setError(getString(R.string.validate_email));
            requestFocus(mEditEmail);
            return false;
        } else {
            mInputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    /**
     * validate password
     * @return
     */
    private boolean validatePassword() {
        if (mEditPassword.getText().toString().trim().isEmpty() || mEditPassword.length() < 8) {
            mInputLayoutPwd.setError(getString(R.string.validate_pwd));
            requestFocus(mEditPassword);
            return false;
        } else {
            mInputLayoutPwd.setErrorEnabled(false);
        }

        return true;
    }
    /**
     * validate mobile number
     * @return
     */
    private boolean validateMobileNo() {
        if (mEditMobNo.getText().toString().trim().isEmpty() || mEditMobNo.length() < 10) {
            mInputLayoutMobNo.setError(getString(R.string.validate_mob_no));

            requestFocus(mEditMobNo);
            return false;
        } else {
            mInputLayoutMobNo.setErrorEnabled(false);
            mTxtSendOtp.setVisibility(View.VISIBLE);
        }

        return true;
    }
    /**
     * validate verification code
     * @return
     */
    private boolean validateVerificationCode() {
        if (mEditVerificationCode.getText().toString().trim().isEmpty() || mEditVerificationCode.length() < 6) {
            mInputLayoutVerificationCode.setError(getString(R.string.validate_otp));
            requestFocus(mEditVerificationCode);
            return false;
        } else {
            mInputLayoutVerificationCode.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
