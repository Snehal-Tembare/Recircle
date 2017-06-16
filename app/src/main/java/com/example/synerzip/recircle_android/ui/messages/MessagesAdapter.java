package com.example.synerzip.recircle_android.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.OwnerProdRelatedMsg;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prajakta Patil on 8/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context mContext;
    private RootMessageInfo mRootMessageInfo;
    private OwnerProdRelatedMsg ownerProdRelatedMsg;

    public MessagesAdapter(Context mContext, RootMessageInfo mRootMessageInfo) {
        this.mContext = mContext;
        this.mRootMessageInfo = mRootMessageInfo;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_messages,
                viewGroup, false);
        return new MessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        ownerProdRelatedMsg = mRootMessageInfo.getOwnerProdRelatedMsgs().get(position);
        if (!ownerProdRelatedMsg.is_read()) {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorLightGrey));
        } else {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorWhite));
        }

        viewHolder.mTxtOwnerName.setText(ownerProdRelatedMsg.getUser().getFirst_name() +
                " " + ownerProdRelatedMsg.getUser().getLast_name());
        if (ownerProdRelatedMsg.getUser_product().getProduct_manufacturer_title() != null) {
            viewHolder.mTxtOwnerProdName.setText(ownerProdRelatedMsg.getUser_product()
                    .getProduct_manufacturer_title());
        }
        if (ownerProdRelatedMsg.getUser().getUser_image_url() != null) {

            Picasso.with(mContext)
                    .load(ownerProdRelatedMsg.getUser().getUser_image_url())
                    .placeholder(R.drawable.ic_user)
                    .into(viewHolder.mImgOwnerImage);
        }

    }

    @Override
    public int getItemCount() {
        return mRootMessageInfo.getOwnerProdRelatedMsgs().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        private TextView mTxtOwnerName, mTxtOwnerProdName, mTxtOwnerReqTime;
        private CircleImageView mImgOwnerImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.cardview_msgs);
            mImgOwnerImage = (CircleImageView) view.findViewById(R.id.img_user_profile);
            mTxtOwnerName = (TextView) view.findViewById(R.id.txt_user_name);
            mTxtOwnerProdName = (TextView) view.findViewById(R.id.txt_product_name);
            mTxtOwnerReqTime = (TextView) view.findViewById(R.id.txt_msg_date_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ownerProdRelatedMsg.set_read(true);
                    mCardView.setBackgroundColor(mCardView.getContext().getResources().getColor(R.color.colorWhite));

                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("renter_name", ownerProdRelatedMsg.getUser().getFirst_name() + " " +
                            ownerProdRelatedMsg.getUser().getLast_name());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    RCLog.showToast(v.getContext(), getAdapterPosition() + " clicked");

                }
            });
        }

    }
}
