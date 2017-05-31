package com.example.synerzip.recircle_android.ui.rentals;

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
import com.example.synerzip.recircle_android.models.UserRentings;

import java.util.ArrayList;


/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestFromMeFragment extends Fragment {
    private String TAG = "RequestFromMeFragment";
    public ArrayList<UserRentings> userRentingsArrayList;
    private RentingsAdapter adapter;

private TextView mTxtNoRentings;
    private RecyclerView mRecyclerRentings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_from_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtNoRentings= (TextView) view.findViewById(R.id.txt_no_requests);
        mRecyclerRentings = (RecyclerView) view.findViewById(R.id.recycler_renting);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, "setUserVisibleHint" + isVisibleToUser);
        if (isVisibleToUser) {
            if (((AllRequestsActivity) getActivity()).userRentingsArrayList != null) {
                this.userRentingsArrayList = ((AllRequestsActivity) getActivity()).userRentingsArrayList;

                if (userRentingsArrayList != null && userRentingsArrayList.size()!=0) {
                    Log.v(TAG, "Title" + userRentingsArrayList.get(0).getProduct().getProduct_title());
                    adapter = new RentingsAdapter(getActivity(), userRentingsArrayList);
                    mRecyclerRentings.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerRentings.setAdapter(adapter);
                }
            }else {
                mTxtNoRentings.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
