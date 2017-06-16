package com.example.synerzip.recircle_android.ui.rentals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.rentals.CancelOrder;
import com.example.synerzip.recircle_android.models.rentals.UserRentings;
import com.example.synerzip.recircle_android.models.rentals.UserRequest;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.example.synerzip.recircle_android.utilities.RCLog;
import com.google.android.gms.common.api.Api;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Snehal Tembare on 26/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RentingsAdapter extends RecyclerView.Adapter<RentingsAdapter.ViewHolder> {

    private static final String TAG = "RentingsAdapter";
    private Context mContext;
    private ArrayList<UserRentings> userRentingsArrayList;
    private String formatedFromDate;
    private String formatedToDate;
    private int dayCount;
    private AlertDialog.Builder builder;
    private UserRentings userRentings;
    private String mAccessToken;
    private DateFormat simpleDateFormat;
    private Date onDate = null;
    private RCAPInterface service;


    public RentingsAdapter(Activity activity, ArrayList<UserRentings> userRentingsArrayList) {
        mContext = activity;
        this.userRentingsArrayList = userRentingsArrayList;
    }

    @Override
    public RentingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pending_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        userRentings = userRentingsArrayList.get(position);
        service = ApiClient.getClient().create(RCAPInterface.class);

        if (userRentings.getUser_prod_images() != null) {
            if (userRentings.getUser().getUser_image_url() != null) {
                Picasso.with(mContext).load(userRentings.getUser().getUser_image_url())
                        .into(holder.mImgUser);
            }
        }

        if (userRentings.getUser().getFirst_name() != null ||
                userRentings.getUser().getLast_name() != null) {
            holder.mTxtUserName.setText(userRentings.getUser().getFirst_name() + " "
                    + userRentings.getUser().getLast_name());
        }

        if (userRentings.getRequest_id() != null) {
            holder.mTxtRequestId.setText(userRentings.getRequest_id());
        } else {
            holder.mTxtRequestId.setText(mContext.getString(R.string.request_expired));
        }

        DateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.ddd_mm));
        simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.dd_mm_yyyy_hh_mm));
        Date fromDate = null;
        Date toDate = null;

        try {
            String orderOnDate = userRentings.getDate_on_order().toString();
            String orderFromDate = userRentings.getOrder_from_date().toString();
            String orderToDate = userRentings.getOrder_to_date().toString();

            onDate = formatter.parse(orderOnDate);
            fromDate = formatter.parse(orderFromDate);
            toDate = formatter.parse(orderToDate);

            holder.mTxtRequestLabel.setText(mContext.getString(R.string.requested_on) +
                    " " + simpleDateFormat.format(onDate));


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

        if (userRentings.getUser_prod_images() != null) {
            if (userRentings.getUser_prod_images().get(0).getUser_prod_image_url() != null) {
                Picasso.with(mContext).load(userRentings.getUser_prod_images()
                        .get(0).getUser_prod_image_url())
                        .into(holder.mImgItem);
            }
        }

        //Calculate no of days
        long diff = toDate.getTime() - fromDate.getTime();
        dayCount = (int) Math.abs(diff / (24 * 60 * 60 * 1000));

        holder.mTxtDayCount.setText(dayCount + " " + mContext.getString(R.string.days));

        holder.mTxtItemTitle.setText(userRentings.getProduct().getProduct_title());

        holder.mTxtPrice.setText(String.valueOf("$" + userRentings.getPrice_per_day()
                + mContext.getString(R.string.per_day)));


        if (!userRentings.getStatus().isEmpty()) {
            if (userRentings.getStatus().equalsIgnoreCase(mContext.getString(R.string.approve))) {
                holder.mTxtStatus.setText(mContext.getString(R.string.approved));
            } else if (userRentings.getStatus().equalsIgnoreCase(mContext.getString(R.string.decline))) {
                holder.mCancelOrderLayout.setVisibility(View.GONE);
                holder.mTxtStatus.setText(mContext.getString(R.string.declined));
            } else if (userRentings.getStatus().equalsIgnoreCase(mContext.getString(R.string.cancelled))) {
                holder.mTxtStatus.setText(mContext.getString(R.string.cancelled));
                holder.mCancelOrderLayout.setVisibility(View.GONE);
            } else {
                holder.mTxtStatus.setText(mContext.getString(R.string.wating_for_owner));
            }
        }
        holder.mCancelOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionDialog(position);
            }
        });
    }

    private void showActionDialog(int position) {
        builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.approve_dialog, null);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();

        TextView mTxtUserName = (TextView) view.findViewById(R.id.txt_requesters_name);
        TextView mTxtItemName = (TextView) view.findViewById(R.id.txt_item_name);
        TextView mTxtRequestedOn = (TextView) view.findViewById(R.id.txt_requested_on);
        CircularImageView mImgRequestres = (CircularImageView) view.findViewById(R.id.img_requesters);
        final EditText mEdtMsg = (EditText) view.findViewById(R.id.edt_msg_to_requester);
        Button mBtnApprove = (Button) view.findViewById(R.id.btn_approve);

        userRentings = userRentingsArrayList.get(position);

        if (userRentings != null) {
            if (userRentings.getUser() != null) {
                if (userRentings.getUser().getUser_image_url() != null) {
                    Picasso.with(mContext).load(userRentings.getUser().getUser_image_url())
                            .placeholder(R.drawable.ic_user)
                            .into(mImgRequestres);
                }
                mTxtItemName.setText(userRentings.getProduct().getProduct_title());
            }

            if (userRentings.getUser() != null) {
                mTxtUserName.setText(userRentings.getUser().getFirst_name() + " "
                        + userRentings.getUser().getLast_name());
            }

            mTxtRequestedOn.setText(mContext.getString(R.string.requested_on) +
                    " " + simpleDateFormat.format(onDate));

            Log.v(TAG, "***" + userRentings.getUser_prod_msg().getUser_prod_msg_id());

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
            mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);


            mBtnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "***" + userRentings.getUser_prod_msg().getUser_prod_msg_id());

                    //TODO:Waiting for stripe Integration
                    alert.cancel();
                    CancelOrder cancelOrder=new CancelOrder();
                    cancelOrder.setUser_msg(mEdtMsg.getText().toString());
                    mEdtMsg.setError(mContext.getString(R.string.enter_message));
                    cancelOrder.setUser_prod_order_id(userRentings.getUser_prod_order_id());

                    Call<CancelOrder> call=service.cancelOrder(cancelOrder);
                    call.enqueue(new Callback<CancelOrder>() {
                        @Override
                        public void onResponse(Call<CancelOrder> call, Response<CancelOrder> response) {
                            if (response.isSuccessful()){
                                RCLog.showToast(mContext,mContext.getString(R.string.order_cancelled));
                            }else {
                                RCLog.showToast(mContext,mContext.getString(R.string.something_went_wrong));
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelOrder> call, Throwable t) {

                        }
                    });

                    }
            });

        }


    }

    @Override
    public int getItemCount() {
        return userRentingsArrayList.size();
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
        private LinearLayout mCancelOrderLayout;


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
            mCancelOrderLayout = (LinearLayout) itemView.findViewById(R.id.layout_cancel_order);

            itemView.findViewById(R.id.layout_buttons).setVisibility(View.GONE);
            mCancelOrderLayout.setVisibility(View.VISIBLE);
        }
    }

}
