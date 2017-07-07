package com.example.synerzip.recircle_android.ui.messages;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserMessages.CancelUserProdRequest;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prajakta Patil on 20/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO class implemetation is in progress

public class NewItemReqActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected android.support.v7.widget.Toolbar mToolbar;

    private RCAPInterface service;

    private String mAccessToken, mUserProdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_req);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.common_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("New Request");

        SharedPreferences sharedPreferences = getSharedPreferences
                (RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mAccessToken = sharedPreferences.getString
                (RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

    }

    @OnClick(R.id.btn_cancel_req)
    public void btnCancelReq(View view) {
        cancelReqDialog();
    }

    /**
     * dialog for cancel user product request
     */
    private void cancelReqDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_cancel_request);
        dialog.setTitle(getString(R.string.cancel_req));
        final EditText mEditTxtReason = (EditText) dialog.findViewById(R.id.edit_txt_reason);

        TextView txtDismiss = (TextView) dialog.findViewById(R.id.txt_dismiss);
        txtDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtCancelReq = (TextView) dialog.findViewById(R.id.txt_confirm_cancel_req);
        txtCancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mCancelReason = mEditTxtReason.getText().toString();
                CancelUserProdRequest cancelUserProdRequest = new CancelUserProdRequest(mUserProdId, mCancelReason);
                service = ApiClient.getClient().create(RCAPInterface.class);
                Call<RootMessageInfo> call = service.cancelProdRequest("Bearer " + mAccessToken, cancelUserProdRequest);
                call.enqueue(new Callback<RootMessageInfo>() {
                    @Override
                    public void onResponse(Call<RootMessageInfo> call, Response<RootMessageInfo> response) {

                    }
                    @Override
                    public void onFailure(Call<RootMessageInfo> call, Throwable t) {
                    }
                });

                dialog.show();
            }
        });
    }
}