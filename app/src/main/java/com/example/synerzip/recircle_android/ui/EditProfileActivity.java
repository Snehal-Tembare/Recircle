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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.ChangePwdRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.UserAccDetails;
import com.example.synerzip.recircle_android.models.UserAddress;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by Prajakta Patil on 17/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.img_user_profile)
    protected CircleImageView mImgUserProfile;

    @BindView(R.id.edit_user_first_name)
    protected EditText mEditTxtFirstName;

    @BindView(R.id.edit_user_last_name)
    protected EditText mEditTxtLastName;

    @BindView(R.id.edit_user_email)
    protected EditText mEditTxtEmail;

    @BindView(R.id.edit_user_mob)
    protected EditText mEditTxtMob;

    @BindView(R.id.edit_street_addr)
    protected EditText mEditTxtStreetAddr;

    @BindView(R.id.edit_city)
    protected EditText mEditTxtCity;

    @BindView(R.id.edit_state)
    protected EditText mEditTxtState;

    @BindView(R.id.edit_zipcode)
    protected EditText mEditZipcode;

    @BindView(R.id.switch_notification)
    protected SwitchCompat mSwitchNotification;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private Button btnSelect;

    private String userChoosenTask;

    private SharedPreferences sharedPreferences;

    private RCAPInterface service;

    private String mAccessToken;

    private boolean isLoggedIn;

    private String mUserId, mFirstName, mLastName, mEmail, mUserImage, mPaymentMethods;

    private boolean mNotificationFlag, mMobVerification;

    private UserAddress mUserAddress;

    private UserAccDetails mAccDetails;

    private long mUserMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);

    }

    /**
     * Login again dialog
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
                final String mUserName = mEditTxtOldPwd.getText().toString();
                final String mUserPwd = mEditTxtNewPwd.getText().toString();
                ChangePwdRequest changePwdRequest = new ChangePwdRequest(mUserName, mUserPwd);

                service = ApiClient.getClient().create(RCAPInterface.class);
                Call<User> userCall = service.changePwd("Bearer " + mAccessToken, changePwdRequest);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
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

                    }
                });
            }
        });

        dialog.show();

    }//end changePwdDialog()

    /**
     * cancel button to close activity
     * @param view
     */
    @OnClick(R.id.txt_cancel_edit_pro)
    public void txtCancel(View view) {
        finish();
    }

    /**
     * change user profile picture
     * @param view
     */
    @OnClick(R.id.img_user_profile)
    public void imgUserProfile(View view) {
        selectImage();
    }

    /**
     * change user password
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
                    if (userChoosenTask.equals(getString(R.string.take_photo)))
                        cameraIntent();
                    else if (userChoosenTask.equals(getString(R.string.choose_from_gallery)))
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
                    userChoosenTask = getString(R.string.take_photo);
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                    userChoosenTask = getString(R.string.choose_from_gallery);
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
