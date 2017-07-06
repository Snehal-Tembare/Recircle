package com.example.synerzip.recircle_android.ui.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.ProdRelatedMsg;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta Patil on 15/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO the implementation is in progress

public class MsgOwnerAdapter extends RecyclerView.Adapter<MsgOwnerAdapter.ViewHolder> {

    private List<ProdRelatedMsg> messageDetailsRespons = new ArrayList<>();
    private Context context;
    private RootMessageInfo mRootMessageInfo;

    public MsgOwnerAdapter(Context mContext,int  textview) {
        this.context = mContext;
    }

    public MsgOwnerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_owner_msg,
                viewGroup, false);
        return new MsgOwnerAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        //inflate respective layouts for renter n owner
        return super.getItemViewType(position);
    }

    public void onBindViewHolder(final MsgOwnerAdapter.ViewHolder viewHolder, int position) {
        ProdRelatedMsg prodRelatedMsg =mRootMessageInfo.getProdRelatedMsgs().get(position);
        viewHolder.mTxtOwnerMsgs.setText(prodRelatedMsg.getUser_msg());
    }

    public int getItemCount() {
        return this.messageDetailsRespons.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTxtOwnerMsgs;

        public ViewHolder(View view) {
            super(view);
            mTxtOwnerMsgs = (TextView) view.findViewById(R.id.txt_msg_owner);
        }
    }
}
