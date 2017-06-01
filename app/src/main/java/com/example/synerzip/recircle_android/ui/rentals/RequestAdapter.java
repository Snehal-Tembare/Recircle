package com.example.synerzip.recircle_android.ui.rentals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.UserRentings;
import com.example.synerzip.recircle_android.models.UserRequests;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserRequests> userRequestsArrayList;

    private String formatedFromDate;
    private String formatedToDate;
    private int dayCount;

    RequestAdapter(Context context, ArrayList<UserRequests> userRequestsArrayList){
        mContext=context;
        this.userRequestsArrayList=userRequestsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pending_request, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRequests userRequest = userRequestsArrayList.get(position);


        if (userRequest.getUser()!= null) {
            if (userRequest.getUser().getUser_image_url() != null) {
                Picasso.with(mContext).load(userRequest.getUser().getUser_image_url())
                        .into(holder.mImgUser);
            }
        }

        if (userRequest.getUser().getFirst_name() != null ||
                userRequest.getUser().getLast_name() != null) {
            holder.mTxtUserName.setText(userRequest.getUser().getFirst_name() + " "
                    + userRequest.getUser().getLast_name());
        }

        if (userRequest.getRequest_id() != null) {
            holder.mTxtRequestId.setText(userRequest.getRequest_id());
        }

        DateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.ddd_mm));
        DateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.dd_mm_yyyy_hh_mm));
        Date fromDate = null;
        Date toDate = null;

        try {
            String orderOnDate = userRequest.getDate_on_order().toString();
            String orderFromDate = userRequest.getOrder_from_date().toString();
            String orderToDate = userRequest.getOrder_to_date().toString();

            Date onDate = formatter.parse(orderOnDate);
            fromDate = formatter.parse(orderFromDate);
            toDate = formatter.parse(orderToDate);

            holder.mTxtRequestLabel.setText("Requested on: " + simpleDateFormat.format(onDate));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calFromDate = Calendar.getInstance();
        Calendar calToDate = Calendar.getInstance();
        calFromDate.setTime(fromDate);
        calToDate.setTime(toDate);

        CharSequence monthFromDate = android.text.format.DateFormat
                .format(mContext.getString(R.string.month_format), fromDate);
        CharSequence monthToDate = android.text.format.DateFormat
                .format(mContext.getString(R.string.month_format), toDate);

        formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate + ", " + calFromDate.get(Calendar.YEAR);
        formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);

        if (monthFromDate.equals(monthToDate)) {
            formatedFromDate = calFromDate.get(Calendar.DATE) + "";
            formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);

        } else if (!monthFromDate.equals(monthToDate) && !(calFromDate.get(Calendar.YEAR) == calToDate.get(Calendar.YEAR))) {
            formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate + ", " + calFromDate.get(Calendar.YEAR);
            formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);

        } else if (!monthFromDate.equals(monthToDate)) {
            formatedFromDate = calFromDate.get(Calendar.DATE) + " " + monthFromDate;
            formatedToDate = calToDate.get(Calendar.DATE) + " " + monthToDate + ", " + calToDate.get(Calendar.YEAR);
        }

        holder.mTxtDate.setText(formatedFromDate + "-" + formatedToDate);

        if (userRequest.getUser_prod_images() != null) {
            if (userRequest.getProduct().getProduct_detail().getProduct_image_url() != null) {
                Picasso.with(mContext).load(userRequest.getProduct().getProduct_detail().getProduct_image_url() )
                        .into(holder.mImgItem);
            }
        }

        long diff = toDate.getTime() - fromDate.getTime();
        dayCount = (int) diff / (24 * 60 * 60 * 1000);

        holder.mTxtDayCount.setText(dayCount + " days");

        holder.mTxtItemTitle.setText(userRequest.getProduct().getProduct_title());

        holder.mTxtPrice.setText(String.valueOf("$" + userRequest.getPrice_per_day() + "/day"));

        //To make first letter of status capital
        String status = userRequest.getStatus().substring(0, 1).toUpperCase() + userRequest.getStatus().substring(1);

        holder.mTxtStatus.setText(status);
    }

    @Override
    public int getItemCount() {
        return userRequestsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView mImgUser;
        private TextView mTxtUserName;
        private TextView mTxtRequestId;
        private TextView mTxtRequestLabel;
        private ImageView mImgItem;
        private TextView mTxtItemTitle;
        private TextView mTxtDate;
        private TextView mTxtDayCount;
        private TextView mTxtPrice;
        private TextView mTxtStatus;


        public ViewHolder(View itemView) {
            super(itemView);
            mImgUser = (CircularImageView) itemView.findViewById(R.id.img_user);
            mTxtUserName = (TextView) itemView.findViewById(R.id.txt_user_name);
            mTxtRequestId = (TextView) itemView.findViewById(R.id.txt_request_id);
            mTxtRequestLabel = (TextView) itemView.findViewById(R.id.txt_request_label);
            mImgItem = (ImageView) itemView.findViewById(R.id.img_item);
            mTxtItemTitle = (TextView) itemView.findViewById(R.id.txt_item_title);
            mTxtDate = (TextView) itemView.findViewById(R.id.txt_date);
            mTxtDayCount = (TextView) itemView.findViewById(R.id.txt_days);
            mTxtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            mTxtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            itemView.findViewById(R.id.layout_buttons).setVisibility(View.VISIBLE);
        }
    }
}
