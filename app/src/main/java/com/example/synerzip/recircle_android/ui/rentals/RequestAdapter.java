package com.example.synerzip.recircle_android.ui.rentals;

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
import com.example.synerzip.recircle_android.models.rentals.UserRequest;
import com.example.synerzip.recircle_android.models.rentals.UserRequests;
import com.example.synerzip.recircle_android.network.ApiClient;
import com.example.synerzip.recircle_android.network.RCAPInterface;
import com.example.synerzip.recircle_android.utilities.RCAppConstants;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Snehal Tembare on 22/5/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private static final String TAG = "RequestAdapter";
    private Context mContext;
    private ArrayList<UserRequests> userRequestsArrayList;
    private String formatedFromDate;
    private String formatedToDate;
    private int dayCount;
    private Date onDate = null;
    private String mAccessToken;
    private UserRequests userRequest;
    private DateFormat simpleDateFormat;
    private AlertDialog.Builder builder;

    private RCAPInterface service;


    RequestAdapter(Context context, ArrayList<UserRequests> userRequestsArrayList) {
        mContext = context;
        this.userRequestsArrayList = userRequestsArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pending_request, parent, false);
        service = ApiClient.getClient().create(RCAPInterface.class);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        userRequest = userRequestsArrayList.get(position);

        if (!userRequest.getStatus().equalsIgnoreCase("Pending")) {
            holder.mButtonsLayout.setVisibility(View.GONE);
        } else {
            holder.mButtonsLayout.setVisibility(View.VISIBLE);
        }

        if (userRequest.getUser() != null) {
            if (userRequest.getUser().getUser_image_url() != null) {
                Picasso.with(mContext).load(userRequest.getUser().getUser_image_url())
                        .placeholder(R.drawable.ic_user)
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
        } else {
            holder.mTxtRequestId.setText(mContext.getString(R.string.request_expired));
        }

        DateFormat formatter = new SimpleDateFormat(mContext.getString(R.string.ddd_mm));
        simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.dd_mm_yyyy_hh_mm));
        Date fromDate = null;
        Date toDate = null;

        try {
            String orderOnDate = userRequest.getDate_on_order().toString();
            String orderFromDate = userRequest.getOrder_from_date().toString();
            String orderToDate = userRequest.getOrder_to_date().toString();

            onDate = formatter.parse(orderOnDate);
            fromDate = formatter.parse(orderFromDate);
            toDate = formatter.parse(orderToDate);

            holder.mTxtRequestLabel.setText(mContext.getString(R.string.requested_on)
                    + " " + simpleDateFormat.format(onDate));

            //Adding 24hours in Request Time
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(onDate);
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            if (!userRequest.getStatus().isEmpty()) {
                if (userRequest.getStatus().equalsIgnoreCase(mContext.getString(R.string.approve))) {
                    holder.mTxtStatus.setText(mContext.getString(R.string.approved));
                } else if (userRequest.getStatus().equalsIgnoreCase(mContext.getString(R.string.decline))) {
                    holder.mTxtStatus.setText(mContext.getString(R.string.declined));
                }else if (userRequest.getStatus().equalsIgnoreCase(mContext.getString(R.string.cancelled))) {
                    holder.mTxtStatus.setText(mContext.getString(R.string.cancelled));
                } else {
                    holder.mTxtStatus.setText(mContext.getString(R.string.time_to_respond) + " " +
                            simpleDateFormat.format(calendar.getTime()));
                }
            }

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
            if (userRequest.getUser_prod_images() != null
                    && userRequest.getUser_prod_images().size() != 0) {
                Picasso.with(mContext).load(userRequest.getUser_prod_images()
                        .get(0).getUser_prod_image_url())
                        .placeholder(R.drawable.ic_camera)
                        .into(holder.mImgItem);
            }
        }

        long diff = toDate.getTime() - fromDate.getTime();
        dayCount = (int) Math.abs(diff / (24 * 60 * 60 * 1000));

        holder.mTxtDayCount.setText(dayCount + " " + mContext.getString(R.string.days));

        holder.mTxtItemTitle.setText(userRequest.getProduct().getProduct_title());

        holder.mTxtPrice.setText(String.valueOf("$" + userRequest.getPrice_per_day()
                + mContext.getString(R.string.per_day)));

        holder.mImgApprove.setTag(0);
        holder.mImgDecline.setTag(1);

        TODO:
        //Dialog for approve request

        holder.mImgApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionDialog((Integer) v.getTag(), position);
            }
        });
        holder.mImgDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionDialog((Integer) v.getTag(), position);
            }
        });

    }


    private void showActionDialog(final int tag, int position) {
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
        mBtnApprove.setText(mContext.getString(R.string.confirm_and_cancel));
        userRequest = userRequestsArrayList.get(position);
        if (userRequest != null) {
            if (userRequest.getUser() != null) {
                if (userRequest.getUser().getUser_image_url() != null) {
                    Picasso.with(mContext).load(userRequest.getUser().getUser_image_url())
                            .placeholder(R.drawable.ic_user)
                            .into(mImgRequestres);
                }
                mTxtItemName.setText(userRequest.getProduct().getProduct_title());
            }

            if (userRequest.getUser() != null) {
                mTxtUserName.setText(userRequest.getUser().getFirst_name() + " "
                        + userRequest.getUser().getLast_name());
            }

            mTxtRequestedOn.setText(mContext.getString(R.string.requested_on) +
                    " " + simpleDateFormat.format(onDate));

            Log.v(TAG, "***" + userRequest.getUser_prod_msg().getUser_prod_msg_id());

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(RCAppConstants.RC_SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
            mAccessToken = sharedPreferences.getString(RCAppConstants.RC_SHARED_PREFERENCES_ACCESS_TOKEN, mAccessToken);

            final UserRequest request = new UserRequest();

            if (tag == 0) {
                request.setAction(mContext.getString(R.string.approve));
                mBtnApprove.setText(mContext.getString(R.string.approve));
            } else {
                request.setAction(mContext.getString(R.string.decline));
                mBtnApprove.setText(mContext.getString(R.string.decline));
            }

            mBtnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "***" + userRequest.getUser_prod_msg().getUser_prod_msg_id());

                    //TODO:Waiting for stripe Integration
                    alert.cancel();

                    if (tag == 1 && mEdtMsg.getText().toString().isEmpty()) {
                        mEdtMsg.setError(mContext.getString(R.string.enter_message));
                        return;
                    }else {
                        request.setUser_msg(mEdtMsg.getText().toString());
                    }
                    Call<UserRequest> call = service.actionOnRequest(userRequest.getUser_prod_msg().getUser_prod_msg_id(),
                            "Bearer " + mAccessToken, request);
                    call.enqueue(new Callback<UserRequest>() {
                        @Override
                        public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Log.v("**onResponse", response.body().getUser_msg());
                                    alert.cancel();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRequest> call, Throwable t) {

                        }
                    });

                }
            });

        }

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
        private ImageView mImgApprove;
        private ImageView mImgDecline;
        private LinearLayout mButtonsLayout;


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
            mImgApprove = (ImageView) itemView.findViewById(R.id.img_approve);
            mImgDecline = (ImageView) itemView.findViewById(R.id.img_decline);
            mButtonsLayout = (LinearLayout) itemView.findViewById(R.id.layout_buttons);

            itemView.findViewById(R.id.layout_buttons).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.layout_cancel_order).setVisibility(View.GONE);
        }
    }
}
