package com.example.synerzip.recircle_android.ui.messages;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.models.user_messages.UserAskQueResponse;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 14/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO the implementation is in progress

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_msgs)
    protected Toolbar mToolbar;

    private RCAPInterface service;

    private SharedPreferences sharedPreferences;

    private boolean isLoggedIn;

    private String mAccessToken, mAskQue;

    @BindView(R.id.editTxt_send_msg)
    protected EditText mEditTxtSendMsg;

    private MsgOwnerAdapter mChatAdapter;

    @BindView(R.id.list_chat_msgs)
    protected RecyclerView mListViewMsgs;

    @BindView(R.id.img_send_msg)
    protected ImageView mImgSendMsg;

    private ArrayList<String> mListRenterMsgs;

    @BindView(R.id.txt_toolbar_user_name)
    protected TextView mTxtUserName;

    @BindView(R.id.progress_bar)
    protected RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //owner msgs
        mChatAdapter = new MsgOwnerAdapter(getApplicationContext(), R.id.txt_msg_owner);
        mListViewMsgs.setAdapter(mChatAdapter);

        //renter msgs
        mChatAdapter = new MsgOwnerAdapter(getApplicationContext(), R.id.txt_msg_owner);
        mListViewMsgs.setAdapter(mChatAdapter);

        Bundle bundle = getIntent().getExtras();
        mTxtUserName.setText(bundle.getString(getString(R.string.renter_name)));

        //get data from shared preferences
        sharedPreferences = getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(RCAppConstants.RC_SHARED_PREFERENCES_LOGIN_STATUS, false);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

    }

    @OnClick(R.id.img_send_msg)
    public void imgSendMsg(View view) {
        if (!mEditTxtSendMsg.getText().toString().isEmpty()) {
            mAskQue = mEditTxtSendMsg.getText().toString();
            sendUserMsg();
        } else {
            RCLog.showToast(this, getString(R.string.field_not_empty));
        }
    }

    /**
     * get user question and answer
     */
    public void sendUserMsg() {
        UserAskQueResponse userAskQueResponse = new UserAskQueResponse(AllMessagesActivity.userProductMsgId, mAskQue);
        mProgressBar.setVisibility(View.VISIBLE);
        if (ApiClient.getClient(this) != null) {
            service = ApiClient.getClient(this).create(RCAPInterface.class);
            Call<RootMessageInfo> call = service.getMsgResponse("Bearer " + mAccessToken, userAskQueResponse);
            call.enqueue(new Callback<RootMessageInfo>() {
                @Override
                public void onResponse(Call<RootMessageInfo> call, Response<RootMessageInfo> response) {
                    if (response.isSuccessful()) {
                        mListRenterMsgs = new ArrayList<>();
                        RCLog.showToast(ChatActivity.this, getString(R.string.msg_sent));
                    }
                }

                @Override
                public void onFailure(Call<RootMessageInfo> call, Throwable t) {
                    RCLog.showToast(ChatActivity.this, getString(R.string.something_went_wrong));
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
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
