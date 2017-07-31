package com.example.synerzip.recircle_android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pkmmte.view.CircularImageView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.ChangePwdRequest;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.RootUserInfo;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserAccDetails;
import com.example.synerzip.recircle_android.models.UserAddress;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.Base64Encryption;
import com.example.synerzip.recircle_android.utilities.HideKeyboard;
import com.example.synerzip.recircle_android.utilities.NetworkUtility;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 17/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.img_user_profile)
    protected CircularImageView mImgUserProfile;

    @BindView(R.id.input_layout_user_first_name)
    protected TextInputLayout mInputLayoutFirstName;

    @BindView(R.id.edit_user_first_name)
    protected EditText mEditTxtFirstName;

    @BindView(R.id.input_layout_user_last_name)
    protected TextInputLayout mInputLayoutLastName;

    @BindView(R.id.edit_user_last_name)
    protected EditText mEditTxtLastName;

    @BindView(R.id.input_layout_user_mob)
    protected TextInputLayout mInputLayoutMob;

    @BindView(R.id.edit_user_mob)
    protected EditText mEditTxtMob;

    @BindView(R.id.edit_street_addr)
    protected EditText mEditTxtStreetAddr;

    @BindView(R.id.edit_city)
    protected EditText mEditTxtCity;

    @BindView(R.id.edit_state)
    protected EditText mEditTxtState;

    @BindView(R.id.edit_zipcode)
    protected EditText mEditTxtZipcode;

    @BindView(R.id.switch_notification)
    protected SwitchCompat mSwitchNotification;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String userChosenTask;

    private SharedPreferences sharedPreferences;

    private RCAPInterface service;

    private String mAccessToken;

    private boolean isLoggedIn;

    public static String mUserId, mFirstName, mLastName, mEmail, mUserImage;

    private boolean mNotificationFlag, mMobVerification;

    public static UserAddress mAddress;

    private UserAccDetails mAccDetails;

    public static long mMobNo, mUserMobNo;

    private String mUserName;

    @BindView(R.id.txt_verify_mob_no)
    protected TextView mTxtVerifyMob;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    public static boolean mTextNotification;
    private RootUserInfo rootUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mUserName = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USERNAME, mUserName);
        mUserMobNo = sharedPreferences.getLong(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_MOB_NO, mUserMobNo);

        mEditTxtFirstName.addTextChangedListener(new EditProfileActivity.RCTextWatcher(mEditTxtFirstName));
        mEditTxtLastName.addTextChangedListener(new EditProfileActivity.RCTextWatcher(mEditTxtLastName));
        mEditTxtMob.addTextChangedListener(new EditProfileActivity.RCTextWatcher(mEditTxtMob));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rootUserInfo = bundle.getParcelable(getString(R.string.rootUserInfo));

            mEditTxtFirstName.setText(rootUserInfo.getFirst_name());
            mEditTxtLastName.setText(rootUserInfo.getLast_name());
            mEditTxtMob.setText(String.valueOf(rootUserInfo.getUser_mob_no()));
            mSwitchNotification.setChecked(rootUserInfo.isNotification_flag());
            if (rootUserInfo.getUserAddress() != null) {
                mEditTxtCity.setText(rootUserInfo.getUserAddress().getCity());
                mEditTxtState.setText(rootUserInfo.getUserAddress().getState());
                mEditTxtStreetAddr.setText(rootUserInfo.getUserAddress().getStreet());
                mEditTxtZipcode.setText(rootUserInfo.getUserAddress().getZip());
            }
            Picasso.with(this).load(rootUserInfo.getUser_image_url()).placeholder(R.drawable.ic_user).into(mImgUserProfile);
        }
    }

    /**
     * save user details
     *
     * @param view
     */
    @OnClick(R.id.btn_save_user_details)
    public void btnSubmitDetails(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setAlpha((float) 0.6);
        submitForm();
        HideKeyboard.hideKeyBoard(this);
        if (NetworkUtility.isNetworkAvailable()) {
            if (getValues()) {
                editUserDetails();

            } else {
                RCLog.showToast(this, getString(R.string.mandatory_dates));
            }
        } else {
            RCLog.showToast(this, getResources().getString(R.string.err_network_available));
        }
    }

    /**
     * get values for edit user details
     */
    public boolean getValues() {

        if (!SettingsActivity.mUserId.isEmpty()) {
            mUserId = SettingsActivity.mUserId;
        }
        if (!mEditTxtFirstName.getText().toString().isEmpty()) {
            mFirstName = mEditTxtFirstName.getText().toString();
        }
        if (!mEditTxtLastName.getText().toString().isEmpty()) {
            mLastName = mEditTxtLastName.getText().toString();
        }
        //TODO image url is hardcoded
        mUserImage = "https://s3.ap-south-1.amazonaws.com/recircleimages/men3.png";
        if (mSwitchNotification.isChecked()) {
            mNotificationFlag = true;
        }

        if (!mEditTxtMob.getText().toString().isEmpty()) {
            mMobNo = Long.parseLong(mEditTxtMob.getText().toString());
        }

        mAddress = new UserAddress(SettingsActivity.mUserAddreessId,
                mEditTxtStreetAddr.getText().toString(),
                mEditTxtCity.getText().toString(), mEditTxtState.getText().toString(),
                Integer.parseInt(mEditTxtZipcode.getText().toString()));
        //TODO data is hardcoded as payments module is yet to be implement
        mAccDetails = new UserAccDetails("6689d008-6b6d-437b-81b5-0eb8b3710743",
                646743343, "abhijit", 783723, "2017-03-25",
                324332325, "individual");
        return true;
    }

    /**
     * button for mobile verification
     *
     * @param view
     */
    @OnClick(R.id.txt_verify_mob_no)
    public void btnVerify(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setAlpha((float) 0.6);
        verifyMobNo();
    }

    /**
     * api call for mobile verification
     */
    private void verifyMobNo() {
        mMobNo = Long.parseLong(mEditTxtMob.getText().toString());

        service = ApiClient.getClient(this).create(RCAPInterface.class);
        Call<RootUserInfo> userCall = service.verifyUserMobNo("Bearer " + mAccessToken);
        userCall.enqueue(new Callback<RootUserInfo>() {
            @Override
            public void onResponse(Call<RootUserInfo> call, Response<RootUserInfo> response) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
                if (response.isSuccessful()) {
                    RCLog.showToast(EditProfileActivity.this, getString(R.string.toast_send_otp));
                    mMobVerification = true;
                }
            }

            @Override
            public void onFailure(Call<RootUserInfo> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
            }
        });

    }

    /**
     * api call for editing user details
     */
    private void editUserDetails() {
        getValues();
        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            RootUserInfo rootUserInfo = new RootUserInfo(mUserId, mFirstName, mLastName, mEmail, mUserImage,
                    mNotificationFlag,
                    mMobNo, mMobVerification
                    , mAddress, mAccDetails);
            Call<RootUserInfo> call = service.editUser("Bearer " + mAccessToken, rootUserInfo);
            call.enqueue(new Callback<RootUserInfo>() {
                @Override
                public void onResponse(Call<RootUserInfo> call, Response<RootUserInfo> response) {
                    mProgressBar.setVisibility(View.GONE);
                    mFrameLayout.setAlpha((float) 1.0);
                    if (response.isSuccessful()) {
                        RCLog.showToast(EditProfileActivity.this, "Profile updated");

                        mUserName = response.body().getFirst_name() + " " + response.body().getLast_name();
                        mUserMobNo = response.body().getUser_mob_no();
                        if (mAddress != null) {
                            mAddress.setCity(response.body().getUserAddress().getCity());
                            mAddress.setState(response.body().getUserAddress().getState());
                            mAddress.setStreet(response.body().getUserAddress().getStreet());
                            mAddress.setZip(response.body().getUserAddress().getZip());
                        }
                        mTextNotification = response.body().isNotification_flag();

                        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
                        editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USERNAME, mUserName);
                        editor.putLong(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_USER_MOB_NO, mUserMobNo);

                        editor.apply();
                        editor.commit();
                        finish();

                    } else {
                        if (response.code() == RCWebConstants.RC_ERROR_CODE_BAD_REQUEST) {
                            RCLog.showToast(EditProfileActivity.this, getString(R.string.product_creation_failed));
                        } else {
                            RCLog.showToast(EditProfileActivity.this, getString(R.string.user_not_authenticated));
                            logInDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RootUserInfo> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    mFrameLayout.setAlpha((float) 1.0);
                }
            });
        }
    }

    /**
     * Login again dialog
     */
    private void logInDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_again_dialog);
        dialog.setTitle(getString(R.string.log_in_again));
        final EditText mEditTxtUserName = (EditText) dialog.findViewById(R.id.edit_login_email_dialog);
        final EditText mEditTxtPwd = (EditText) dialog.findViewById(R.id.edit_login_pwd_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_user_log_in_dialog);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mFrameLayout.setAlpha((float) 0.6);
                final String mUserName = mEditTxtUserName.getText().toString();
                final String mUserPwd = mEditTxtPwd.getText().toString();
                LogInRequest logInRequest = new LogInRequest(mUserName, mUserPwd);

                if (ApiClient.getClient(EditProfileActivity.this) != null) {
                    service = ApiClient.getClient(EditProfileActivity.this).create(RCAPInterface.class);
                    Call<User> userCall = service.userLogIn(logInRequest);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            mProgressBar.setVisibility(View.GONE);
                            mFrameLayout.setAlpha((float) 1.0);

                            if (response.isSuccessful()) {
                                mAccessToken = response.body().getToken();
                                sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
                                editor.apply();
                                dialog.dismiss();
                            } else {
                                if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                    RCLog.showToast(EditProfileActivity.this, getString(R.string.err_credentials));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            mFrameLayout.setAlpha((float) 1.0);
                        }
                    });
                }
            }
        });

        dialog.show();
    }


    /**
     * update user password dialog
     */
    private void changePwdDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_pwd_dialog);
        dialog.setTitle(getString(R.string.change_pwd));
        final EditText mEditTxtOldPwd = (EditText) dialog.findViewById(R.id.edit_current_pwd_dialog);
        final EditText mEditTxtNewPwd = (EditText) dialog.findViewById(R.id.edit_new_pwd_dialog);

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnChangePwd = (Button) dialog.findViewById(R.id.btn_save_pwd);
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mUserName = Base64Encryption.encrypt(mEditTxtOldPwd.getText().toString());
                final String mUserPwd = Base64Encryption.encrypt(mEditTxtNewPwd.getText().toString());
                ChangePwdRequest changePwdRequest = new ChangePwdRequest(mUserName, mUserPwd);

                if (ApiClient.getClient(EditProfileActivity.this) != null) {
                    service = ApiClient.getClient(EditProfileActivity.this).create(RCAPInterface.class);
                    Call<User> userCall = service.changePwd("Bearer " + mAccessToken, changePwdRequest);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            mProgressBar.setVisibility(View.GONE);
                            mFrameLayout.setAlpha((float) 1.0);
                            if (response.isSuccessful()) {
                                mAccessToken = response.body().getToken();
                                sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
                                editor.apply();
                                dialog.dismiss();
                                RCLog.showToast(EditProfileActivity.this, getString(R.string.changed_pwd));
                            } else {
                                if (response.code() == RCWebConstants.RC_ERROR_UNAUTHORISED) {
                                    RCLog.showToast(EditProfileActivity.this, getString(R.string.err_credentials));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            mProgressBar.setVisibility(View.GONE);
                            mFrameLayout.setAlpha((float) 1.0);
                        }
                    });
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        dialog.show();

    }//end changePwdDialog()

    /**
     * cancel button to close activity
     *
     * @param view
     */
    @OnClick(R.id.txt_cancel_edit_pro)
    public void txtCancel(View view) {
        finish();
    }

    /**
     * change user profile picture
     *
     * @param view
     */
    @OnClick(R.id.btn_user_profile)
    public void imgUserProfile(View view) {
        selectImage();
    }

    /**
     * change user password
     *
     * @param view
     */
    @OnClick(R.id.txtChangePwd)
    public void txtChangePwd(View view) {
        changePwdDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImageUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChosenTask.equals(getString(R.string.take_photo)))
                        cameraIntent();
                    else if (userChosenTask.equals(getString(R.string.choose_from_gallery)))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    /**
     * select image from gallery or camera
     */
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_gallery),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = ImageUtility.checkPermission(EditProfileActivity.this);

                if (items[item].equals(getString(R.string.take_photo))) {
                    userChosenTask = getString(R.string.take_photo);
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                    userChosenTask = getString(R.string.choose_from_gallery);
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImgUserProfile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mImgUserProfile.setImageBitmap(bm);
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
                case R.id.edit_user_first_name:
                    validateFirstName();
                    break;

                case R.id.edit_user_last_name:
                    validateLastName();
                    break;

                case R.id.edit_user_mob:
                    validateUserMob();
                    break;
            }
        }
    }

    /**
     * validation for user first name edittext
     *
     * @return
     */
    private boolean validateFirstName() {

        if (mEditTxtFirstName.getText().toString().trim().isEmpty()) {
            mInputLayoutFirstName.setError(getString(R.string.validate_first_name));
            requestFocus(mEditTxtFirstName);
            return false;
        } else {
            mInputLayoutFirstName.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * validation for user last name edittext
     *
     * @return
     */
    private boolean validateLastName() {

        if (mEditTxtFirstName.getText().toString().trim().isEmpty()) {
            mInputLayoutFirstName.setError(getString(R.string.validate_last_name));
            requestFocus(mEditTxtFirstName);
            return false;
        } else {
            mInputLayoutFirstName.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * validation for mobile no. edittext
     *
     * @return
     */
    private boolean validateUserMob() {
        if (mEditTxtMob.getText().toString().trim().isEmpty() || mEditTxtMob.length() < 10) {
            mInputLayoutMob.setError(getString(R.string.validate_mob_no));

            requestFocus(mEditTxtMob);
            return false;
        } else {
            mInputLayoutMob.setErrorEnabled(false);
            mTxtVerifyMob.setVisibility(View.GONE);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
        if (!validateUserMob()) {
            return;
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
