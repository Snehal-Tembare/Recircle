package com.example.synerzip.recircle_android.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.ProdRelatedMsg;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prajakta Patil on 8/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO class implemetation is in progress

public class OwnerMessagesAdapter extends RecyclerView.Adapter<OwnerMessagesAdapter.ViewHolder> {
    private Context mContext;
    private RootMessageInfo mRootMessageInfo;
    private ProdRelatedMsg prodRelatedMsg;

    public OwnerMessagesAdapter(Context mContext, RootMessageInfo mRootMessageInfo) {
        this.mContext = mContext;
        this.mRootMessageInfo = mRootMessageInfo;
    }

    @Override
    public OwnerMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_owner_messages,
                viewGroup, false);
        return new OwnerMessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        prodRelatedMsg = mRootMessageInfo.getOwnerProdRelatedMsgs().get(position);
        if (!prodRelatedMsg.is_read()) {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorLightGrey));
        } else {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorTransparent));
        }

        viewHolder.mTxtOwnerName.setText(prodRelatedMsg.getUser().getFirst_name() +
                " " + prodRelatedMsg.getUser().getLast_name());

        viewHolder.mTxtOwnerProdName.setText(prodRelatedMsg.getUser_product().getProduct().getProduct_title());

        if (prodRelatedMsg.getUser().getUser_image_url() != null) {
            Picasso.with(mContext)
                    .load(prodRelatedMsg.getUser().getUser_image_url())
                    .placeholder(R.drawable.ic_user)
                    .into(viewHolder.mImgOwnerImage);
        }
        viewHolder.mTxtOwnerMsg.setText(prodRelatedMsg.getUser_msg());

        String reqDate = String.valueOf(prodRelatedMsg.getCreated_at());
        Date date = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.getTime().getDate();
            date = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy").parse(reqDate);

            String day = new SimpleDateFormat("dd").format(date);
            if (day.equals(calendar.getTime().getDate())) {
                String formattedDate = new SimpleDateFormat("HH:mm").format(date);
                viewHolder.mTxtOwnerReqTime.setText(formattedDate);
            } else {
                String formattedDate = new SimpleDateFormat("dd MMM").format(date);
                viewHolder.mTxtOwnerReqTime.setText(formattedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mRootMessageInfo.getOwnerProdRelatedMsgs().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        private TextView mTxtOwnerName, mTxtOwnerProdName, mTxtOwnerReqTime, mTxtOwnerMsg;
        private CircleImageView mImgOwnerImage;

        public ViewHolder(View view) {
            super(view);

            mCardView = (CardView) view.findViewById(R.id.cardview_owner_msgs);
            mImgOwnerImage = (CircleImageView) view.findViewById(R.id.img_owner_user_profile);
            mTxtOwnerName = (TextView) view.findViewById(R.id.txt_owner_user_name);
            mTxtOwnerProdName = (TextView) view.findViewById(R.id.txt_owner_product_name);
            mTxtOwnerReqTime = (TextView) view.findViewById(R.id.txt_owner_msg_date_time);
            mTxtOwnerMsg = (TextView) view.findViewById(R.id.txt_owner_msg);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    prodRelatedMsg.set_read(true);
                    mCardView.setBackgroundColor(mCardView.getContext().getResources().getColor(R.color.colorWhite));
                    Intent intent = new Intent(v.getContext(), NewItemReqActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("renter_name", prodRelatedMsg.getUser().getFirst_name() + " " +
                            prodRelatedMsg.getUser().getLast_name());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
