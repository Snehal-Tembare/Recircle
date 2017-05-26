package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.synerzip.recircle_android.models.UserRequests;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserRequests> userRequestsArrayList;

    RequestAdapter(Context context, ArrayList<UserRequests> userRequestsArrayList){
        mContext=context;
        this.userRequestsArrayList=userRequestsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Inflater inflater= (Inflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
