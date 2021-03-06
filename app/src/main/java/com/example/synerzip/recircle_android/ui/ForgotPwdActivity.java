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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.ForgotPwdRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.Base64Encryption;
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
 * Created by Prajakta Patil on 24/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class ForgotPwdActivity extends AppCompatActivity {
    private RCAPInterface service;

    @BindView(R.id.input_layout_forgot_pwd_email)
    protected TextInputLayout mInputLayoutEmail;

    @BindView(R.id.input_layout_forgot_pwd_resend_otp)
    protected TextInputLayout mInputLayoutOtp;

    @BindView(R.id.input_layout_forgot_pwd_new_pwd)
    protected TextInputLayout mInputLayoutNewPwd;

    @BindView(R.id.edit_forgot_pwd_email)
    protected EditText mEditEmail;

    @BindView(R.id.edit_forgot_pwd_otp)
    protected EditText mEditOtp;

    @BindView(R.id.edit_forgot_pwd_new_pwd)
    protected EditText mEditNewPwd;

    private String mEmail, mNewPwd;
    private int mOtp;

    @BindView(R.id.txt_resend_code)
    public TextView mTxtResendOtp;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.layout_activity_forgot)
    protected LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.forgot_pwd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        mEditEmail.addTextChangedListener(new RCTextWatcher(mEditEmail));
        mEditOtp.addTextChangedListener(new RCTextWatcher(mEditOtp));
        mEditNewPwd.addTextChangedListener(new RCTextWatcher(mEditNewPwd));
    }

    /**
     * forgot pwd submit button
     *
     * @param view
     */
    @OnClick(R.id.btn_submit)
    public void btnSubmit(View view) {
        submitForm();
        HideKeyboard.hideKeyBoard(ForgotPwdActivity.this);

        if (NetworkUtility.isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mLinearLayout.setAlpha((float) 0.6);
            mEmail = mEditEmail.getText().toString();
            mOtp = Integer.parseInt(mEditOtp.getText().toString());
            mNewPwd = Base64Encryption.encrypt(mEditNewPwd.getText().toString());
            getForgotPwd();

        } else {
            mProgressBar.setVisibility(View.GONE);
            mLinearLayout.setAlpha((float) 1.0);
            RCLog.showToast(this, getResources().getString(R.string.err_network_available));
        }
    }

    /**
     * api call to reset passward
     */
    public void getForgotPwd() {
        ForgotPwdRequest forgotPwdRequest = new ForgotPwdRequest(mOtp, mEmail, mNewPwd);
        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<User> call = service.forgotPassword(forgotPwdRequest);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setAlpha((float) 1.0);
                    if (response.isSuccessful()) {
                        startActivity(new Intent(ForgotPwdActivity.this, LogInActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setAlpha((float) 1.0);
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * textview for resend otp
     *
     * @param view
     */
    @OnClick(R.id.txt_resend_code)
    public void txtResendOtp(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayout.setAlpha((float) 0.6);
        mEmail = mEditEmail.getText().toString();

        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<User> call = service.otpForgotPassword(mEmail);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setAlpha((float) 1.0);
                    if (response.isSuccessful()) {
                        RCLog.showToast(getApplicationContext(), getString(R.string.toast_send_otp));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setAlpha((float) 1.0);
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }
        if (!validateOtp()) {
            return;
        }
        if (!validateNewPassword()) {
            return;
        }
    }

    /**
     * TextInputLayout TextWatcher class
     */
    private class RCTextWatcher implements TextWatcher {

        private View view;

        private RCTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_forgot_pwd_email:
                    validateEmail();
                    break;

                case R.id.edit_forgot_pwd_otp:
                    validateOtp();
                    break;
                case R.id.edit_forgot_pwd_new_pwd:
                    validateNewPassword();
                    break;
            }
        }
    }

    /**
     * validate email
     *
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
            mTxtResendOtp.setVisibility(View.VISIBLE);
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

    /**
     * validate password
     *
     * @return
     */
    private boolean validateNewPassword() {
        if (mEditNewPwd.getText().toString().trim().isEmpty() || mEditNewPwd.length() < 8) {
            mInputLayoutNewPwd.setError(getString(R.string.validate_pwd));
            requestFocus(mEditNewPwd);
            return false;
        } else {
            mInputLayoutNewPwd.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * validate otp
     *
     * @return
     */
    private boolean validateOtp() {
        if (mEditOtp.getText().toString().trim().isEmpty() || mEditOtp.length() < 6) {
            mInputLayoutOtp.setError(getString(R.string.validate_otp));
            requestFocus(mEditOtp);
            return false;
        } else {
            mInputLayoutOtp.setErrorEnabled(false);
        }

        return true;
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
