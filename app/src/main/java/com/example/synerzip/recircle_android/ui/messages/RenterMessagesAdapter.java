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
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Prajakta Patil on 11/7/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
class RenterMessagesAdapter extends RecyclerView.Adapter<RenterMessagesAdapter.ViewHolder> {
    private Context mContext;
    private RootMessageInfo mRootMessageInfo;
    private ProdRelatedMsg prodRelatedMsg;

    public RenterMessagesAdapter(Context mContext, RootMessageInfo mRootMessageInfo) {
        this.mContext = mContext;
        this.mRootMessageInfo = mRootMessageInfo;
    }

    @Override
    public RenterMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_renter_messages,
                viewGroup, false);
        return new RenterMessagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RenterMessagesAdapter.ViewHolder viewHolder, int position) {

        prodRelatedMsg = mRootMessageInfo.getOwnerRequestMsgs().get(position);
        if (!prodRelatedMsg.is_read()) {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorLightGrey));
        } else {
            viewHolder.mCardView.setBackgroundColor(viewHolder.mCardView.getContext()
                    .getResources().getColor(R.color.colorWhite));
        }

        viewHolder.mTxtRenterName.setText(prodRelatedMsg.getUser().getFirst_name() +
                " " + prodRelatedMsg.getUser().getLast_name());

        viewHolder.mTxtRenterProdName.setText(prodRelatedMsg.getUser_product().getProduct().getProduct_title());
        if (prodRelatedMsg.getUser().getUser_image_url() != null) {

            Picasso.with(mContext)
                    .load(prodRelatedMsg.getUser().getUser_image_url())
                    .placeholder(R.drawable.ic_user)
                    .into(viewHolder.mImgRenterImage);
        }
        viewHolder.mTxtRenterMsg.setText(prodRelatedMsg.getUser_msg());
        String reqDate = String.valueOf(prodRelatedMsg.getCreated_at());
        Date date = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.getTime().getDate();
            date = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy").parse(reqDate);

            String day = new SimpleDateFormat("dd").format(date);
            if (day.equals(calendar.getTime().getDate())) {
                String formattedDate = new SimpleDateFormat("HH:mm").format(date);
                viewHolder.mTxtRenterReqTime.setText(formattedDate);
            } else {
                String formattedDate = new SimpleDateFormat("dd MMM").format(date);
                viewHolder.mTxtRenterReqTime.setText(formattedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mRootMessageInfo.getOwnerRequestMsgs().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        private TextView mTxtRenterName, mTxtRenterProdName, mTxtRenterReqTime, mTxtRenterMsg;
        private CircularImageView mImgRenterImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.cardview_renter_msgs);
            mImgRenterImage = (CircularImageView) view.findViewById(R.id.img_renter_user_profile);
            mTxtRenterName = (TextView) view.findViewById(R.id.txt_renter_user_name);
            mTxtRenterProdName = (TextView) view.findViewById(R.id.txt_renter_product_name);
            mTxtRenterReqTime = (TextView) view.findViewById(R.id.txt_renter_msg_date_time);
            mTxtRenterMsg = (TextView) view.findViewById(R.id.txt_renter_msg);

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
