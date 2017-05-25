package com.example.synerzip.recircle_android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserRequests;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestToMeFragment extends Fragment {

    private static final String TAG = "RequestToMeFragment";
    private ArrayList<UserRequests> userRequestsArrayList;

    protected RecyclerView mRequestToMeView;
    private RequestAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        Log.v(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userRequestsArrayList=getArguments().getParcelableArrayList(getString(R.string.user_requests_to_me));
        // Inflate the layout for this fragment
        Log.v(TAG,"onCreateView");
        return inflater.inflate(R.layout.fragment_request_to_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (userRequestsArrayList!=null &&
                userRequestsArrayList.size()!=0){
            Log.v(TAG,userRequestsArrayList.get(0).getProduct().getProduct_title());
        }

        mRequestToMeView= (RecyclerView) view.findViewById(R.id.requests_to_me);
        adapter=new RequestAdapter(getActivity());


    }
}
