package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.RootUserInfo;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO Functionality yet to be completed

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final long TIMER = 800;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private RCAPInterface service;

    public static String mUserId, mAccessToken, mUserAddreessId;

    @BindView(R.id.txt_user_mob)
    protected TextView mTxtUserMob;

    @BindView(R.id.txt_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.txt_user_email)
    protected TextView mTxtUserEmail;

    @BindView(R.id.img_user_pro)
    protected CircleImageView mImgUserProfile;

    public static String mFirstName, mLastName, mUserImg, mMobNo, mEmail;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    @BindView(R.id.linear_layout_street_addr)
    protected LinearLayout mLinearLayoutStreetAddr;

    @BindView(R.id.linear_layout_state)
    protected LinearLayout mLinearLayoutState;

    @BindView(R.id.linear_layout_city)
    protected LinearLayout mLinearLayoutCity;

    @BindView(R.id.linear_layout_zipcode)
    protected LinearLayout mLinearLayoutZipcode;

    @BindView(R.id.txt_street_addr)
    protected TextView mTxtStreetAddr;

    @BindView(R.id.txt_city)
    protected TextView mTxtCity;

    @BindView(R.id.txt_state)
    protected TextView mTxtState;

    @BindView(R.id.txt_zipcode)
    protected TextView mTxtZipcode;

    @BindView(R.id.switch_notification)
    protected SwitchCompat mSwitchNotification;

    public static boolean mTextNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwitchNotification.setKeyListener(null);

        SharedPreferences sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME,
                MODE_PRIVATE);
        mUserId = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

        mProgressBar.setVisibility(View.VISIBLE);
        mFrameLayout.setAlpha((float) 0.6);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    /**
     * get user details
     */
    public void getUserDetails() {
        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<RootUserInfo> userCall = service.getUserDetails("Bearer " + mAccessToken, mUserId);
            userCall.enqueue(new Callback<RootUserInfo>() {
                @Override
                public void onResponse(Call<RootUserInfo> call, Response<RootUserInfo> response) {
                    if (response.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        mFrameLayout.setAlpha((float) 1.0);
                        mFirstName = response.body().getFirst_name();
                        mLastName = response.body().getLast_name();
                        if (!response.body().getFirst_name().isEmpty() && !response.body().getLast_name().isEmpty()) {

                            mTxtUserName.setText(mFirstName + " " + mLastName);
                        }

                        mEmail = response.body().getEmail();
                        mMobNo = String.valueOf(response.body().getUser_mob_no());
                        mUserImg = response.body().getUser_image_url();
                        if (response.body().getUser_id() != null) {
                            mUserId = response.body().getUser_id();
                        }

                        if (response.body().getUserAddress() != null) {
                            mUserAddreessId = response.body().getUserAddress().getUser_address_id();

                            if (!response.body().getUserAddress().getCity().isEmpty()) {
                                mLinearLayoutCity.setVisibility(View.VISIBLE);
                                mTxtCity.setText(response.body().getUserAddress().getCity());
                            }
                            if (!response.body().getUserAddress().getState().isEmpty()) {
                                mLinearLayoutState.setVisibility(View.VISIBLE);
                                mTxtState.setText(response.body().getUserAddress().getState());
                            }
                            if (!response.body().getUserAddress().getStreet().isEmpty()) {
                                mLinearLayoutStreetAddr.setVisibility(View.VISIBLE);
                                mTxtStreetAddr.setText(response.body().getUserAddress().getStreet());
                            }
                            if (response.body().getUserAddress().getZip() != 0) {
                                mLinearLayoutZipcode.setVisibility(View.VISIBLE);
                                mTxtZipcode.setText(String.valueOf(response.body().getUserAddress().getZip()));
                            }
                        }
                        mTextNotification = response.body().isNotification_flag();

                        if (mTextNotification) {
                            mSwitchNotification.setChecked(true);
                        } else {
                            mSwitchNotification.setChecked(false);
                        }


                        mTxtUserName.setText(mFirstName + " " + mLastName);
                        mTxtUserEmail.setText(mEmail);
                        mTxtUserMob.setText(mMobNo);
                        Picasso.with(SettingsActivity.this).load(mUserImg)
                                .placeholder(R.drawable.ic_user).into(mImgUserProfile);
                    } else {

                        RCLog.showToast(SettingsActivity.this, getString(R.string.user_not_authenticated));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, TIMER);

                        mProgressBar.setVisibility(View.GONE);
                        mFrameLayout.setAlpha((float) 1.0);
                    }
                }

                @Override
                public void onFailure(Call<RootUserInfo> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    mFrameLayout.setAlpha((float) 1.0);
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * edit user profile button
     *
     * @param view
     */
    @OnClick(R.id.btn_edit_profile)
    public void btnEditProfile(View view) {
        startActivityForResult(new Intent(SettingsActivity.this, EditProfileActivity.class),
                REQUEST_CODE);
    }

    /**
     * get edit user details in onActivityResult()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUserDetails();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (!EditProfileActivity.mFirstName.isEmpty() && !EditProfileActivity.mLastName.isEmpty()) {
                    mTxtUserName.setText(EditProfileActivity.mFirstName + " " + EditProfileActivity.mLastName);
                }
                if (EditProfileActivity.mMobNo != 0) {
                    mTxtUserName.setText(String.valueOf(EditProfileActivity.mMobNo));
                }
                if (!EditProfileActivity.mAddress.getStreet().isEmpty()) {
                    mLinearLayoutStreetAddr.setVisibility(View.VISIBLE);
                    mTxtStreetAddr.setText(EditProfileActivity.mAddress.getStreet());
                }
                if (!EditProfileActivity.mAddress.getState().isEmpty()) {
                    mLinearLayoutState.setVisibility(View.VISIBLE);
                    mTxtState.setText(EditProfileActivity.mAddress.getState());

                }
                if (!EditProfileActivity.mAddress.getCity().isEmpty()) {
                    mLinearLayoutCity.setVisibility(View.VISIBLE);
                    mTxtCity.setText(EditProfileActivity.mAddress.getCity());

                }
                if (EditProfileActivity.mAddress.getZip() != 0) {
                    mLinearLayoutZipcode.setVisibility(View.VISIBLE);
                    String zipcode = String.valueOf(EditProfileActivity.mAddress.getZip());
                    mTxtZipcode.setText(zipcode);
                }
                if (EditProfileActivity.mTextNotification) {
                    mSwitchNotification.setChecked(true);
                } else {
                    mSwitchNotification.setChecked(false);
                }
            }
        }
    }

    /**
     * cardview for payments method
     *
     * @param view
     */
    @OnClick(R.id.card_payments)
    public void cardPayments(View view) {
        startActivity(new Intent(SettingsActivity.this, PaymentsActivity.class));
    }

    /**
     * cardview for add bank acc
     *
     * @param view
     */
    @OnClick(R.id.card_bank_acc)
    public void cardBankAcc(View view) {
        startActivity(new Intent(SettingsActivity.this, BankAccActivity.class));
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
