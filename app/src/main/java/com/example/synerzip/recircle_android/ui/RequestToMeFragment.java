package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.OrderDetails;
import com.example.synerzip.recircle_android.models.UserRentings;
import com.example.synerzip.recircle_android.models.UserRequests;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestToMeFragment extends Fragment {

    private static final String TAG = "RequestToMeFragment";
    private ArrayList<UserRequests> userRequestsArrayList;
    private ArrayList<UserRentings> userRentingsArrayList;

    protected RecyclerView mRequestToMeView;
    private RequestAdapter adapter;
    private RCAPInterface service;
    private String mUserId;
    private String mAccessToken;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    protected TextView mTxtNoReuests;

   public RequestToMeFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_to_me, container, false);
        ButterKnife.bind(getActivity());

        // Inflate the layout for this fragment
        Log.v(TAG,"onCreateView");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        mUserId = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_USERID, mUserId);
        mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

        service = ApiClient.getClient().create(RCAPInterface.class);
        Call<OrderDetails> call = service.getOrderDetails("Bearer " + mAccessToken);
        ((AllRequestsActivity)getActivity()).mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {
                Log.v(TAG, "before isSuccessful");
                if (response.isSuccessful()) {
                    if (response.body() != null
                            && response.body().getUserRequests() != null
                            && response.body().getUserRequests().size() != 0) {
                        ((AllRequestsActivity)getActivity()).mProgressBar.setVisibility(View.GONE);
                        userRequestsArrayList = response.body().getUserRequests();
                        if (userRequestsArrayList!=null && userRequestsArrayList.size()!=0){
                            Log.v(TAG,"Title"+userRequestsArrayList.get(0).getProduct().getProduct_title());
                        }
                    }else {
                        mTxtNoReuests.setVisibility(View.VISIBLE);
                    }

                    if (response.body().getUserRentings() != null
                            && response.body().getUserRentings().size() != 0) {
                        ((AllRequestsActivity)getActivity()).mProgressBar.setVisibility(View.GONE);
                        userRentingsArrayList = response.body().getUserRentings();
                        if (userRentingsArrayList!=null && userRentingsArrayList.size()!=0){
                            Log.v(TAG,"Title"+userRentingsArrayList.get(0).getProduct().getProduct_title());
                            onFragmentInteractionListener.sendDataToActivity(userRentingsArrayList);
                        }
                    }
                    Log.v(TAG, "after isSuccessful");

                } else {
                    RCLog.showToast(getActivity(), getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onFragmentInteractionListener= (OnFragmentInteractionListener) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtNoReuests= (TextView) view.findViewById(R.id.txt_no_requests);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void sendDataToActivity(ArrayList<UserRentings> userRentingsArrayList);
    }
}
