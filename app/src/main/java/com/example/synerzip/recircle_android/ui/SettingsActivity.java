package com.example.synerzip.recircle_android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

//TODO Functionality yet to be comleted

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private RCAPInterface service;

    private String mUserId,mAccessToken;

    @BindView(R.id.txt_user_mob)
    protected TextView mTxtUserMob;

    @BindView(R.id.txt_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.txt_user_email)
    protected TextView mTxtUserEmail;

    @BindView(R.id.img_user_pro)
    protected CircleImageView mImgUserProofile;

    public static String mFirstName,mLastName,mUserImg,mMobNo,mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences=getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME,MODE_PRIVATE);
        mUserId=sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID,mUserId);
        mAccessToken=sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN,mAccessToken);
        getUserDetails();
    }

    /**
     * get user details
     */
    public void getUserDetails(){
        service= ApiClient.getClient().create(RCAPInterface.class);
        Call<RootUserInfo> userCall=service.getUserDetails("Bearer " + mAccessToken,mUserId);
        userCall.enqueue(new Callback<RootUserInfo>() {
            @Override
            public void onResponse(Call<RootUserInfo> call, Response<RootUserInfo> response) {
                if(response.isSuccessful()) {
                    mFirstName = response.body().getFirst_name();
                    mLastName = response.body().getLast_name();
                    mEmail = response.body().getEmail();
                    mMobNo = String.valueOf(response.body().getUser_mob_no());
                    mUserImg = response.body().getUser_image_url();

                    mTxtUserName.setText(mFirstName + " " + mLastName);
                    mTxtUserEmail.setText(mEmail);
                    mTxtUserMob.setText(mMobNo);
                    Picasso.with(SettingsActivity.this).load(mUserImg).into(mImgUserProofile);
                }else {
                    RCLog.showToast(SettingsActivity.this,"Response is null");
                }
            }

            @Override
            public void onFailure(Call<RootUserInfo> call, Throwable t) {

            }
        });
    }

    /**
     * edit profile button
     * @param view
     */
    @OnClick(R.id.btn_edit_profile)
    public void btnEditProfile(View view) {
        startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
    }

    /**
     * cardview for payments method
     * @param view
     */
    @OnClick(R.id.card_payments)
    public void cardPayments(View view) {
        startActivity(new Intent(SettingsActivity.this, PaymentsActivity.class));
    }

    /**
     * cardview for add bank acc
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
