package com.example.synerzip.recircle_android.ui.messages;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.models.user_messages.UserAskQueRequest;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.ui.SettingsActivity;
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

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class UserQueAnsActivity extends AppCompatActivity {

    private RCAPInterface service;

    private String mAccessToken;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @BindView(R.id.frame_layout)
    protected FrameLayout mFrameLayout;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.edit_txt_ask_que)
    protected EditText mEditTxtAskQue;

    private String mAskQue,mUserFirstName,mUserLastName;

    @BindView(R.id.txt_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.txt_product_name)
    protected TextView mTxtProdName;

    @BindView(R.id.img_user_profile)
    protected CircleImageView mImgUserProfile;

    private String productId;

    //TODO functionality not completed yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_que_ans);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToolbar.setTitle(R.string.ask_que);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));

        SharedPreferences sharedPreferences = getSharedPreferences
                (RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString
                (RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);
        mUserFirstName=sharedPreferences.getString
                (RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_FIRST_USERNAME, mUserFirstName);
        mUserLastName=sharedPreferences.getString
                (RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_LAST_USERNAME, mUserLastName);

        mTxtUserName.setText(mUserFirstName+" "+mUserLastName);

        Picasso.with(this).load(SettingsActivity.mUserImg).placeholder(R.drawable.ic_user).
                into(mImgUserProfile);

        productId=getIntent().getExtras().getString(getString(R.string.product_id));
        mTxtProdName.setText(getIntent().getExtras().getString(getString(R.string.product_title)));
    }

    @OnClick(R.id.btn_ask)
    public void editTxtAsk(View view) {
        if(!mEditTxtAskQue.getText().toString().isEmpty()) {
            mAskQue = mEditTxtAskQue.getText().toString();
            mProgressBar.setVisibility(View.VISIBLE);
            mFrameLayout.setAlpha((float) 0.6);
            userAueAns();
        }else {
            RCLog.showToast(this,getString(R.string.field_not_empty));
        }

    }

    /**
     * get user question and answer
     */
    public void userAueAns() {

        UserAskQueRequest userAskQueRequest = new UserAskQueRequest(productId, mAskQue);
        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<RootMessageInfo> call = service.getUserQueAns("Bearer " + mAccessToken, userAskQueRequest);
        call.enqueue(new Callback<RootMessageInfo>() {
            @Override
            public void onResponse(Call<RootMessageInfo> call, Response<RootMessageInfo> response) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
                if (response.isSuccessful()) {
                    RCLog.showToast(UserQueAnsActivity.this, getString(R.string.que_success));

                } else {
                }
            }

            @Override
            public void onFailure(Call<RootMessageInfo> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mFrameLayout.setAlpha((float) 1.0);
            }
        });
    }
}
