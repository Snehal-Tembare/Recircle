package com.example.synerzip.recircle_android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.Base64Encryption;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 3/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.edit_login_email)
    protected EditText mEditLogInEmail;

    @BindView(R.id.edit_login_pwd)
    protected EditText mEditLogInPassword;

    @BindView(R.id.input_layout_email)
    protected TextInputLayout mInputLayoutEmail;

    @BindView(R.id.input_layout_pwd)
    protected TextInputLayout mInputLayoutPassword;

    private String mUserName, mPassword;

    private RCAPInterface service;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private String mEmail;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.linear_layout)
    protected LinearLayout mLinearLayout;

    protected String mUserId, mUserEmail, mUserLastName, mUserFirstName, mUserImage, mAccessToken = "";

    private long mUserMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.log_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        /*haredPreferences sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

        mPassword=sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_PASSWORD,mPassword);*/

        mEditLogInEmail.addTextChangedListener(new RCTextWatcher(mEditLogInEmail));
        mEditLogInPassword.addTextChangedListener(new RCTextWatcher(mEditLogInPassword));
    }

    /**
     * login submit button
     *
     * @param v
     */
    @OnClick(R.id.btn_user_log_in)
    public void btnLogIn(View v) {

        submitForm();
        HideKeyboard.hideKeyBoard(LogInActivity.this);
        if (NetworkUtility.isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mLinearLayout.setAlpha((float) 0.6);
            mUserName = mEditLogInEmail.getText().toString().trim();
            mPassword=Base64Encryption.encrypt(mEditLogInPassword.getText().toString().trim());

            getUserLogIn();

        } else {
            mProgressBar.setVisibility(View.GONE);
            mLinearLayout.setAlpha((float) 1.0);

            RCLog.showToast(this, getResources().getString(R.string.err_network_available));
        }
    }

    /**
     * api call for user login
     */
    private void getUserLogIn() {

        LogInRequest logInRequest = new LogInRequest(mUserName, mPassword);

        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<User> userCall = service.userLogIn(logInRequest);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayout.setAlpha((float) 1.0);

                    if (response.isSuccessful()) {

                        mUserId = response.body().getUser_id();
                        mUserName = response.body().getEmail();
                        mUserFirstName = response.body().getFirst_name();
                        mUserEmail = response.body().getEmail();
                        mUserLastName = response.body().getLast_name();
                        mAccessToken = response.body().getToken();
                        mUserImage = response.body().getUser_image_url();
                        mUserMobNo = response.body().getUser_mob_no();

                        if (null != mUserId && null != mUserName &&
                                null != mUserFirstName && null != mUserLastName && null != mAccessToken) {
                            saveUserData();
                            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            RCLog.showToast(LogInActivity.this, getString(R.string.please_try_again));
                        }
                    } else {
                        if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                            RCLog.showToast(LogInActivity.this, getString(R.string.err_credentials));
                        }
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
     * textview for create account
     *
     * @param view
     */
    @OnClick(R.id.txt_create_acc)
    public void txtCreateAcc(View view) {
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
    }

    /**
     * forgot password textview
     *
     * @param view
     */
    @OnClick(R.id.txt_forgot_pwd)
    public void txtForgotPwd(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.forgot_pwd_dialog);
        dialog.setTitle(getString(R.string.dialog_forgot_pwd));

        final EditText mEditTxtEmail = (EditText) dialog.findViewById(R.id.edit_forgot_pwd_otp_email);
        mEditTxtEmail.getText().toString();

        Button btnSendOtp = (Button) dialog.findViewById(R.id.btn_send_otp);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setAlpha((float) 0.6);
                mEmail = mEditTxtEmail.getText().toString();

                if (ApiClient.getClient(LogInActivity.this)!=null){
                service = ApiClient.getClient(LogInActivity.this).create(RCAPInterface.class);
                Call<User> call = service.otpForgotPassword(mEmail);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);
                        RCLog.showToast(getApplicationContext(), getString(R.string.toast_send_otp));
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (null != mEmail) {
                                startActivity(new Intent(LogInActivity.this, ForgotPwdActivity.class));
                            } else {
                                RCLog.showToast(LogInActivity.this, getString(R.string.please_try_again));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setAlpha((float) 1.0);
                    }
                });
            }else {
                    mProgressBar.setVisibility(View.GONE);
                }
        }

        });

        dialog.show();
    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }

    /**
     * validate email
     *
     * @return
     */
    private boolean validateEmail() {
        String email = mEditLogInEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            mInputLayoutEmail.setError(getString(R.string.validate_email));
            requestFocus(mEditLogInEmail);
            return false;
        } else {
            mInputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * validate password
     *
     * @return
     */
    private boolean validatePassword() {
        if (mEditLogInPassword.getText().toString().trim().isEmpty() || mEditLogInPassword.length() < 8) {
            mInputLayoutPassword.setError(getString(R.string.validate_pwd));
            requestFocus(mEditLogInPassword);
            return false;
        } else {
            mInputLayoutPassword.setErrorEnabled(false);
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
                case R.id.edit_login_email:
                    validateEmail();
                    break;

                case R.id.edit_login_pwd:
                    validatePassword();
                    break;
            }
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
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * saves user details in Shared Preferences
     */
    private void saveUserData() {

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        SharedPreferences sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        try {
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_PASSWORD, mPassword);
            editor.putBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, true);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_EMAIL, mUserEmail);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_FIRSTNAME, mUserFirstName);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_LASTNAME, mUserLastName);
            editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_IMAGE, mUserImage);
            editor.putLong(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_MOB_NO, mUserMobNo);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
