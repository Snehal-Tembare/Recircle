package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context mContext;

    RequestAdapter(Context context){
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
