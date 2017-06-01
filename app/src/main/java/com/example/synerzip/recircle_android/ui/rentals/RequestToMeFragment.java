package com.example.synerzip.recircle_android.ui.rentals;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.OrderDetails;
import com.example.synerzip.recircle_android.models.UserRentings;
import com.example.synerzip.recircle_android.models.UserRequests;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import java.util.ArrayList;

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

    private RequestAdapter adapter;
    protected TextView mTxtNoReuests;

    protected RecyclerView mRecyclerRequests;

    public RequestToMeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_to_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtNoReuests = (TextView) view.findViewById(R.id.txt_no_requests);
        mRecyclerRequests = (RecyclerView) view.findViewById(R.id.recycler_requests);

    }

    public void communicateWithActivity(ArrayList<UserRequests> userRentingsArrayList) {
        this.userRequestsArrayList = userRentingsArrayList;

        if (userRequestsArrayList != null && userRequestsArrayList.size() != 0) {
            adapter = new RequestAdapter(getActivity(), userRequestsArrayList);
            mRecyclerRequests.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerRequests.setAdapter(adapter);
        } else {
            mTxtNoReuests.setVisibility(View.VISIBLE);
        }

    }
}
